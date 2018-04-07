/**
 * 
 */
package com.android.dev.shop.android.login;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.common.http.utils.PreferencesUtils;
import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.R;
import com.android.dev.shop.android.activity.CheckActivity;
import com.android.dev.shop.android.base.ShowLoadingTitleBarFragment;
import com.android.dev.shop.android.base.ui.CommonDialog;
import com.android.dev.shop.android.eventbus.EventType;
import com.android.dev.shop.android.eventbus.KGEventBus;
import com.android.dev.shop.android.model.User;
import com.android.dev.shop.android.view.ClearEditText;
import com.android.dev.shop.http.ErrorCodeUtil;
import com.android.dev.shop.http.biz.HttpManagerFactory;
import com.android.dev.shop.http.biz.HttpUserManager;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.utils.StringUtil;



/**
 * 登录
 * @author michellezhao
 *
 */
public class LoginFragment extends ShowLoadingTitleBarFragment implements HttpRequestHelper.IHttpResponseListener<String>{
	private TextView mLoginBtn;
	private ClearEditText mUserNameEt;
	private ClearEditText mUserPasswordEt;
//	private TextView mForgetPasswordTv;
	private TextView mRegister;
	private static final int HTTP_POST_LOGIN = 0x1;
	private static final int HTTP_GET_PHONE_TYPE = 0x2;
	private static final int HTTP_POST_THIRD_TYPE = 0x3;



	private HttpUserManager mHttpUserManager;
	private CheckActivity mCheckActivity;
	private CommonDialog mDialog;
	String oldUserKey;
	public static LoginFragment getInstance() {
		LoginFragment fragment = new LoginFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}
	@Override
	protected void findView(View v) {
		mLoginBtn = (TextView)v.findViewById(R.id.login_button);
		mUserNameEt = (ClearEditText) v.findViewById(R.id.login_user_name_et);
		mUserPasswordEt = (ClearEditText) v.findViewById(R.id.login_user_password_et);
//		mForgetPasswordTv = (TextView) v.findViewById(R.id.login_tv_forget_password);
		mRegister = (TextView) v.findViewById(R.id.login_to_register);

		mUserNameEt.setSaveEnabled(true);
	
		mUserPasswordEt.setSaveEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mUserPasswordEt.setSaveFromParentEnabled(true);
			mUserNameEt.setSaveFromParentEnabled(true);
		}
	}

	@Override
	protected void initData() {
		setTitleContent("登录");
		setTabRightBtnTextSize(15);
		setTabRightBtnTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.title_color));
		setTabRightBtnText(R.string.forgot_password);
		mCheckActivity = (CheckActivity) getActivity();
		String accout = PreferencesUtils.getString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_ACOUNT);
		String password = PreferencesUtils.getString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_PASSWORD);
		oldUserKey = PreferencesUtils.getString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.USER_KEY);
		accout = "test";password = "123456";
		if (!StringUtil.isEmpty(password)||!StringUtil.isEmpty(accout)) {
			String decryptPass = null;
			try {
				if (!StringUtil.isEmpty(accout)) {

					mUserNameEt.setText(accout);
					mUserNameEt.post(new Runnable() {
						@Override
						public void run() {
							if(mUserNameEt.getText().length()>0)
							mUserNameEt.setSelection(mUserNameEt.getText().length());
						}
					});
				}
				decryptPass = password;
				if(decryptPass!=null){
					mUserPasswordEt.setText(decryptPass);
					mUserPasswordEt.post(new Runnable() {
						@Override
						public void run() {
							if(mUserPasswordEt.getText().length()>0)
							mUserPasswordEt.setSelection(mUserPasswordEt.getText().length());
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			mLoginBtn.setEnabled(false);
		}
//		mForgetPasswordTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//		mForgetPasswordTv.getPaint().setAntiAlias(true);//抗锯齿
		mHttpUserManager = (HttpUserManager) getVolleyFactory().getInstance(HttpManagerFactory.HTTP_USER_MANAGER);
	}


	@Override
	public void onReload() {
		super.onReload();
		String password = PreferencesUtils.getString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_PASSWORD);

		if (!StringUtil.isEmpty(password)) {
			String decryptPass = null;
			try {
				decryptPass = password;
				KGLog.d("debug","decryptPass--==>"+decryptPass);
				if (decryptPass != null) {
					mUserPasswordEt.setText(decryptPass);
					mUserPasswordEt.post(new Runnable() {
						@Override
						public void run() {
							if(mUserPasswordEt.getText().length()>0)
							mUserPasswordEt.setSelection(mUserPasswordEt.getText().length());
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void setListener() {
		setOnClickListenerSingle(mLoginBtn);
		setOnClickListenerSingle(mRegister);

		mUserNameEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length()>=3&&mUserPasswordEt.getText().length()>=6) {
					mLoginBtn.setEnabled(true);
					mLoginBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.white));
				}else{
					mLoginBtn.setEnabled(false);
					mLoginBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_login_unenable));
				}
			}
		});
		mUserPasswordEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length()>=6&&mUserNameEt.getText().length()>=3) {
					mLoginBtn.setEnabled(true);
					mLoginBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.white));
				}else{
					mLoginBtn.setEnabled(false);
					mLoginBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_login_unenable));
				}
			}
		});
	}

	 String userName;
	 String password;
	@Override
	public void OnClickSingle(View v) {
		super.OnClickSingle(v);
		switch(v.getId()) {
		case R.id.login_button:
			 userName = mUserNameEt.getText().toString();
			 password = mUserPasswordEt.getText().toString();

			//进行参数有效性校验
			if(checkParams(userName, password)) {
				showLoading("",true);
				checkLogin(userName,password);
			}
			break;
		case R.id.login_to_register:
//			gotoForgetPassword();
			gotoRegisterWork();
			break;
		}
	}

	@Override
	public void OnClicked(View v) {
		super.OnClicked(v);
	}

	/**
	 * 校验参数有效性
	 * @param userName
	 * @param password
	 * @return 是否通过验证
	 */
	private boolean checkParams(String userName, String password) {
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			showToast("账号/密码不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 登录
	 * @param userName
	 * @param password
	 */
	public void checkLogin(String userName,String password){
		HttpMessage msg = new HttpMessage(HTTP_POST_LOGIN);
		mHttpUserManager.beginLogin(userName, password, this, msg);
	}
	


	User user;
	@Override
	public void onHttpSuccess(String data, HttpMessage msg) {
		int id = msg.what;
		
		switch (id) {
			case HTTP_POST_LOGIN:
				
//				RingBackMusicRespone comment = null;
//				try {
//					comment = ((RingBackMusicRespone<Object>) HttpRequestHelper.fromJson((String) data, new TypeToken<RingBackMusicRespone<User>>(){}.getType()));
//				} catch (IllegalStateException | JsonSyntaxException e) {
//					e.printStackTrace();
//				}
//				if (comment!=null&&comment.getResCode().equals(HttpErrorCode.RESULT_OK)&&comment.getResponse()!=null){
//					UmengDataReportUtil.onEvent(DevApplication.getMyApplication().getApplicationContext(), UmengEventID.V370_login_signin_success);
//					user = (User) comment.getResponse();
//					PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.SESSION_ID, user.getSession_id());
//					PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.USER_KEY, user.getUser_info().getKey());
//					PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_ACOUNT, DESUtils.Encrypt(userName));
//					PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_PASSWORD, DESUtils.Encrypt(password));
//					PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_USER_ID, user.getUser_info().getUser_id());
//					if (!TextUtils.isEmpty(oldUserKey)&&!TextUtils.isEmpty(user.getUser_info().getKey())&&!oldUserKey.equals(user.getUser_info().getKey())) {//切换账号的时候 才把时间戳更为0
//						SharedPreferences sharedPreferences = KTVConfigure.APP_CONTEXT.getSharedPreferences(InviteFriendModel.CONTACT_FRIEND_TIME, Context.MODE_PRIVATE);
//						sharedPreferences.edit().putLong(InviteFriendModel.CONTACT_FRIEND_TIME, 0).commit();
//					}
//					PreferencesUtils.putBoolean(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.AUTO_LOGIN, true);
//					DevApplication.getMyApplication().setIsGuest(false);
////					showLoading("",true);
////					loginSuccess(user);
//				}else if(comment!=null&&comment.getResCode().equals(HttpErrorCode.cmmLoginReport)){
//					hideLoading();
//					user = (User) comment.getResponse();
//					if (user!=null) {
//						showToast(user.getReport_info().getReason());
//					}
//				}else{
//					hideLoading();
//					if (comment!=null&&comment.getResMsg()!=null) {
//						showToast(comment.getResMsg());
//					}
//				}
				break;
			default:
				break;
		}

	}

	/**
	 * 用户登录成功之后，清理掉用户之前保存的本地数据
	 */
	private void clenCrbtUser(){
//		PrefUtil.setCmmPhoneNum(DevApplication.getMyApplication().getApplicationContext(), "");
//		PrefUtil.setCtmPhoneNum(DevApplication.getMyApplication().getApplicationContext(), "");
//		PrefUtil.setUncPhoneNum(DevApplication.getMyApplication().getApplicationContext(), "");
//		PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.ACCESS_TOKEN, "");
//		PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.CTM_CRBT_TOKEN, "");
//		PreferencesUtils.putBoolean(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.RINGTON_UNC_TOKEN_IS_FAILURE, true);
	}
	@Override
	public void onHttpFail(int errorCode, String errorResponse, HttpMessage msg) {
		int id = msg.what;
		hideLoading();
		switch (id) {	
			case HTTP_POST_LOGIN:
			case HTTP_POST_THIRD_TYPE:
				ErrorCodeUtil.handleErrorCode(errorCode);
			break;

		default:
			break;
		}
	}

	/**
	 * 登录成功，跳转主界面
	 * @param user 
	 */
	public void loginSuccess(final User user) {
		DevApplication.getMyApplication().setUserData(user);
//		UserDBManager.getInstance().setUserData(user);
		KGEventBus.post(EventType.EVENT_USER_LOGIN_UPDATE_INFO);
//		getActivity().getContentResolver().notifyChange(ProviderUriBuilder.get(UserConstant._URI_).what(UserProviderUtils._TAG_LOGIN_).build(),null);



//		ActivityUtils.gotoActivity(mCheckActivity, TurnActivity.class, ActivityUtils.TURN_MAIN_AND_REFLUSE, true);
	}


	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			Activity activity = getActivity();
			KGLog.d("LoginFragment", "onDestroy activity="+activity.toString());
		} catch (Exception e) {
			Log.d("LoginFragment", "unregisterReceiver exception"+e);
		}
	}
	/**
	 * 跳转忘记密码界面
	 */
	public void gotoForgetPassword() {
		mCheckActivity.addCheckPhoneFragment(1);
	}

	/**
	 * 请求Activity跳转到注册界面
	 */
	public void gotoRegisterWork() {
		mCheckActivity.addCheckPhoneFragment(0);
	}



	@Override
	protected void onTabRightClick(View v) {
		super.onTabRightClick(v);
//		gotoRegisterWork();
		gotoForgetPassword();
	}


	@Override
	public void onResume() {
		super.onResume();
//		hideLoading();
	}
}
