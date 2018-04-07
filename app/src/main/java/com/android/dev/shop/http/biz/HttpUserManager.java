package com.android.dev.shop.http.biz;


import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.shop.http.framework.HttpStringRequest;

public class HttpUserManager extends HttpBaseManager implements IHttpUserManager {

	public HttpUserManager(String tag) {
		super(tag);
	}


	@Override
	public HttpStringRequest beginLogin(String userName, String password, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
		HttpStringRequest request = getManager().beginLogin(userName, password, listener, msg);
		addRequest(request, msg);
		return request;
	}

	@Override
	public HttpStringRequest getCheckNum(String phone, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
		return null;
	}

	@Override
	public HttpStringRequest registerUser(String phone, String checkCode, String password, String imageHash, String imageName, String nickname, String user_sex, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
		return null;
	}

	@Override
	public HttpStringRequest resetPassword(String phone, String checkCode, String newPassword, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
		return null;
	}

	@Override
	public HttpStringRequest getPasswdSendCode(String phone, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
		return null;
	}


}
