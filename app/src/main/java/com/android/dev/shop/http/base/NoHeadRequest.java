
package com.android.dev.shop.http.base;

import com.android.common.volley.AuthFailureError;
import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.StringRequest;
import com.android.dev.shop.http.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class NoHeadRequest extends StringRequest {
    private Map<String, String> params = new HashMap<String, String>();

    public NoHeadRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        super(url, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
    }

    public NoHeadRequest(int method, String url, Listener<String> listener,
                         ErrorListener errorListener) {
        super(method, url, listener, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    /**
     * 添加请求参数
     * 
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        params.put(key, value);
    }
}
