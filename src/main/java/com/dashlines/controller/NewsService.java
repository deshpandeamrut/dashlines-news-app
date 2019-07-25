package com.dashlines.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dashlines.entity.Article;
import com.dashlines.entity.Source;
import com.dashlines.service.NewsServiceImpl;

@RestController
public class NewsService {

	@Autowired
	NewsServiceImpl newsServiceImpl;

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
		List<Article> temp = newsServiceImpl.getLatestNewsForSearchText(searchText);
		if (temp.isEmpty()) {
			temp = newsServiceImpl.getAllNewsForSearchText(searchText);
		}
		return temp;
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
	public Map<String, List<Article>> getLatestFeed() {
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

	@RequestMapping(value = "/news/mySources")
	@ResponseBody
	public Map<String, List<Article>> getMySourcesNews() {
		return newsServiceImpl.getMySourcesNews();
	}
}