package com.android.dev.shop.android.base;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;
import com.android.dev.framework.component.controller.FragmentUserVisibleController;
import com.android.dev.shop.R;
import com.android.dev.shop.android.utils.FragmentUtils;
import com.android.dev.shop.http.biz.HttpManagerFactory;


public class CommonTitleBarFragment extends BaseWorkerOnClickFragment implements FragmentUserVisibleController.UserVisibleCallback{

	private HttpManagerFactory mFactory;
	
	private static final String TAG = CommonTitleBarFragment.class.getSimpleName();
	protected ImageView mLeftImageView;
	protected TextView mTitleTextView;
	protected TextView mRightImageView;
//	protected RelativeLayout mTitleRl;
	protected View mLine;

	private boolean isGeust;
//	private User.UserInfo userInfo;
	
	protected static final int FIRST_LOAD_SIZE = 20;
	protected static final int MORE_LOAD_SIZE = 10;

	private FragmentUserVisibleController userVisibleController;
	
	protected void setTitleContent(int resId){
		if(mTitleTextView!=null){
			mTitleTextView.setText(getString(resId));
		}else{
			KGLog.e(TAG, "the mTitleTextView is null,the method must call after onActivityCreated() method");
		}
	}
	
	protected void setTitleContent(String title){
		if(mTitleTextView!=null){
			mTitleTextView.setText(title);
		}else{
			KGLog.e(TAG, "the mTitleTextView is null,the method must call after onActivityCreated() method");
		}
	}

	public CommonTitleBarFragment() {
		userVisibleController = new FragmentUserVisibleController(this, this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFactory = new HttpManagerFactory(this);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initTitleBar(getView());
		super.onActivityCreated(savedInstanceState);
		userVisibleController.activityCreated();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mFactory.cancelAll();
	}
	
	public HttpManagerFactory getVolleyFactory() {
		return mFactory;
	}
	
	private void initTitleBar(View v){
		mLeftImageView = (ImageView) v.findViewById(R.id.common_left_iv);
		mTitleTextView = (TextView) v.findViewById(R.id.common_title_tv);
		mRightImageView = (TextView) v.findViewById(R.id.common_right_iv);
//		mTitleRl = (RelativeLayout) v.findViewById(R.id.common_title_bar_rl);
		mLine = (View) v.findViewById(R.id.common_title_line);
		if (mLeftImageView == null || mTitleTextView == null || mRightImageView == null) {
			KGLog.e(TAG, "the fragment must include the common_title_bar layout in the content view");
		}
//		if(mTitleRl!=null){
//			DoubleClickUtils.registerDoubleClickListener(mTitleRl,this);
//		}
		setOnClickListenerSingle(mLeftImageView);
		setOnClickListenerSingle(mRightImageView);
		reSetRightImage(mRightImageView);
		initUserDate();
	}

	protected void initUserDate(){
		isGeust = DevApplication.getMyApplication().isGuest();
//		userInfo = DevApplication.getMyApplication().getUserData();
	}
	
	@Override
	public void OnClickSingle(View v) {
		switch (v.getId()) {
		case R.id.common_left_iv:
			onTabLeftClick(v);
			break;
		case R.id.common_right_iv:
			onTabRightClick(v);
			break;
		}
	}


	/**
	 * 用户交互提示
	 */
	public void showInform(int type) {}
	
	/**
	 * activity回调函数接口
	 */
	public void onResultFormActivity(int type, Object data) {}
	
	/**
	 * 监控设置右侧按钮监听事件
	 */
	protected void onTabRightClick(View v) {}
	/**
	 * 监控设置左侧按钮监听事件
	 */
	protected void onTabLeftClick(View v) {
		if (getFragmentManager().getBackStackEntryCount() != 0)
			FragmentUtils.popBackStackAndNotify(getFragmentManager());
		else {
			onLeave();
			if (mActivity!=null) {
				mActivity.finish();
			}
		}
	}
	public void runOnTabLeft() {
		onTabLeftClick(mLeftImageView);
	}
	/**
	 * 设置右侧按钮图标
	 * @param res
	 */
	public void setTabRightBtnDrawable(int res) {
		mRightImageView.setBackgroundResource(res);
		showRightBtn(true);
	}
	/**
	 * 设置右侧的文字
	 * @param res
	 */
	public void setTabRightBtnText(int res) {
		mRightImageView.setText(res);
		showRightBtn(true);
	}
	/**
	 * 设置右侧的文字大池
	 * @param res
	 */
	public void setTabRightBtnTextSize(int res) {
		mRightImageView.setTextSize(res);
	}
		/**
	 * 设置右侧的文字颜色
	 * @param res
	 */
	public void setTabRightBtnTextColor(int res) {
		mRightImageView.setTextColor(res);
	}
	
	public void clickLeftBtn() {
		mLeftImageView.performClick();
	}
	
	/**
	 * 设置左侧按钮图标
	 * @param res
	 */
	public void setTabLeftBtnDrawable(int res) {
		mLeftImageView.setImageResource(res);
		showLeftBtn(true);
	}
	/**
	 * 设置左侧按钮可见并且可用
	 */
	public void showLeftBtn(boolean show) {
		if (mLeftImageView!=null) {
			if (show) {
				mLeftImageView.setVisibility(View.VISIBLE);
				mLeftImageView.setClickable(true);
			} else {
				mLeftImageView.setVisibility(View.GONE);
				mLeftImageView.setClickable(false);
			}
		}
	}
	/**
	 * 设置右侧按钮可见并且可用
	 */
	public void showRightBtn(boolean show) {
		if (show) {
			mRightImageView.setVisibility(View.VISIBLE);
			mRightImageView.setClickable(true);
		}
		else {
			mRightImageView.setVisibility(View.GONE);
			mRightImageView.setClickable(false);
		}
	}
	
	public void reSetRightImage(TextView tv) {
		mRightImageView.setVisibility(View.VISIBLE);
		mRightImageView.setClickable(true);
	}
//	public void setTitleRLVisible(boolean isVisible) {
//		if(mTitleRl!=null){
//			if (isVisible)
//				mTitleRl.setVisibility(View.VISIBLE);
//			else
//				mTitleRl.setVisibility(View.GONE);
//		}
//		
//	}	
	public void setTitleLineVisible(boolean isVisible) {
		if (isVisible)
			mLine.setVisibility(View.VISIBLE);
		else
			mLine.setVisibility(View.GONE);
	}
	
	@Override
	protected void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
	

//
//	@Override
//	public void OnSingleClick(View v) {
//
//	}

//	@Override
//	public void OnDoubleClick(View v) {
//		switch (v.getId()){
//			case R.id.common_title_bar:
//				onTitleDoubleClick();
//			break;
//		}
//	}

	public void onTitleDoubleClick() {
	}

	public TextView getmRightImageView() {
		return mRightImageView;
	}

	@Override
	public void onResume() {
		super.onResume();

		userVisibleController.resume();
	}


	@Override
	public void onPause() {
		super.onPause();

		userVisibleController.pause();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		userVisibleController.setUserVisibleHint(isVisibleToUser);
	}


	@Override
	public boolean isWaitingShowToUser() {
		return userVisibleController.isWaitingShowToUser();
	}

	@Override
	public void setWaitingShowToUser(boolean waitingShowToUser) {
		userVisibleController.setWaitingShowToUser(waitingShowToUser);
	}

	@Override
	public boolean isVisibleToUser() {
		return userVisibleController.isVisibleToUser();
	}

	@Override
	public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	/**
	 * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
	 * @param isVisibleToUser true：用户能看见当前Fragment；false：用户看不见当前Fragment
	 * @param invokeInResumeOrPause true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
	 */
	@Override
	public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
		if(isVisibleToUser){
//			UmengDataReportUtil.onPageStart(mActivity, this.getClass().getName());
		}else{
//			UmengDataReportUtil.onPageEnd(mActivity, this.getClass().getName());
		}
	}
}
