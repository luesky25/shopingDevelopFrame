package com.android.dev.shop.android.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.android.common.utils.KGLog;
import com.android.dev.shop.R;
import com.android.dev.shop.http.base.RequestManager;


public class ShowLoadingTitleBarFragment extends CommonTitleBarFragment
		implements OnKeyListener {

	private Dialog mShowLaodingDialog;
	private ViewGroup mShowLaodingLayout;
	private TextView mTextOne;
	private TextView mTextTwo;


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		mShowLaodingLayout = (ViewGroup) LayoutInflater.from(mActivity)
				.inflate(R.layout.common_loading_layout, null);
		mTextOne = (TextView) mShowLaodingLayout
				.findViewById(R.id.loading_show_text_one);
		mTextTwo = (TextView) mShowLaodingLayout
				.findViewById(R.id.loading_show_text_two);
		if (mShowLaodingLayout == null) {
			throw new IllegalStateException(
					"the fragment layout must loading layout");
		}
		setOnClickListener(mShowLaodingLayout);
		mShowLaodingDialog = new Dialog(mActivity, R.style.dialogForLoadingStyle);
		mShowLaodingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mShowLaodingDialog.setContentView(mShowLaodingLayout);
		Window dialogWindow = mShowLaodingDialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		mShowLaodingDialog.setOnKeyListener(this);
		mShowLaodingDialog.setCanceledOnTouchOutside(false);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mShowLaodingDialog!=null
				&&mShowLaodingDialog.isShowing()&&mActivity!=null && !mActivity.isFinishing()){
			KGLog.d("ShowLoadingTitleBarFragment", "canceled onDestroy the mShowLaodingDialog!!!");
			// 在onDestory的时候改成直接调用cancel，这个时候调直接用cancel时安全的，这里是为了补救没有在Activity Destory之前正常调用hideLoading可能出现的
			// android.view.WindowLeaked: Activity xxxx has leaked， that was originally added here这样的异常
			mShowLaodingDialog.dismiss();
		}
		mShowLaodingDialog = null;
	}

	private boolean isShowLoading = false;
	private boolean mIsCancelable = false;

	public boolean isShowLoading() {
		return isShowLoading;
	}

	public void cancelLoading() {
		if (isShowLoading) {
			hideLoading();
			onLoadingCancel();
		}
	}

	/**
	 * 后退键取消loading状态回调事件
	 */
	public void onLoadingCancel() {
		RequestManager.cancelAll();
	}


	/**
	 * 显示loading动画
	 */
	public void showLoading(String text, boolean cancelable) {
		if (mShowLaodingDialog != null
				&& mActivity!=null&&!mActivity.isFinishing()) {
			setLoadingTextOne(text);
			mShowLaodingDialog.show();
			isShowLoading = true;
			mIsCancelable = cancelable;
		}
	}
	/**
	 * 显示loading动画
	 */
	public void dialogSetCanceledTouch(boolean cancelable) {
		if (mShowLaodingDialog != null){
			mShowLaodingDialog.setCanceledOnTouchOutside(cancelable);
		}
	}

	
	/**
	 * 隐藏loading动画
	 */
	public void hideLoading() {
		if (mShowLaodingDialog != null
				&& mActivity!=null && !mActivity.isFinishing()) {
			mShowLaodingDialog.cancel();
			isShowLoading = false;
		}
	}

	public void setLoadingTextOne(String str) {
		mTextOne.setText(str);
	}

	public void setLoadingTextTwo(String str) {
		mTextTwo.setText(str);
	}


	@Override
	public void onLeave() {
		super.onLeave();
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (isShowLoading) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (!mIsCancelable ) {
					return true;
				}else{
					hideLoading();
					runOnTabLeft();
				}
			}
		}
		return false;
	}


	
}
