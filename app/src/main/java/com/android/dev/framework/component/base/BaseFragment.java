package com.android.dev.framework.component.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 描述:所有Fragment的父类,提供刷新UI的Handler
 *
 * @author chenys
 * @since 2013-7-29 上午11:03:47
 */
public class BaseFragment extends Fragment {

//	protected View mView;

	protected Activity mActivity;

	private boolean mIsFirstLoad;
	/*
	 *  setArguments/getArguments只能使用一次，一旦添加到 FragmentManager后，
	 *  如果再次使用，将导致java.lang.IllegalStateException: Fragment already active 异常
	 *  以下代码，因为在项目中未真正起作用，所以暂时未作处理
	 */
	public void setShownIndex(int index) {
		Bundle args = new Bundle();
		args.putInt("index", index);
		setArguments(args);

	}

	public int getShownIndex() {
		return getArguments() == null ? 0 : getArguments().getInt("index", 0);
	}

	private Toast mToast;

	protected Handler mUiHandler;

	@SuppressLint("HandlerLeak")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mIsFirstLoad = true;
		initData();
		setListener();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// MobclickAgent.onError(getActivity());
//		mView = getView();
		mActivity = getActivity();
		mUiHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (isAdded()) {
					super.handleMessage(msg);
					handleUiMessage(msg);
				}
			};
		};
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findView(view);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 处理更新UI任务
	 *
	 * @param msg
	 */
	protected void handleUiMessage(Message msg) {
	}

	/**
	 * 发送UI更新操作
	 *
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	/**
	 * 发送UI更新操作
	 *
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	/**
	 * 显示一个Toast类型的消息
	 *
	 * @param msg
	 *            显示的消息
	 */
	public void showToast(final String msg) {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(getActivity(), "",
							Toast.LENGTH_SHORT);
				}
				mToast.setText(msg);
				mToast.show();
			}
		});

	}

	/**
	 * 显示{@link Toast}通知
	 *
	 * @param strResId
	 *            字符串资源id
	 */
	public void showToast(int strResId) {
		if (getActivity() == null) {
			return;
		}
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
		}
		mToast.setText(strResId);
		mToast.show();
	}
	
	
	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		View view = getActivity().getCurrentFocus();
		if (view != null) {
			manager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	protected void findView(View v) {
	}

	protected void initData() {
	}

	protected void setListener() {
	}



	@Override
	public void onStop() {
		super.onStop();
		mIsFirstLoad = false;
	}

	@Override
	public void onStart() {
		if (!mIsFirstLoad)
			onReStart();
		super.onStart();
	}

	/**
	 * 该新添加生命周期可用于fragment返回时进入刷新处理操作
	 */
	public void onReStart() {
	}



	/**
	 * 延时发送UI更新操作，delayMillis为延时发送
	 */
	public void sendEmptyUiMessageDelayed(int what, long delayMillis) {
		mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	/**
	 * 移除消息队列对应的消息
	 */
	public void removeUiMessages(int what) {
		mUiHandler.removeMessages(what);
	}

	/**
	 * 从消息池获取一个msg实例
	 */
	public Message obtainUiMessage() {
		return mUiHandler.obtainMessage();
	}


	
	// 回调刷新fragment
	public void onReload() {
	}

	// 离开当前fragment
	public void onLeave() {
	}


}
