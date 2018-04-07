
package com.android.dev.shop.http.framework;

import android.util.Log;

import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.NetworkResponse;
import com.android.common.volley.Response;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.HttpHeaderParser;
import com.android.common.volley.toolbox.JsonObjectRequest;
import com.android.dev.shop.http.HttpUtils;

import org.json.JSONObject;

public class HttpJsonRequest extends JsonObjectRequest {

    public HttpJsonRequest(int method, String url, JSONObject jsonRequest,
                           Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}
    
	public HttpJsonRequest(String url, JSONObject jsonRequest,
                           Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));		
	}
	
	public HttpJsonRequest(String url,
                           Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, null, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));		
	}
	
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.d("http_volley", "json =" + json);
		} catch(Exception e) {
			
		}
		return super.parseNetworkResponse(response);
	}
}
