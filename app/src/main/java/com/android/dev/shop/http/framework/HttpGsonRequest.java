package com.android.dev.shop.http.framework;

import android.util.Log;

import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.NetworkResponse;
import com.android.common.volley.ParseError;
import com.android.common.volley.Response;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.HttpHeaderParser;
import com.android.common.volley.toolbox.JsonRequest;
import com.android.dev.shop.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HttpGsonRequest<T> extends JsonRequest<T> {
	
	private final Gson mGson = new Gson();
	private final Class<T> mClazz;
	private final Listener<T> mListener;

	public HttpGsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, clazz,null, listener, errorListener);
	}

	public HttpGsonRequest(int method, String url, Class<T> clazz, JSONObject jsonRequest, Listener<T> listener, ErrorListener errorListener) {
		
		super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
		this.mClazz = clazz;
		this.mListener = listener;
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}
	
	public HttpGsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		this(method, url, clazz,null,listener, errorListener);
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
	
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.d("http_volley", "json =" + json);
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
