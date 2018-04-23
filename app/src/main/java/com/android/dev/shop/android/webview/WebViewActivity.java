package com.android.dev.shop.android.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;
import com.android.dev.framework.component.base.BaseFragmentActivity;
import com.android.dev.shop.R;


/**
 * 内置浏览网页
 * 
 * @author levinlee
 * 
 */
public class WebViewActivity extends BaseFragmentActivity implements
		OnClickListener {
	/** webview 浏览器 */
	private WebView webView;
	/** url路径 */
	private String url;
	private String title;
	/** title栏文字 */
	private TextView textView;

	private ImageView bakcImageView;
	
	private ProgressBar progressbar;

	public static final String KEY_URL = "key_url";
	public static final String KEY_TITLE = "key_title";
	public static final String KEY_ISWECOME = "isWecome";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_base_webview);
		LoadIntentInfo();
		findWidget();
		initWidget();
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		LoadIntentInfo();
		initWidget();
	}

//    @Override
//    public String getUmengPageName() {
//		return StringUtils.getSimpleName(this) + "[" + title + "]";
//	}

	private void LoadIntentInfo() {
		Intent intent = getIntent();
		url = intent.getStringExtra(KEY_URL);
		title = intent.getStringExtra(KEY_TITLE);
	}

	private void findWidget() {
		webView = (WebView) findViewById(R.id.baseWebView);
		textView = (TextView) findViewById(R.id.common_title_tv);
		bakcImageView = (ImageView) findViewById(R.id.common_left_iv);
		progressbar = (ProgressBar) findViewById(R.id.webview_ProgressBar);
	}

	private void initWidget() {
//		bakcImageView.setImageResource(R.drawable.close_btn_selector);
		bakcImageView.setOnClickListener(this);
		// 创建WebChromeClient对象，获取当前页面的标题
		WebChromeClient webChromeClient = new WebChromeClient() {
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressbar.setVisibility(View.GONE);
				} else {
					if (progressbar.getVisibility() == View.GONE)
						progressbar.setVisibility(View.VISIBLE);
					progressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
			
			@Override
			public void onReceivedTitle(WebView view, String mTitle) {
				super.onReceivedTitle(view, title);
				//textView.setText(title);
				if (!TextUtils.isEmpty(title))
					textView.setText(title);
				else
					textView.setText(mTitle);
			}
		};
		// 创建WebViewClient对象
		WebViewClient webViewClient = new WebViewClient() {
			
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				KGLog.d("debug","url---===>"+url);
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
//				if (url.contains("/h5/index.html#ordertip")) {
//					finish();
//					return true;
//				}
				view.loadUrl(url);
				// 消耗掉这个事件
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				KGLog.d("debug","url---===>"+url);
				if (!TextUtils.isEmpty(title))
					textView.setText(title);
				else
					textView.setText(view.getTitle());
			}
			
		};
		webView.getSettings().setDomStorageEnabled(true);
		webView.addJavascriptInterface(new AndroidJavaScript(this), "listner");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setDownloadListener(new WebViewDownLoadListener());
		webView.setWebViewClient(webViewClient);
		webView.setWebChromeClient(webChromeClient);
		//WEB端统计分析，需要将APP的 user-agent 作特征标记，ndroid对webview的User-Agent设置方法
		webView.getSettings().setUserAgentString("Duanshi,Mobile,Android,"+"versionName="+DevApplication.getMyApplication().getVersionName());
		webView.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_left_iv:
			if(webView!=null && webView.canGoBack()){
				webView.goBack();
			}else{
				finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();// 返回前一个页面
			return true;
		}else{
				finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private class WebViewDownLoadListener implements DownloadListener {  
		  
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
									long contentLength) {  
            Uri uri = Uri.parse(url);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent);  
        }  
  
    }

	@Override
	protected void onDestroy() {
		if (webView != null) {
			webView.removeAllViews();
			webView.stopLoading();
			webView.destroy();
			webView = null;
		}
		super.onDestroy();
	}
}
