package com.android.dev.shop.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.dev.shop.android.base.BaseWorkerShowFragmentActivity;
import com.android.dev.shop.android.login.LoginFragment;
import com.android.dev.shop.android.login.RegisterPasswordDateFragment;
import com.android.dev.shop.android.login.RegisterPasswordFragment;
import com.android.dev.shop.android.utils.FragmentUtils;

import java.util.List;


/**
 * 集成登录验证以及注册验证的模块
 * 
 * @author MichelleZhao
 */
public class CheckActivity extends BaseWorkerShowFragmentActivity {

	private static final String IS_ACTIVITY_RESTART = "is_activity_resart";
	private FragmentManager mManager;
	private boolean mIsActiivtyRestarted;
	LoginFragment mLoginFragment;
	private  int isLogin; //0---登录；1---忘记密码 2---修改密码 3---去绑定手机号

	public static final String LOGIN_INFO="song_info";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 对于这一个activity所谓的恢复就是重新初始化
		if (savedInstanceState != null) {
			mIsActiivtyRestarted = savedInstanceState.getBoolean(
					IS_ACTIVITY_RESTART, false);
			savedInstanceState.putBoolean(IS_ACTIVITY_RESTART, false);
		}
		Bundle b = getIntent().getExtras();
		isLogin =  b.getInt(LOGIN_INFO);
		mManager = getSupportFragmentManager();

		if (isLogin!=0){
//			if (isLogin==3){
//				Fragment fragment = ThirdRegisterPhoneFragment.getInstance(null,isLogin);
//				FragmentUtils.replaceFragment(mManager, getFramentLayout(),fragment);
//
//			}else {
//				RegisterCheckPhoneFragment fragments = (RegisterCheckPhoneFragment) mManager.findFragmentByTag(RegisterCheckPhoneFragment.class.getSimpleName());
//				if (fragments == null) {
//					fragments = RegisterCheckPhoneFragment.getInstance(isLogin);
//				}
//				FragmentUtils.replaceFragment(mManager, getFramentLayout(), fragments);
//			}
		}
		else {
			initData();
		}

	}


	protected void initData() {
		
		mLoginFragment = (LoginFragment) mManager.findFragmentByTag(LoginFragment.class.getSimpleName());
		// 默认加载登录界面
		if(mLoginFragment==null){
		    addLoginFragment(false);
		}
	}

	/**
	 * 加载登录界面
	 */
	public void addLoginFragment(boolean addStack) {
		mLoginFragment= LoginFragment.getInstance();
		if (addStack)
			FragmentUtils.replaceFragmentWithStack(mManager,
					getFramentLayout(), mLoginFragment);
		else
			FragmentUtils.replaceFragment(mManager, getFramentLayout(),
					mLoginFragment);
	}

	/**
	 * 加载手机验证界面
	 */
	public void addCheckPhoneFragment(int type) {
//		Fragment fragment = RegisterCheckPhoneFragment.getInstance(type);
//		FragmentUtils.replaceFragmentWithStack(mManager, getFramentLayout(),
//				fragment);
	}

	/**
	 * 加载输入密码的界面
	 */
	public void addSetDataFragment(String phone,int type) {
		Fragment fragment = RegisterPasswordFragment.getInstance(phone,type);
		FragmentUtils.replaceFragmentWithStack(mManager, getFramentLayout(),
				fragment);
	}	
	/**
	 * 加载输入注册的界面
	 */
	public void addRegisterFragment(String phone) {
		Fragment fragment = RegisterPasswordDateFragment.getInstance(phone);
		FragmentUtils.replaceFragmentWithStack(mManager, getFramentLayout(),
				fragment);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(IS_ACTIVITY_RESTART, true);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		RegisterPasswordDateFragment fragment = findFragmentByClass(RegisterPasswordDateFragment.class);
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		List<Fragment> fragments = mManager.getFragments();
		if (fragments != null) {
			for (Fragment fragment : fragments) {
				if (fragment != null) {
					fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
				}
			}
		}
	}
	
}
