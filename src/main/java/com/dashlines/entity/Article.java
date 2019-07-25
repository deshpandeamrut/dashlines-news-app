package com.dashlines.entity;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("Article")
public class Article implements Serializable {
	
	private String id;
	 
	private String sourceName;
	private String sourceId;

	@JsonProperty("source")
	private void unpackNameFromNestedObject(Map<String, String> source) {
		sourceName = source.get("name");
		sourceId = source.get("id");
	}

	@JsonProperty("title")
	private String title;

	@JsonProperty("author")
	private String author;

	@JsonProperty("description")
	private String description;

	@JsonProperty("url")
	private String url;

	@JsonProperty("content")
	private String content;

	@JsonProperty("publishedAt")
	private String publishedAt;

	@JsonProperty("urlToImage")
	private String urlToImage;

	private int weight;
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getUrlToImage() {
		return urlToImage;
	}

	public void setUrlToImage(String urlToImage) {
		this.urlToImage = urlToImage;
	}

	public String getSource() {
		return sourceName;
	}

	public void setSource(String source) {
		this.sourceName = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}
	
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return "Article [sourceName=" + sourceName + ", sourceId=" + sourceId + ", title=" + title + ", author="
				+ author + ", description=" + description + ", url=" + url + ", content=" + content + ", publishedAt="
				+ publishedAt + ", urlToImage=" + urlToImage + ", weight=" + weight + "]";
	}


}
