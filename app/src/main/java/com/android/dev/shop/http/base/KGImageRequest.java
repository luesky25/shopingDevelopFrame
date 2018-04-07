package com.android.dev.shop.http.base;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.Response.ErrorListener;
import com.android.common.volley.Response.Listener;
import com.android.common.volley.toolbox.ImageRequest;
import com.android.dev.shop.http.HttpUtils;

/**
 * @author levinlee
 */
public class KGImageRequest extends ImageRequest {
	

	public KGImageRequest(String url, Listener<Bitmap> listener, int maxWidth,
                          int maxHeight, Config decodeConfig, ErrorListener errorListener) {
		super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
		this.setRetryPolicy(new DefaultRetryPolicy(HttpUtils.DEFAULT_SOCKET_TIMEOUT,
				HttpUtils.DEFAULT_MAX_RETRIES,
				HttpUtils.DEFAULT_BACKOFF_MULT));
	}

}
