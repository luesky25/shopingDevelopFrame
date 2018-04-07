package com.android.dev.shop.http.biz;


import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.shop.http.framework.HttpStringRequest;

public interface IHttpUserManager {


	public HttpStringRequest beginLogin(String userName, String password, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg);

	/**
	 * 获取验证码
	 * @param phone
	 * @param listener
	 * @param msg
	 * @return
	 */
	public HttpStringRequest getCheckNum(String phone, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg);


	/**
	 *注册验证码
	 * @param phone  手机号
	 * @param checkCode 收到的验证码
	 * @param password  密码
	 * @return
	 */
	public HttpStringRequest registerUser(String phone, String checkCode, String password,String imageHash,String imageName,String nickname,String user_sex,  HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg);


	/**
	 * 密码修改验证码验证接口passwd_verification
	 * @param phone  手机号
	 * @param checkCode 收到的验证码
	 * @param newPassword  密码
	 * @return
	 */
	public HttpStringRequest resetPassword(String phone, String checkCode, String newPassword, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg);

	/**
	 * 获取密码修改验证码
	 * @param phone
	 * @param listener
	 * @param msg
	 * @return
	 */
	public HttpStringRequest getPasswdSendCode(String phone, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg);
	



}
