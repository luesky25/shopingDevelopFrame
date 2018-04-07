package com.android.dev.shop.http.base;

public class HttpErrorCode {
	
	public static final int HTTP_OK = 200; //请求成功
	public static final int BadRequest = 400;	//客户端发送错误请求。具体原因参考表2的错误码
	public static final int Forbidden = 403;	//服务器拒绝客户端请求。当前端HTTP头部的"X-Session-ID"过期，或者不合法的时候，后台会返回该状态码
	public static final int NotFound = 404;	//服务器无法找到请求的URL。
	public static final int RequestEntityTooLarge = 405;	//客户端发送的请求主体长度超过最大上限。
	public static final int UnsupportedMediaType = 415;	//服务器无法支持客户端所发实体的内容类型。比如：前端请求Content-Type应该为json，却填写了其它的内容
	
	public static final int ServieError = 500;	//服务器错误
	public static final int UnkonwError = -1;	//不明错误
	public static final int AuthFailureError = 1;	//如果在做一个HTTP的身份验证，可能会发生这个错误
	public static final int NetworkError = 2;	//Socket关闭，服务器宕机，DNS错误都会产生这个错误
	public static final int NoConnectionError = 3;	//客户端没有网络连接
	public static final int ParseError = 4;	//在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常
	public static final int SERVERERROR = 5;	//服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码
	public static final int TimeoutError = 6;	//Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy


	public static final String RESULT_OK = "000000"; // 成功
	public static final int InvalidOperatorError = 10100;	//根据前端的X-Session-id，没权限对当前记录进行操作
	public static final int ExperationError = 1;	//该会话已经过期
	public static final int MustUpdate = 10102;	//客户端版本过低，需要强制升级
	public static final int OtherError = 10103;	//客户端收到该类型的错误后，把"message"的内容提示给用户
	public static final int ParamError = 10000;	//前端输入的参数类型有误，例如应该输入int型，但是实际输入字符串类型
	public static final int CodeError = 10001;	//验证码错误，校验失败
	public static final int SendCodeError = 10002;	//验证码发送失败
	public static final int AuthError = 10003;	//用户登录验证失败
	public static final int AccountExist = 10004;	//帐号已经存在，不允许重复注册
	public static final int PasswordError = 10005;	//登录时的密码错误
	public static final int NotExistsError = 10006;	//查询的对象不存在
	public static final int LoginError = 10007;	//登录错误
	public static final int TimeError = 10008;	//时间错误
	public static final int MaxRestrictionError = 10009;	//最大限制错误
	public static final int InMyBlackError = 10010;	//拉黑错误
	public static final int TokenError = 10011;	//Token验证错误
	public static final int TokenExpire = 10012;	//Token验证超时
	public static final String CMM_MOHT_MESSAGE = "P1000";	//移动开通包月需要二次短信确认
	public static final String cmmLoginReport = "200001";	//移动开通包月需要二次短信确认
	public static final String CTMTOKENPASS1 = "630001";	//电信toekn过期1
	public static final String CTMTOKENPASS2 = "630002";	//电信toekn过期2
	public static final String THIRD_UNREGISTER = "100001";	//第三方未注册的错误码

} 
