package com.android.dev.shop.http.base;


import com.android.common.utils.KGLog;
import com.android.common.volley.AuthFailureError;
import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.NetworkResponse;
import com.android.common.volley.ParseError;
import com.android.common.volley.Request;
import com.android.common.volley.Response;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.HttpHeaderParser;
import com.android.common.volley.toolbox.JsonRequest;
import com.android.dev.shop.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * json
 * @author Administrator
 *
 * @param <T>
 */
public class GsonRequest<T> extends JsonRequest<T> {
	private final Gson mGson = new Gson();
	private final Class<T> mClazz;
	private final Response.Listener<T> mListener;
	private static final int SOCKET_TIMEOUT = 15*1000;
	//private final Map<String, String> mHeaders;

	public GsonRequest(String url, Class<T> clazz,Listener<T> listener, Response.ErrorListener errorListener) {
		this(Request.Method.GET, url, clazz,null, listener, errorListener);
	}

	public GsonRequest(int method, String url, Class<T> clazz, JSONObject jsonRequest, Listener<T> listener, Response.ErrorListener errorListener) {
		
		//super(method, url, errorListener);
		super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
		this.mClazz = clazz;
		//this.mHeaders = headers;
		this.mListener = listener;
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}
	
	public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, Response.ErrorListener errorListener) {
		this(method, url, clazz,null,listener, errorListener);
	}

	/**
     * 设置请求�?
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	return HttpUtils.getHeaders();
    }

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
	
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			/*for(Map.Entry<String, String> entry:response.headers.entrySet()){ 
				Log.d("levintest",entry.getKey()+"--->"+entry.getValue()); 
			} */
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			KGLog.d("httpresult", "json =" +json);
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			KGLog.e("httpresult", "UnsupportedEncodingException");
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			KGLog.e("httpresult", "JsonSyntaxException :" +e.getMessage());
			return Response.error(new ParseError(e));
		}
	}
}
