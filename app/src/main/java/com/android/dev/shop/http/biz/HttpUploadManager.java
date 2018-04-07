package com.android.dev.shop.http.biz;


import com.android.common.volley.Request;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;

public class HttpUploadManager extends HttpBaseManager implements IHttpUploadManager{

	public HttpUploadManager(String tag) {
		super(tag);
	}

	@Override
	public Request<?> upLoadImageWithThumbCloud(String filePath,
												HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
		Request<?> request = getManager().upLoadImageWithThumbCloud(filePath, listener, msg);
		addRequest(request, msg);
		return null;
	}

}
