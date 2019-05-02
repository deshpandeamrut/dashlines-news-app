package com.eurofins.amrut.springbootplay.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.eurofins.amrut.springbootplay.entity.Article;
import com.eurofins.amrut.springbootplay.entity.Response;
import com.eurofins.amrut.springbootplay.entity.Source;

@RestController
public class NewsService {
	
	@Autowired
	NewsServiceImpl newsServiceImpl;
	
	static List<Article> articleList = null;
	static List<Article> techArticleList = null;
	static Map<String, List<Article>> cache = new HashMap<>();
	static long lastRefreshTime = 0;
	
	@RequestMapping(value = "/news/getLatest")
	@ResponseBody
	public List<Article> getLatestNews() {
		return newsServiceImpl.getLatestNews();
	}

	@RequestMapping("/news/source/{sourceName}")
	@ResponseBody
	public List<Article> getNewsFromSource(@PathVariable String sourceName) {
		return newsServiceImpl.getNewsFromSource(sourceName);
	}

	@RequestMapping(value = "/news/search/{searchText}")
	@ResponseBody
	public List<Article> getLatestNewsForSearchText(@PathVariable String searchText) {
		return newsServiceImpl.getNewsFromSource(searchText);
	}

	@RequestMapping(value = "/news/all/search/{searchText}")
	@ResponseBody
	public List<Article> getAllNewsForSearchText(@PathVariable String searchText) {
		return newsServiceImpl.getAllNewsForSearchText(searchText);
	}

	@RequestMapping(value = "/news/category/{category}")
	@ResponseBody
	public List<Article> getNewsForCategory(@PathVariable String category, boolean extendedSearch) {
		return newsServiceImpl.getNewsForCategory(category, extendedSearch);
	}

	@RequestMapping(value = "/news/myFeed")
	@ResponseBody
	public List<Article> getLatestFeed() {
		return newsServiceImpl.getLatestFeed();
	}

	@RequestMapping(value = "/news/sources")
	@ResponseBody
	public List<Source> getNewsSources() {
		return newsServiceImpl.getNewsSources();
	}
	
	@RequestMapping(value = "/news/smartFeed")
	@ResponseBody
	public List<Article> getSmartNews() {
		return newsServiceImpl.getSmartNews("");
	}
}