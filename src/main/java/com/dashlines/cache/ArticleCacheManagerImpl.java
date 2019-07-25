package com.dashlines.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.dashlines.entity.Article;

@Configuration
public class ArticleCacheManagerImpl implements ArticleCacheManager {

	@Value("${news.articles.refresh.interval.in.minutes}")
	private int articlesRefreshInterval;

	private RedissonClient redisson;

	@Autowired
	ArticleCacheManagerImpl(RedissonClient redisson) {
		this.redisson = redisson;
	}

	@Override
	public List<Article> getList(String key) {
		RMapCache<String, List<Article>> map = redisson.getMapCache(key);
		return (List<Article>) map.get(key);
	}

	@Override
	public void cacheArticleList(String key, List<Article> articleList) {
		RMapCache<String, List<Article>> map = redisson.getMapCache(key);
		map.put(key, articleList, articlesRefreshInterval, TimeUnit.MINUTES);
	}
}
