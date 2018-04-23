package com.android.dev.shop.android.webview;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;


/**
 * 用于js 回调
 */
public class AndroidJavaScript {  
  
    Context mContext;  
    
  
    public AndroidJavaScript(Context c) {
        this.mContext = c;  
    }

    /**
     * 订购完成后finish
     * 
     * js 调用  window.listner.tofinish();  
     */
    @JavascriptInterface  
    public void tofinish() {
        Activity activity = (Activity) mContext;
        activity.finish();
    }  



   
}  