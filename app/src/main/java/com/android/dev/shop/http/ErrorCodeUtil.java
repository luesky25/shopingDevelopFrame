package com.android.dev.shop.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.dev.shop.http.base.HttpErrorCode;
import com.android.dev.shop.http.framework.HttpToast;
import com.android.dev.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ErrorCodeUtil {
	private static final String UNKNOWN_ERROR = "未知错误";
	private static Map<Integer, String> mErrorCodeMap;
	private static Set<Integer> mOtherServerErrorCodeSet;
	static {
		mErrorCodeMap = new HashMap<Integer, String>();
		mErrorCodeMap.put(HttpErrorCode.HTTP_OK, "请求成功");
		mErrorCodeMap.put(HttpErrorCode.BadRequest, "客户端发送错误请求");
		mErrorCodeMap.put(HttpErrorCode.Forbidden, "服务器拒绝客户端请求");
//		mErrorCodeMap.put(HttpErrorCode.NotFound, "服务器无法找到请求的URL");
//		mErrorCodeMap.put(HttpErrorCode.RequestEntityTooLarge, "客户端发送的请求主体长度超过最大上限");
//		mErrorCodeMap.put(HttpErrorCode.UnsupportedMediaType, "服务器无法支持客户端所发实体的内容类型");
////		mErrorCodeMap.put(HttpErrorCode.ServieError, "服务器暂时开小差，请稍后重试");
//		mErrorCodeMap.put(HttpErrorCode.ServieError, "网络异常，请稍后重试");
//		mErrorCodeMap.put(HttpErrorCode.UnkonwError, "系统错误");
//		mErrorCodeMap.put(HttpErrorCode.AuthFailureError, "HTTP的身份验证失败");
//		// Socket关闭，服务器宕机，DNS错误都会产生这个错误
//		mErrorCodeMap.put(HttpErrorCode.NetworkError, "服务器暂时开小差，请稍后重试");
//		mErrorCodeMap.put(HttpErrorCode.NoConnectionError, DevApplication.getInstance().getResources().getString(R.string.ringtone_download_failed));
//		mErrorCodeMap.put(HttpErrorCode.ParseError, "服务器暂时开小差，请稍后重试");
//		mErrorCodeMap.put(HttpErrorCode.SERVERERROR, "服务器暂时开小差，请稍后重试");
//		mErrorCodeMap.put(HttpErrorCode.TimeoutError, "连接超时，请稍后重试");
//
//		mErrorCodeMap.put(HttpErrorCode.InvalidOperatorError, "根据前端的X-Session-id，没权限对当前记录进行操作");
//		mErrorCodeMap.put(HttpErrorCode.ExperationError, "登录信息过期，请重新登录");
//		mErrorCodeMap.put(HttpErrorCode.MustUpdate, "客户端版本过低，请更新至最新版本");
//		mErrorCodeMap.put(HttpErrorCode.OtherError, "服务器暂时开小差，请稍后重试");
//		mErrorCodeMap.put(HttpErrorCode.ParamError, "请求参数异常");
//		mErrorCodeMap.put(HttpErrorCode.CodeError, "验证码错误，校验失败");
//		mErrorCodeMap.put(HttpErrorCode.SendCodeError, "验证码发送失败");
//		mErrorCodeMap.put(HttpErrorCode.AuthError, "用户登录验证失败");
//		mErrorCodeMap.put(HttpErrorCode.AccountExist, "帐号已经存在，不允许重复注册");
//		mErrorCodeMap.put(HttpErrorCode.PasswordError, "登录时的密码错误");
//		mErrorCodeMap.put(HttpErrorCode.NotExistsError, "查询的对象不存在");
//		mErrorCodeMap.put(HttpErrorCode.LoginError, "登录错误");
//		mErrorCodeMap.put(HttpErrorCode.TimeError, "时间错误");
//
//		mOtherServerErrorCodeSet = new HashSet<Integer>();
//		mOtherServerErrorCodeSet.add(HttpErrorCode.ParamError);
//		mOtherServerErrorCodeSet.add(HttpErrorCode.TokenError);
//		mOtherServerErrorCodeSet.add(HttpErrorCode.NotFound);
//		mOtherServerErrorCodeSet.add(HttpErrorCode.ParseError);
//		mOtherServerErrorCodeSet.add(HttpErrorCode.NetworkError);
//		mOtherServerErrorCodeSet.add(HttpErrorCode.ServieError);
	}

	/**
	 * 获取错误提示字符串
	 * 
	 * @param errorCode
	 *            错误码
	 * @return
	 */
	public static String getErrorCodeDesc(int errorCode) {
		String desc = mErrorCodeMap.get(errorCode);
		if (StringUtils.isEmpty(desc)) {
			// 错误码在400 ~ 600之间的未知错误都，显示服务器开小差
			if (errorCode >= HttpErrorCode.BadRequest && errorCode < 600) {
				desc = mErrorCodeMap.get(HttpErrorCode.ServieError);
			} else {
				desc = UNKNOWN_ERROR;
			}
		}
		return desc;
	}

	/**
	 * 获取错误提示字符串
	 * 
	 * @param errorCode
	 *            错误码
	 * @param supplementStr
	 *            错误后面希望补充的字符串，默认已经添加,隔开
	 * @return
	 */
	public static String getErrorCodeDesc(int errorCode, String supplementStr) {
		StringBuffer sb = new StringBuffer(getErrorCodeDesc(errorCode));
		if (!TextUtils.isEmpty(supplementStr)) {
			sb.append("，");
			sb.append(supplementStr);
		}
		return sb.toString();
	}

	/**
	 * 打印错误码对应的toast
	 * 
	 *            上下文
	 * @param errorCode
	 *            错误
	 */
	public static void showErrorCodeDescToast(int errorCode) {
		showErrorToast(errorCode, getErrorCodeDesc(errorCode), false);
	}

	/**
	 * 打印错误码对应的toast
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码
	 * @param supplementStr
	 *            错误描述后面希望补充的字符串，默认已经添加,隔开
	 */
	public static void showErrorCodeDescToast(Context context, int errorCode, String supplementStr) {
		showErrorToast(errorCode, getErrorCodeDesc(errorCode, supplementStr), true);
	}

	/**
	 * 是否是其他的服务器异常
	 * 
	 * @param errorCode
	 *            错误码
	 * @return 是否是其他的服务器异常
	 */
	private static boolean isOtherServerError(int errorCode) {
		boolean isotherServerError = false;
		// 如果在那些码在400(包含)到600之间，则返回true
		if (errorCode >= HttpErrorCode.BadRequest && errorCode < 600) {
			return true;
		}

		if (mOtherServerErrorCodeSet.contains(errorCode)) {
			isotherServerError = true;
		} else {
			isotherServerError = false;
		}

		return isotherServerError;
	}

	// /**
	// * 获取错误码的描述，包含
	// *
	// * @param errorCode
	// * 错误码
	// * @param customHandleMap
	// * 自定义的处理对应错误码的map的描述字符串，如果不为空优先从这个map里面获取错误描述，然后再去获取通用的错误描述
	// * @return 错误描述
	// */
	// public static String getErrorCodeDesc(int errorCode, Map<Integer, String>
	// customHandleMap) {
	//
	// }

	/**
	 * 处理错误的函数(默认showToast+错误描述也使用默认的)
	 * 
	 * @param errorCode
	 *            错误码
	 * @return 错误描述
	 */
	public static String handleErrorCode(int errorCode) {
		return handleErrorCode(errorCode, null, null, true);
	}

	/**
	 * 处理错误的函数(默认showToast+错误描述也使用默认的)
	 * 
	 * @param errorCode
	 *            错误码
	 * @param supplementStr
	 *            在错误码提示后面增加你的话，比如"xxx错误,请稍后重试" ，"xxx错误,信息获取失败" 可以为空(会帮你加“，”)
	 * @return 错误描述
	 */
	public static String handleErrorCode(int errorCode, String supplementStr) {
		return handleErrorCode(errorCode, null, supplementStr, true);
	}

	/**
	 * 处理错误的函数(默认showToast)
	 * 
	 * @param errorCode
	 *            错误码
	 * @param customHandleMap
	 *            自定义的处理对应错误码的map的描述字符串，如果不为空优先从这个map里面获取错误描述，然后再去获取通用的错误描述(可以传空
	 *            )
	 * @param supplementStr
	 *            在错误码提示后面增加你的话，比如"xxx错误,请稍后重试" ，"xxx错误,信息获取失败" 可以为空(会帮你加“，”)
	 * @return 错误描述
	 */
	public static String handleErrorCode(int errorCode, Map<Integer, String> customHandleMap, String supplementStr) {
		return handleErrorCode(errorCode, customHandleMap, supplementStr, true);
	}

	/**
	 * 处理错误的函数(默认showToast)
	 * 
	 * @param errorCode
	 *            错误码
	 * @param customHandleMap
	 *            自定义的处理对应错误码的map的描述字符串，如果不为空优先从这个map里面获取错误描述，然后再去获取通用的错误描述(可以传空
	 *            )
	 * @return 错误描述
	 */
	public static String handleErrorCode(int errorCode, Map<Integer, String> customHandleMap) {
		return handleErrorCode(errorCode, customHandleMap, null, true);
	}
	
	/**
	 * 如果后台有返回错误message，优先使用后台的message文案
	 * @param errorCode
	 * @param errorResponse
	 * @return
	 */
	public static String handleErrorCodeWithErrorString(int errorCode, String errorResponse) {
		Map<Integer, String> map = null;
		String msgText = null;
		try {
			JSONObject jsonObject = new JSONObject(errorResponse);
			msgText = jsonObject.getString("message");
		}
		catch (Exception e){};
		if (!TextUtils.isEmpty(msgText)) {
			map = new HashMap<Integer, String>();
			map.put(errorCode, msgText);
		}
		return handleErrorCode(errorCode, map);
	}

	/**
	 * 处理错误的函数
	 * 
	 * @param errorCode
	 *            错误码
	 * @param customHandleMap
	 *            自定义的处理对应错误码的map的描述字符串，如果不为空优先从这个map里面获取错误描述，然后再去获取通用的错误描述(可以传空
	 *            )
	 * @param supplementStr
	 *            在错误码提示后面增加你的话，比如"xxx错误,请稍后重试" ，"xxx错误,信息获取失败" 可以为空(会帮你加“，”)
	 * @param isShowToast
	 *            是否showToast
	 * @return 错误描述
	 */
	public static String handleErrorCode(int errorCode, Map<Integer, String> customHandleMap, String supplementStr,
			boolean isShowToast) {
		String errorCodeDesc = null;
		boolean isCostomErrorMsg = false;
		if (customHandleMap != null) {
			errorCodeDesc = customHandleMap.get(errorCode);
		}

		if (errorCodeDesc == null) {
			errorCodeDesc = mErrorCodeMap.get(errorCode);
			if (errorCodeDesc == null) {
				// 错误码在400 ~ 600之间的未知错误都，显示服务器开小差
				if (isOtherServerError(errorCode)) {
					errorCodeDesc = mErrorCodeMap.get(HttpErrorCode.ServieError);
				} else {
					errorCodeDesc = UNKNOWN_ERROR;
				}
			}
		} else {
			isCostomErrorMsg = true;
		}

		if (!isCostomErrorMsg && supplementStr != null) {
			errorCodeDesc = errorCodeDesc + "，" + supplementStr;
		}

		if (isShowToast) {
			showErrorToast(errorCode, errorCodeDesc, isCostomErrorMsg);
		}
		return errorCodeDesc;
	}

	/**
	 * 显示errorCode的Toast，相同的错误码在2000ms之内不会重复显示
	 * 
	 * @param errorCode
	 *            错误码
	 * @param errorCodeDesc
	 *            错误描述
	 * @param isCostomMsg
	 *            是否是自定义的消息
	 */
	private static void showErrorToast(int errorCode, String errorCodeDesc, boolean isCostomMsg) {
		// 增加HTTP_OK——"请求成功"不显示，避免HTTP_OK+但是不是"success"的情况出现,然后打印一个"请求成功",这种情况需要自己
		if(HttpErrorCode.HTTP_OK != errorCode){
		    HttpToast.showToast(errorCode, errorCodeDesc);
		}
	}
}
