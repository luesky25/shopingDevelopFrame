package com.android.dev.shop.android.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


/**
 * 所有界面的基类，一些公共的属性、方法写在这里
 *
 * @author ldx
 */
class BaseActivity extends FragmentActivity  {

	public static final String TITLE_KEY = "title_key";

	public static final String ACTIVITY_INDEX_KEY = "activity_index_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// // 设置全屏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置横屏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// TODO 子类先调用super.onCreate(savedInstanceState)
		//MobclickAgent.onKillProcess(getApplicationContext());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (getParent() != null) {
			return getParent().onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 这个不推荐使用
	 */
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	/**
	 * 启动界面
	 *
	 * @param normal
	 *            true正常启动，false压栈
	 * @param intent
	 */
	protected void startActivity(boolean normal, Intent intent) {
		if (!normal) {
			if (getParent() != null) {
				getParent().startActivity(intent);
			}
		} else {
			super.startActivity(intent);
		}
	}

	/**
	 * 显示提示信息
	 *
	 * @param msg
	 *            提示信息
	 */
	protected void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示提示信息
	 *
	 * @param msg
	 *            提示信息
	 */
	protected void showLongMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		View view = getCurrentFocus();
		if (view != null) {
			manager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//KGRingApplication.getMyApplication().stopMusic();
	}

}
