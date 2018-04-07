package com.android.dev.shop.android.model;

import java.io.Serializable;

public class CloudUriData implements Serializable {
	
	private String url;
	private String Content_MD5;
	private String thumb_url;
	private String uri;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent_MD5() {
		return Content_MD5;
	}
	public void setContent_MD5(String content_MD5) {
		Content_MD5 = content_MD5;
	}
	public String getThumb_url() {
		return thumb_url;
	}
	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public static class CloudUriDataModel {
		public int code;
		public CloudUriData data;
	}
}
