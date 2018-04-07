package com.android.dev.shop.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MotionEvent;


import com.android.common.utils.KGLog;
import com.android.dev.framework.component.base.BaseWorkerFragmentActivity;
import com.android.dev.shop.R;
import com.android.dev.shop.android.utils.FragmentUtils;
import com.android.dev.shop.http.base.RequestManager;
import com.android.dev.utils.ExceptionUtil;
import com.android.dev.utils.ScreenUtils;
import com.android.dev.utils.StringUtils;

import java.util.List;

public class BaseWorkerShowFragmentActivity extends BaseWorkerFragmentActivity {
	
	private static final String tag = BaseWorkerShowFragmentActivity.class.getSimpleName();
	private FragmentManager mManager;
	
	protected boolean needAotohideKey = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_activity_layout);
		KGLog.i("net168", "onCreata " + this.getClass().getSimpleName());
	}
	
	@Override
	protected void handleBackgroundMessage(Message msg) {}
	
	/**
	 * 返回可用层级
	 * @return
	 */
	public int getFramentLayout() {
		if (mManager == null)
			mManager = getSupportFragmentManager();
		//如果是初始化则返回第一个布局
		if (mManager.getFragments() == null || mManager.getFragments().size() == 0) {
			KGLog.i(tag, "现在加载初始化布局");
			return R.id.common_fisrt_layout_root;
		}
		//非初始化，返回后面的布局
		int count = mManager.getBackStackEntryCount();
		int layoutRes = 0;
		switch (count) {
		case 0:
			layoutRes = R.id.common_second_layout_root;
			break;
		case 1:
			layoutRes = R.id.common_third_layout_root;
			break;
		case 2:
			layoutRes = R.id.common_four_layout_root;
			break;
		default:
			layoutRes = android.R.id.content;
			ExceptionUtil.throwsException("当前activity最多只能加载4层fragment，当前已经超出限制或异常, BackStackEntryCount = " + count);
		}
		KGLog.i(tag, "现在加载的是第" + (count + 1) + "层布局");
		return layoutRes;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if (fragments == null)
			return;
		for (Fragment f : fragments) {
			if (f != null)
				f.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				List<Fragment> fragments = getSupportFragmentManager().getFragments();
				if (fragments != null && fragments.size() >= 1) {
					Fragment item = null;
					for (int i = fragments.size() - 1; i >= 0; i--) {
						item = fragments.get(i);
						
//						if (item != null && item instanceof CommonTitleBarFragment)//等公共标题fragment 写好 再加入
//							break;
					}
					//如果fragment存在loading状态，取消loading不退栈，回调loading状态的fragment的onLoadingCancel()方法
//					先不放开该功能
//					if (item != null && item instanceof ShowLoadingTitleBarFragment) {
//						ShowLoadingTitleBarFragment f = (ShowLoadingTitleBarFragment) item;
//						if (f.isShowLoading()) {
//							f.cancelLoading();
//							return true;
//						}
//					}
					
					//如果fragment都不在loading状态，则是退栈操作
//					if (item != null && item instanceof CommonTitleBarFragment)
//						((CommonTitleBarFragment)item).runOnTabLeft();
//					else {
						if (getSupportFragmentManager().getBackStackEntryCount() != 0)
							FragmentUtils.popBackStackAndNotify(getSupportFragmentManager());
						else
							finish();
					}
//				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 自动隐藏软键盘输入框
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(needAotohideKey){
			ScreenUtils.hideKeyboard(this, ev);
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		KGLog.d("BaseWorkerShowFragmentActivity",
				"BaseWorkerShowFragmentActivity cancelAll for this activity "
						+ StringUtils.getSimpleName(this));
		RequestManager.cancelAll(StringUtils.getSimpleName(this));
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	
	
}
