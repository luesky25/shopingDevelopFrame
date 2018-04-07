package com.android.dev.shop.http;

import android.widget.TextView;

import com.android.common.http.utils.PreferencesUtils;
import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.http.base.HttpErrorCode;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
	/**
	 * 默认的http请求重试次数
	 */
	public static final int DEFAULT_MAX_RETRIES = 0;
	
	/**
	 * 默认的http超时时间
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 15 * 1000;

	/**
	 * 这个和retry时候的新的timeout有关
	 */
	public static final float DEFAULT_BACKOFF_MULT = 1;
	
	
	
	public static Map<String, String> getHeaders() {
	   	    HashMap<String, String> headers = new HashMap<String, String>();
	        headers.put("Content-Type", "application/json; charset=utf-8");
	       String release = "release";
			if (KGLog.isDebug()) {
				release = "debug";
			}
//			else{
//				headers.put("X-API-ID", "android/"+ DevApplication.getMyApplication().getVersionName()*//*+"/"+DevApplication.getMyApplication().getChannelID()+"/"+release*//*);
//			}
			//ring头部添加参数来判断是否已经登录,记录各种参数返回给后台
//			headers.put("X-API-ID", "android/"+  DevApplication.getMyApplication().getVersionName()+"/"+DevApplication.getMyApplication().getChannelID()+"/"+release);
//	        headers.put("X-USER-ID", PreferencesUtils.getString(DevApplication.getMyApplication(), PreferenceConfig.LOGIN_USER_ID));
	        headers.put("X-AUTH-TOKEN", PreferencesUtils.getString(DevApplication.getMyApplication(), PreferenceConfig.X_AUTH_TOKEN));
			headers.put("Content-Type", "application/json");
			headers.put("Accept", "*/*");
//
//			headers.put("Authorization", "OEPAUTH realm="+"\"OEP\",netMode=\"WIFI\",version=\"S1.0\"");
	        return headers;
	   }
	
	    // 网络请求失败回调
		public static boolean requestNetError(int code,TextView tv) {
			boolean handled = true;
			switch (code) {
			case HttpErrorCode.ExperationError:
//				EventMessage eventMsg = new EventMessage(EventType.EVENT_SESSION_FAIL_TO_LOGIN);
//				//由于session过期会相互冲突，所以采用错误码作为toast的tag
//				eventMsg.arg1 = HttpErrorCode.ExperationError;
//				KGEventBus.post(eventMsg);
				break;
			default:
			    //在default分支补充处理那些未处理的系统的错误码
                if(tv!=null){
                	String errorDesc = ErrorCodeUtil.getErrorCodeDesc(code);
				    tv.setText(errorDesc);
                }
				break;
			}
			return handled;
		}
}
