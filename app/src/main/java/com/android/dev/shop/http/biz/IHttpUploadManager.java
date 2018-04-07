package com.android.dev.shop.http.biz;


import com.android.common.volley.Request;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;

public interface IHttpUploadManager {
	

	/**
	 * 上传图片到服务器（服务器会生成响应缩略图）-云存储
	 */
	Request<?> upLoadImageWithThumbCloud(String filePath, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg);
	
}
