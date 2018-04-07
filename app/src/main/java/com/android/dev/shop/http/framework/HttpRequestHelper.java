package com.android.dev.shop.http.framework;

import android.util.Log;

import com.android.common.volley.AuthFailureError;
import com.android.common.volley.Request.Method;
import com.android.common.volley.Response;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.VolleyError;
import com.android.dev.shop.http.base.HttpErrorCode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHelper {
	
	//网络请求类型
	public static final int GET = Method.GET;
	public static final int POST = Method.POST;
	public static final int PUT = Method.PUT;
	public static final int DELETE = Method.DELETE;
	public static final int HEAD = Method.HEAD;
	public static final int OPTIONS = Method.OPTIONS;
	public static final int TRACE = Method.TRACE;
	public static final int PATCH = Method.PATCH;
	//返回状态码
	public static final int DEFAULT_HTTPHELPER_REQUEST_CODE = -1;
	public static final String DEFAULT_JSON_SUCCESS_STR = "success";
	//用户自定义默认头部请求参数(默认为空)
	private static Map<String,String> mDefaultHeader = null;
	private static ICommonHttpResponseDeal mCommonDeal = null;
	private static Gson mGson = new Gson();
	/**
	 * 设置默认的header
	 */
	public static void initDefaultHeader(Map<String, String> defaultMap) {
		mDefaultHeader = defaultMap;
	}
	/**
	 * 设置通用网络回调处理
	 */
	public static void initCommonHttpResponseDeal(ICommonHttpResponseDeal commonDeal) {
		mCommonDeal = commonDeal;
	}
	/**
	 * 网络访问回调接口（公开给用户）
	 */
	public interface IHttpResponseListener<T> {
		public void onHttpSuccess(T data, HttpMessage msg);
		public void onHttpFail(int errorCode, String errorResponse, HttpMessage msg);
	}
	/**
	 * 网络访问回调接口 （公开给业务框架，实现通用逻辑）
	 */
	public interface ICommonHttpResponseDeal {
		public void dealSuccess(Object data, int tag);
		public void dealError(VolleyError error, int errorCode, String errorMessage, int tag);
	}

	public static <T> HttpGsonRequest<T> getRequestUsingGson(final int method,
                                                             final String url, final Map<String, String> head,
                                                             final byte[] body, final IHttpResponseListener<T> responseListener,
                                                             final Class<T> clazz)  {
		return getRequestUsingGson(method, url, head, body, responseListener, clazz,
				new HttpMessage(DEFAULT_HTTPHELPER_REQUEST_CODE));
	}
	/**
	 * 使用volley去发http请求，把结果使用Gson转换为T对应的类型，传给responseListener
	 * @param method 访问的方法
	 * @param url 访问的url
	 * @param head 头的参数列表
	 * @param body body的内容           
	 * @param responseListener 响应处理listener，在http请求成功或者失败的时候会回调
	 * @param clazz 期望接口把返回值转换成的类型参数
	 * @param tag 请求的tag,对应volley框架请求的框架的tag
	 * @param requestCode 请求code，会透传给responseListener的回调函数
	 * @return 对应的http request
	 */
	public static <T> HttpGsonRequest<T> getRequestUsingGson(final int method,
                                                             final String url, final Map<String, String> head,
                                                             final byte[] body, final IHttpResponseListener<T> responseListener,
                                                             final Class<T> clazz, final HttpMessage msg) {
		HttpGsonRequest<T> request = new HttpGsonRequest<T>(method, url, clazz,
				new Listener<T>() {
					@Override
					public void onResponse(T data) {
						if (mCommonDeal != null) {
							mCommonDeal.dealSuccess(data, msg.what);
						}
						if (responseListener != null) {
							responseListener.onHttpSuccess(data, msg);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						final int errorCode = getErrorCode(error);
						final String errorMessage = getErrorMessage(error);
						if (mCommonDeal != null) {
							mCommonDeal.dealError(error, errorCode, errorMessage, msg.what);
						}
						if (responseListener != null) {
							responseListener.onHttpFail(errorCode, errorMessage, msg);
						}
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headMap = new HashMap<String, String>();
				if (mDefaultHeader != null) {	//如果有默认头部，先加载
					headMap.putAll(mDefaultHeader);
				}
				if (head != null) {		//加载用户接口的head参数
					headMap.putAll(head);
				}
				return headMap;
			}
			@Override
			public byte[] getBody() {
				if (body != null) {
					return body;
				} else {
					return super.getBody();
				}
			}
		};
		return request;
	}

	public static HttpJsonRequest getRequestUsingJSON(final int method,
                                                      final String url, final Map<String, String> head,
                                                      final byte[] body,
                                                      final IHttpResponseListener<JSONObject> responseListener) {
		return getRequestUsingJSON(method, url, head, body, responseListener,
				new HttpMessage(DEFAULT_HTTPHELPER_REQUEST_CODE));
	}

	/**
	 * 使用volley去发http请求，把结果使用JSON转换为org.json.JSONObject对应的类型，传给responseListener
	 * @param method 访问的方法
	 * @param url 访问的url
	 * @param paras 参数列表（不需要"dateline"和"token"函数会自动生成）
	 * @param responseListener 响应处理listener，在http请求成功或者失败的时候会回调
	 * @param tag 请求的tag,对应volley框架请求的框架
	 * @param requestCode 请求code，会透传给responseListener的回调函数
	 */
	public static HttpJsonRequest getRequestUsingJSON(final int method,
                                                      final String url, final Map<String, String> head,
                                                      final byte[] body,
                                                      final IHttpResponseListener<JSONObject> responseListener,
                                                      final HttpMessage msg) {
		HttpJsonRequest request = new HttpJsonRequest(method, url,
				new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						if (mCommonDeal != null) {
							mCommonDeal.dealSuccess(data, msg.what);
						}
						if (responseListener != null) {
							responseListener.onHttpSuccess(data, msg);
						}
					}

				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						final int errorCode = getErrorCode(error);
						final String errorMessage = getErrorMessage(error);
						if (mCommonDeal != null) {
							mCommonDeal.dealError(error, errorCode, errorMessage, msg.what);
						}
						if (responseListener != null) {
							responseListener.onHttpFail(errorCode, errorMessage, msg);
						}
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headMap = new HashMap<String, String>();
				if (mDefaultHeader != null) {	//如果有默认头部，先加载
					headMap.putAll(mDefaultHeader);
				}
				if (head != null) {		//加载用户接口的head参数
					headMap.putAll(head);
				}
				return headMap;
			}
			@Override
			public byte[] getBody() {
				if (body != null) {
					return body;
				} else {
					return super.getBody();
				}
			}
		};
		return request;
	}

	private static enum ErrorType {
		AuthFailureError, NetworkError, NoConnectionError, ParseError, TimeoutError, ServerError
	};
	/**
	 * 获取错误码
	 */
	public static int getErrorCode(VolleyError error) {
		String errorResult = error.getClass().getSimpleName();
		int resultCode = -1;
		try {
			switch (ErrorType.valueOf(errorResult)) {
			case AuthFailureError:
				resultCode = HttpErrorCode.AuthFailureError;
				break;
			case NetworkError:
				resultCode = HttpErrorCode.NetworkError;
				break;
			case NoConnectionError:
				resultCode = HttpErrorCode.NoConnectionError;
				break;
			case ParseError:
				resultCode = HttpErrorCode.ParseError;
				break;
			case TimeoutError:
				resultCode = HttpErrorCode.TimeoutError;
				break;
			case ServerError:
				if (error.networkResponse != null) {
					resultCode = error.networkResponse.statusCode;
					if (resultCode == 400) {
						try {
							JSONObject obj = new JSONObject(getErrorMessage(error));
							if (obj.has("resCode")) {
								resultCode = obj.optInt("resCode");
							}
						} catch (Exception e) {}
					}
				}
				else {
					Log.i("volley_http", "错误码解析：networkResponse 数据为null，无法获取状态码");
				}
			}
		} catch (Exception e) {
			Log.i("volley_http", "错误码解析：不明错误");
		};
		return resultCode;
	}


	public static HttpStringRequest getRequestUsingString(final int method,
                                                          final String url, final Map<String, String> head,
                                                          final byte[] body, final IHttpResponseListener<String> listener)  {
		return getRequestUsingString(method, url, head, body, listener, new HttpMessage(DEFAULT_HTTPHELPER_REQUEST_CODE));
	}
	/**
	 * 使用volley去发http请求，把结果使用Gson转换为T对应的类型，传给responseListener
	 * @param method 访问的方法
	 * @param url 访问的url
	 * @param head 头的参数列表
	 * @param body body的内容           
	 * @param responseListener 响应处理listener，在http请求成功或者失败的时候会回调
	 * @param clazz 期望接口把返回值转换成的类型参数
	 * @param tag 请求的tag,对应volley框架请求的框架的tag
	 * @param requestCode 请求code，会透传给responseListener的回调函数
	 * @return 对应的http request
	 */
	public static HttpStringRequest getRequestUsingString(final int method,
                                                          final String url, final Map<String, String> head,
                                                          final byte[] body, final IHttpResponseListener<String> listener, final HttpMessage msg) {
		HttpStringRequest request = new HttpStringRequest(method, url, 
				new Listener<String>() {
					@Override
					public void onResponse(String data) {
						if (mCommonDeal != null) {
							mCommonDeal.dealSuccess(data, msg.what);
						}
						if (listener != null) {
							listener.onHttpSuccess(data, msg);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						final int errorCode = getErrorCode(error);
						final String errorMessage = getErrorMessage(error);
						if (mCommonDeal != null) {
							mCommonDeal.dealError(error, errorCode, errorMessage, msg.what);
						}
						if (listener != null) {
							listener.onHttpFail(errorCode, errorMessage, msg);
						}
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headMap = new HashMap<String, String>();
				if (mDefaultHeader != null) {	//如果有默认头部，先加载
					headMap.putAll(mDefaultHeader);
				}
				
				if (head != null) {		//加载用户接口的head参数
					headMap.putAll(head);
				}
				return headMap;
			}
			@Override
			public byte[] getBody() {
				if (body != null) {
					return body;
				} else {
					return super.getBody();
				}
			}
		};
		return request;
	}



	/**
	 * 使用volley去发http请求，把结果使用Gson转换为T对应的类型，传给responseListener
	 * @param method 访问的方法
	 * @param url 访问的url
	 * @param head 头的参数列表
	 * @param body body的内容
	 * @param responseListener 响应处理listener，在http请求成功或者失败的时候会回调
	 * @param clazz 期望接口把返回值转换成的类型参数
	 * @param tag 请求的tag,对应volley框架请求的框架的tag
	 * @param requestCode 请求code，会透传给responseListener的回调函数
	 * @return 对应的http request
	 */
	public static HttpStringRequest getRequestUsingString(final int method,
                                                          final String url, final Map<String, String> head,
                                                          final byte[] body, final IHttpResponseListener<String> listener, final HttpMessage msg, int timeout) {
		HttpStringRequest request = new HttpStringRequest(method, url,
				new Listener<String>() {
					@Override
					public void onResponse(String data) {
						if (mCommonDeal != null) {
							mCommonDeal.dealSuccess(data, msg.what);
						}
						if (listener != null) {
							listener.onHttpSuccess(data, msg);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				final int errorCode = getErrorCode(error);
				final String errorMessage = getErrorMessage(error);
				if (mCommonDeal != null) {
					mCommonDeal.dealError(error, errorCode, errorMessage, msg.what);
				}
				if (listener != null) {
					listener.onHttpFail(errorCode, errorMessage, msg);
				}
			}
		},timeout) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headMap = new HashMap<String, String>();
				if (mDefaultHeader != null) {	//如果有默认头部，先加载
					headMap.putAll(mDefaultHeader);
				}

				if (head != null) {		//加载用户接口的head参数
					headMap.putAll(head);
				}
				return headMap;
			}
			@Override
			public byte[] getBody() {
				if (body != null) {
					return body;
				} else {
					return super.getBody();
				}
			}
		};
		return request;
	}
	/**
	 * 获取错误信息
	 */
	public static String getErrorMessage(VolleyError error) {
		String result = null;
		if (error.networkResponse != null && error.networkResponse.data != null) {
			try {
				result = new String(error.networkResponse.data, "utf-8");
			}
			catch (Exception e) {
				Log.e("volley_http", "错误信息解析：二进制转换字符串失败");
			}
		}
		return result;
	}
	
	

	/**
	 * 用于获取约定的较为简单的成功返回字符段
	 * 
	 * @param data
	 * @return
	 */
	public static String getSuccessResult(JSONObject data) {
		try {
			data = data.getJSONObject("data");
			return data.getString("result");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isSuccessResult(JSONObject data) {
		String result = getSuccessResult(data);
		if (DEFAULT_JSON_SUCCESS_STR.equals(result)) {
			return true;
		}
		return false;
	}

	public static <T> T fromJson(String json, Class<T> clazz){
		try {
			return mGson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
//			MobclickAgent.reportError(KGRingApplication.getMyApplication().getApplicationContext(),json);
		}
		return null;
	}

	public static <T> T fromJson(String json, Type type) {
		try {
			return mGson.fromJson(json, type);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
//			MobclickAgent.reportError(KGRingApplication.getMyApplication().getApplicationContext(),json);
		}
		return null;
	}	

	/**
	 * 获取JSON返回结果中data字段下面的字段 比如{'code': 0, 'data':{‘max_cost’:
	 * 3.0,},}里面的max_cost
	 * @param data json字符串
	 * @param key 'data'下面的字段的名称
	 * @param T 类型
	 * @return
	 */
	public static <T> T getDataValue(String data, String key, Class T) {
		T result = null;
		JSONObject JSONObj;
		try {
			JSONObj = new JSONObject(data);
			result = getDataValue(JSONObj, key, T);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取JSON返回结果中data字段下面的字段 比如{'code': 0, 'data':{‘max_cost’:
	 * 3.0,},}里面的max_cost
	 * 
	 * @param data
	 *            http JSON访问方式的结果
	 * @param key
	 *            'data'下面的字段的名称
	 * @return 对应的我值
	 */
	public static <T> T getDataValue(JSONObject data, String key, Class T) {
		T result = null;
		if (data == null) {
			return null;
		}
		try {
			JSONObject JSONObjectData;
			JSONObjectData = data.getJSONObject("data");
			if (JSONObjectData != null) {
				if (String.class.isAssignableFrom(T)) {
					result = (T) JSONObjectData.getString(key);
				} else if (Integer.class.isAssignableFrom(T)) {
					result = (T) Integer.valueOf(JSONObjectData.getInt(key));
				} else if (Double.class.isAssignableFrom(T)) {
					result = (T) Double.valueOf(JSONObjectData.getDouble(key));
				} else if (Boolean.class.isAssignableFrom(T)) {
					result = (T) Boolean
							.valueOf(JSONObjectData.getBoolean(key));
				} else if (Long.class.isAssignableFrom(T)) {
					result = (T) Long.valueOf(JSONObjectData.getLong(key));
				} else if (JSONObject.class.isAssignableFrom(T)) {
					result = (T) JSONObjectData.getJSONObject(key);
				} else if (JSONArray.class.isAssignableFrom(T)) {
					result = (T) JSONObjectData.getJSONArray(key);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取{'code': 0, 'data':{‘max_cost’: 里面的code
	 * @param httpResult
	 * @return
	 */
	public static int getResultCode(String httpResult) {
		int result = -1;
		try {
			JSONObject jo = new JSONObject(httpResult);
			result = jo.getInt("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}