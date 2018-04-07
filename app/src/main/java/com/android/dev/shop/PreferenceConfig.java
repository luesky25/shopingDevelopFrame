package com.android.dev.shop;

import com.android.dev.basics.DevApplication;

public class PreferenceConfig {
	
	public static final String isTip = "0";//免费短信提示
	/** 非首次启次 **/
	public static final String HAVE_LOGIN = "have_login"+ DevApplication.getMyApplication().getVersionName();


	/** X-AUTH-TOKEN   两个小时有效期**/
	public static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";
	
	public static final String SLASH_AD = "slash_ad";
	
	// 炫铃token是否失效
	public final static String RINGTON_UNC_TOKEN_IS_FAILURE = "RINGTON_UNC_TOKEN_IS_FAILURE";



	/** 登录账号 **/
	public static final String LOGIN_ACOUNT = "login_acount";


	/** 加密后的密码  **/
	public static final String LOGIN_PASSWORD = "login_password";
/** 加密后的密码  **/
	public static final String LOGIN_USER_ID = "login_user_id";


	/** 免登陆，如果为false，则证明应该以游客状态进入主页面  **/
	public static final String AUTO_LOGIN = "auto_login";
	public static final String USER_KEY = "user_key";





	//自动更新后不再提示
	public static String AUTOUPDATE_HAVE_NO_TIP = "autoupdate_have_no_tip";



	//推送开关
	public static String ACTION_PUSH_TRUED = "action_push_trued";


	/**
	 * 信鸽设备token
	 */
	public static String XINGE_PHONE_TOKEN = "xinge_phone_token";



	/**
	 *用户升级之后是否显示弹窗
	 */
	public static String update_app_dialog = "update_app_dialog";
}
