package com.dashlines.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.dashlines.entity.Source;

@Configuration
public class SourcesCacheManagerImpl implements SourcesCacheManager {

	private RedissonClient redisson;
	
	@Autowired
	SourcesCacheManagerImpl(RedissonClient redisson) {
		this.redisson = redisson;
	}

	
	@Value("${news.sources.refresh.interval.in.days}")
	private int sourcesRefreshInterval;

	@Override
	public List<Source> getList(String key) {
		RMapCache<String, List<Source>> map = redisson.getMapCache(key);
		return (List<Source>) map.get(key);
	}

	@Override
	public void cacheSourceList(String key, List<Source> sourceList) {
		RMapCache<String, List<Source>> map = redisson.getMapCache(key);
		map.put(key, sourceList, sourcesRefreshInterval, TimeUnit.DAYS);
	}

}
