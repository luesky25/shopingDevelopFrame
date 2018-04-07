
package com.android.dev.shop.http.base;

import com.android.common.volley.AuthFailureError;
import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.JsonObjectRequest;
import com.android.dev.shop.http.HttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * json请求
 * @author Administrator
 *
 */
public class KGJsonRequest extends JsonObjectRequest {

    private Map<String, String> params = new HashMap<String, String>();


    public KGJsonRequest(int method, String url, JSONObject jsonRequest,
                         Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}
    
	public KGJsonRequest(String url, JSONObject jsonRequest,
                         Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));		
	}
	
	public KGJsonRequest(String url,
                         Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, null, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));		
	}

   @Override
   public Map<String, String> getHeaders() throws AuthFailureError {
        return HttpUtils.getHeaders();
   }
    
}
