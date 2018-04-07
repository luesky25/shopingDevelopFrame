/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.android.dev.shop.http.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.common.utils.KGLog;
import com.android.common.volley.Request;
import com.android.common.volley.RequestQueue;
import com.android.common.volley.toolbox.ImageLoader;
import com.android.common.volley.toolbox.Volley;

/**
 * 网络请求管理�? * @author Administrator
 *
 */
public class RequestManager {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	public static final String Tag = "sleep";
	
	private RequestManager() {
		// no instances
	}

	public static void init(Context context) {
		//校验context，避免传入非全局Application的上下文导致内存泄露
		if (!(context instanceof Application)) {
			throw new IllegalStateException("Context must instanceof Application");
		}
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        else {
        	request.setTag(Tag);
        }
        KGLog.d("url", request.getUrl());
		if (mRequestQueue!=null) {
			mRequestQueue.add(request);
		}
    }
	
	public static void cancelAll(Object tag) {
		if(mRequestQueue!=null){
			mRequestQueue.cancelAll(tag);
		}
    }
	
	public static void cancelAll() {
        if(mRequestQueue!=null){
			mRequestQueue.cancelAll(Tag);
		}
    }

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
