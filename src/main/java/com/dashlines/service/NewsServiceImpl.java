package com.dashlines.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.dashlines.entity.Article;
import com.dashlines.entity.Response;
import com.dashlines.entity.Source;
import com.dashlines.entity.SourceResponse;

@Component
public class NewsServiceImpl {

	static List<Article> articleList = null;
	static List<Article> techArticleList = null;
	static Map<String, List<Article>> cache = new HashMap<>();
	static long lastRefreshTime = 0;

	@Value("${news.api.key}")
	private String newsApiKey;

	@Value("${user.categories}")
	private String userCategories;

	@Value("${news.refresh.interval}")
	private double refreshInterval;

	@Value("#{'${news.technology.categories}'.split(',')}")
	private List<String> technologySourceList;

	@Value("#{'${news.general.categories}'.split(',')}")
	private List<String> generalSourceList;

	@Value("#{'${news.sports.categories}'.split(',')}")
	private List<String> sportsSourceList;

	@Value("#{'${news.source.negate}'.split(',')}")
	private List<String> newsNegateSourceList;

	@Value("#{'${user.favourite.words}'.split(',')}")
	private List<String> userFavouriteWordList;

	@Value("${news.technology.categories}")
	private String technologySources;

	private static List<Source> newsSourceList = new ArrayList<>();

	public List<Source> getNewsSources() {
		final String uri = "https://newsapi.org/v2/sources?language=en&apiKey=" + newsApiKey;
		for (String src : technologySourceList) {
			System.out.println(src);
		}
		if (newsSourceList.isEmpty()) {
			newsSourceList = doRestCallForSources(uri);
		}
		return newsSourceList;
	}

	private List<Source> doRestCallForSources(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		SourceResponse result = restTemplate.getForObject(uri, SourceResponse.class);
		return result.getSources();

	}

	private List<Article> doRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		Response result = restTemplate.getForObject(uri, Response.class);
		return result.getArticles();

	}

	public List<Article> getSmartNews(String url) {

		Date now = new Date();
		long nowMs = now.getTime();
		List<Article> latestArticles;
		if (cache.get("smartNews") == null || (nowMs - lastRefreshTime) > refreshInterval) {
			latestArticles = doRestCall(url);
			List<String> sourceList = new ArrayList<>();
			sourceList.addAll(generalSourceList);
			sourceList.addAll(technologySourceList);
			sourceList.addAll(sportsSourceList);
			assignWeight(latestArticles, sourceList);
			sortArticlesByWeight(latestArticles);
			cache.put("smartNews", latestArticles);
			lastRefreshTime = nowMs;
		}
		return cache.get("smartNews");
	}

	private void sortArticlesByWeight(List<Article> latestArticles) {
		Collections.sort(latestArticles, new Comparator<Article>() {
			@Override
			public int compare(Article o1, Article o2) {
				return o2.getWeight().compareTo(o1.getWeight());
			}
		});
	}

	private void assignWeight(List<Article> latestArticles, List<String> sources) {
		for (Article article : latestArticles) {
			String sourceName = article.getSourceName();
			String sourceId = article.getSourceId();
			String sourceUrl = article.getUrl();
			for (String source : sources) {
				System.out.println(source);
				if ((sourceName != null && sourceName.toLowerCase().contains(source))
						|| (sourceId != null && sourceId.toLowerCase().contains(source))
						|| (sourceUrl != null && sourceUrl.contains(source))) {
					article.setWeight(article.getWeight() + 1);
				}
			}
			for (String favWord : userFavouriteWordList) {
				if (article.getTitle().toLowerCase().contains(favWord)) {
					article.setWeight(article.getWeight() + 1);
				}
			}
			for (String source : newsNegateSourceList) {
				if ((sourceName != null && sourceName.toLowerCase().contains(source))
						|| (sourceId != null && sourceId.toLowerCase().contains(source))
						|| (sourceUrl != null && sourceUrl.contains(source))) {
					article.setWeight(article.getWeight() - 1);
				}
			}
		}
	}

	public List<Article> getLatestNews() {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey=" + newsApiKey;
		Date now = new Date();
		long nowMs = now.getTime();
		return getSmartNews(uri);
	}

	public List<Article> getNewsFromSource(String sourceName) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey=" + newsApiKey + "&sources="
				+ sourceName;
		if (sourceName == null) {
			return null;
		}
		Date now = new Date();
		long nowMs = now.getTime();
		if (cache.get(sourceName) == null || (nowMs - lastRefreshTime) > refreshInterval) {
			articleList = doRestCall(uri);
			cache.put(sourceName, articleList);
			lastRefreshTime = nowMs;
		}
		return cache.get(sourceName);
	}

	public List<Article> getLatestNewsForSearchText(String searchText) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey=" + newsApiKey + "&q=" + searchText;
		return doRestCall(uri);
	}

	public List<Article> getAllNewsForSearchText(String searchText) {
		final String uri = "https://newsapi.org/v2/everything?language=en&apiKey=" + newsApiKey + "&q=" + searchText;
		return doRestCall(uri);
	}

	public List<Article> getNewsForCategory(String category, boolean extendedSearch) {
		String country = "";
		List<String> indianCategoryList = new ArrayList<>(Arrays.asList("politics", "automobile", "ipl"));
		if (indianCategoryList.contains(category)) {
			country = "in";
		}
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=" + country + "&apiKey="
				+ newsApiKey + "&category=" + category;
		Date now = new Date();
		long nowMs = now.getTime();
		if (cache.get(category) == null || (lastRefreshTime - nowMs) > refreshInterval) {
			List<Article> articles = doRestCall(uri);
			if (articles.isEmpty()) {
				articles = getLatestNewsForSearchText(category);
			}
			if (extendedSearch && articles.isEmpty()) {
				articles = getAllNewsForSearchText(category);
			}
			List<String> sourceList;
			switch (category) {
			case "technlogy":
				sourceList = technologySourceList;
				break;
			case "sports":
				sourceList = sportsSourceList;
				break;
			default:
				sourceList = generalSourceList;
				break;
			}
			assignWeight(articles, sourceList);
			sortArticlesByWeight(articles);
			cache.put(category, articles);
			lastRefreshTime = nowMs;
		}
		return cache.get(category);
	}

	@RequestMapping(value = "/news/myFeed")
	@ResponseBody
	public Map<String, List<Article>> getLatestFeed() {
		Map<String, List<Article>> myFeedMap = new HashMap<String, List<Article>>();
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=us&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f";
		for (String category : userCategories.split(",")) {
			List<Article> temp = getNewsForCategory(category.split(":")[1], false);
			myFeedMap.put(category.split(":")[1], temp);
		}
		return myFeedMap;

	}

	public Map<String,List<Article>> getMySourcesNews() {
		Map<String, List<Article>> myFeedMap = new HashMap<String, List<Article>>();
		List<String> sourceList = new ArrayList<>();
		sourceList.addAll(generalSourceList);
		sourceList.addAll(technologySourceList);
		sourceList.addAll(sportsSourceList);
		for (String source : sourceList) {
			List<Article> temp = getNewsFromSource(source);
			if(!temp.isEmpty()) {
				myFeedMap.put(source, temp);
			}
		}
		return myFeedMap;
	}
}
