package com.dashlines.cache;

import java.util.List;

import com.dashlines.entity.Article;
import com.dashlines.entity.Source;

public interface SourcesCacheManager {

	List<Source> getList(String id);

	void cacheSourceList(String key, List<Source> sourceList);

}
