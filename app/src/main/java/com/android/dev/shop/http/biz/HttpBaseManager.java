
package com.android.dev.shop.http.biz;

import com.android.common.utils.KGLog;
import com.android.common.volley.Request;
import com.android.dev.shop.http.framework.HttpMessage;


import java.util.HashSet;
import java.util.Set;

public class HttpBaseManager {
	
	protected final String mTagStart;
	protected final Set<String> mTagEndList;
	protected final HttpRequestManagerImpl mManager;
	
	public HttpBaseManager(String tag) {
		mTagStart = getCurrentTag(tag);
		mTagEndList = new HashSet<String>();
		mManager = HttpRequestManagerImpl.getInstance();
		KGLog.i("volley_http", "my tag is " + mTagStart);
	}

	private String getCurrentTag(String tag) {
		// 类似com.kk.sleep.http.biz.HttpUserManager@d8e21a2@MainActivity774
		return this.toString()+"@"+tag;
	}
	
	public void addRequest(Request<?> request, HttpMessage msg) {
		String tag = mTagStart + msg.what;
		request.setShouldCache(false);
		mManager.addRequest(request, tag);
		mTagEndList.add(tag);
		
	}
	
	public void cancelAll() {
		for (String end : mTagEndList) {
			mManager.cancelAll(end);
		}
	}
	
	public void cancelTag(int what){
		String currentTag = mTagStart + what;
		String findTag = null;
		for (String end : mTagEndList) {
			if(currentTag.equals(end)){
				findTag = end;
				break;
			}
		}
		if (findTag!=null) {
			mManager.cancelAll(findTag);
		}
	}
	
	public HttpRequestManagerImpl getManager() {
		return mManager;
	}
	
	public void resetManager() {
		HttpRequestManagerImpl.reset();
	}
	
}