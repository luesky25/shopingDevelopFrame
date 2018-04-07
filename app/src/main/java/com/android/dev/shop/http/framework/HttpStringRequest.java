package com.android.dev.shop.http.framework;

import com.android.common.utils.KGLog;
import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.NetworkResponse;
import com.android.common.volley.ParseError;
import com.android.common.volley.Response;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.HttpHeaderParser;
import com.android.common.volley.toolbox.JsonRequest;
import com.android.dev.shop.http.HttpUtils;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HttpStringRequest extends JsonRequest<String> {
	
	private final Listener<String> mListener;
	private static final int SOCKET_TIMEOUT = 15 * 1000;


	public HttpStringRequest(String url, Listener<String> listener,
                             ErrorListener errorListener) {
		this(Method.GET, url, null, listener, errorListener);
	}

	public HttpStringRequest(int method, String url,
                             JSONObject jsonRequest, Listener<String> listener,
                             ErrorListener errorListener) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener, errorListener);
		this.mListener = listener;
        this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}
	public HttpStringRequest(int method, String url,
                             JSONObject jsonRequest, Listener<String> listener,
                             ErrorListener errorListener, int timeout) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener, errorListener);
		this.mListener = listener;
		if (timeout==0){
			timeout = HttpUtils.DEFAULT_SOCKET_TIMEOUT;
		}
        this.setRetryPolicy(new DefaultRetryPolicy(timeout,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}

	public HttpStringRequest(int method, String url,
                             Listener<String> listener, ErrorListener errorListener) {
		this(method, url, null, listener, errorListener);
	}
	public HttpStringRequest(int method, String url,
                             Listener<String> listener, ErrorListener errorListener, int timeOut) {
		this(method, url, null, listener, errorListener, timeOut);
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			KGLog.d("http_volley", "json =" + json);
			return Response.success(json,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
