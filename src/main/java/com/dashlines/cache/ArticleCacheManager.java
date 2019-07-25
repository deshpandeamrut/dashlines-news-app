package com.dashlines.cache;

import java.util.List;

import com.dashlines.entity.Article;

public interface ArticleCacheManager {

	List<Article> getList(String id);

	void cacheArticleList(String key, List<Article> articleList);
}
