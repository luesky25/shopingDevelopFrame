package com.android.dev.shop.http.framework;

import android.widget.Toast;

import com.android.dev.basics.DevApplication;

/**
 * 统一管理网络Toast类，保证短时间内相同调用仅仅会响应一次Toast(先调则响应，后调则忽略) 
 * @author haitaoliu
 */
public class HttpToast {
	
	public static long lastTime = 0;
	public static int lastTag = 0;
	
	/**
	 * 如果相同网络请求，多次调用该函数，应该只会影响首次调用
	 * @return 是否会弹出toast
	 */
	public static final boolean showToast(int tag, String text) {
		boolean success = false;
		if (Math.abs(System.currentTimeMillis() - lastTime) > 500 || lastTag != tag) {
			Toast.makeText(DevApplication.getMyApplication(), text, Toast.LENGTH_SHORT).show();
			success = true;
			lastTime = System.currentTimeMillis();
			lastTag = tag;
		}
		return success;
	}
	
}
