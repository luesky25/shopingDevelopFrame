package com.android.dev.shop.android.login;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.common.http.utils.PreferencesUtils;
import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.R;
import com.android.dev.shop.android.activity.CheckActivity;
import com.android.dev.shop.android.base.ShowLoadingTitleBarFragment;
import com.android.dev.shop.android.eventbus.EventBusCallback;
import com.android.dev.shop.android.eventbus.EventMessage;
import com.android.dev.shop.android.eventbus.EventType;
import com.android.dev.shop.android.eventbus.KGEventBus;
import com.android.dev.shop.android.utils.DESUtils;
import com.android.dev.shop.android.utils.FragmentUtils;
import com.android.dev.shop.android.view.ClearEditText;
import com.android.dev.shop.http.ErrorCodeUtil;
import com.android.dev.shop.http.biz.HttpManagerFactory;
import com.android.dev.shop.http.biz.HttpUserManager;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.utils.StringUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 注册/忘记密码
 */
public class RegisterPasswordFragment extends ShowLoadingTitleBarFragment implements HttpRequestHelper.IHttpResponseListener, EventBusCallback {
	
	private Button mSubmitBtn;
	


	private ClearEditText mCodeEt;
	private EditText mPasswordEtTwo;

	

	private CheckActivity mCheckActivity;

	
	
	private String mPhone;

	
	
	private HttpUserManager mHttpUserManger;

	private static final int HTTP_POST_REGISTER_USER = 0x01;
	private static final int HTTP_GET_CHECKNUM = 0x02;
	private static final int HTTP_GET_PASSWORD_CODE = 0x03;
	private static final int HTTP_POSS_MODIFY_PASSWORD = 0x04;
	private static final int HTTP_GET_PHONE_TYPE = 0x5;
	private Button mGetCheckNumBtn;
	private TextView mShowPhoneNum;
	private int type;//0--注册  1--找回密码
	private TextView isshowPassword;
	private boolean isShowPassword =false;
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	public static RegisterPasswordFragment getInstance(String phone,int type) {
		RegisterPasswordFragment fragment = new RegisterPasswordFragment();
		Bundle bundle = new Bundle();
		bundle.putString("mPhone", phone);
		bundle.putInt("mType", type);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mPhone = bundle.getString("mPhone");
		type = bundle.getInt("mType");
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_check_password, container, false);
	}


	@Override
	protected void findView(View v) {
		mSubmitBtn = (Button) v.findViewById(R.id.submit_btn);

		mGetCheckNumBtn = (Button) v.findViewById(R.id.get_checknum_btn);
		mPasswordEtTwo = (EditText) v.findViewById(R.id.user_check_password_et);
		mCodeEt = (ClearEditText) v.findViewById(R.id.user_code_et);
		mShowPhoneNum = (TextView) v.findViewById(R.id.register_show_phoneNum);
		isshowPassword = (TextView) v.findViewById(R.id.register_isshow_password);

		mPasswordEtTwo.setSaveEnabled(true);
	
		mCodeEt.setSaveEnabled(true);
		
		mCodeEt.setSaveFromParentEnabled(true);
		mPasswordEtTwo.setSaveFromParentEnabled(true);
		
	}
	
	@Override
	protected void initData() {
		if (type==1)
			setTitleContent("找回密码");
		else if (type==2)
			setTitleContent("修改密码");
//		else
//			setTitleContent("注册");

		KGEventBus.register(this);
		mCheckActivity = (CheckActivity) mActivity;
		mHttpUserManger = (HttpUserManager) getVolleyFactory().getInstance(HttpManagerFactory.HTTP_USER_MANAGER);
		mShowPhoneNum.setText(mPhone+"");
		setGetCheckNumable(false);
		mSubmitBtn.setEnabled(false);
		mSubmitBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_et_unenable));

	}




	private CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			mGetCheckNumBtn.setText("重新发送("+(int) (millisUntilFinished / 1000) + "s)");
			
		}

		@Override
		public void onFinish() {
			setGetCheckNumable(true);
		}
	};




	@Override
	protected void setListener() {
		setOnClickListenerSingle(mSubmitBtn);
		setOnClickListenerSingle(mGetCheckNumBtn);
		setOnClickListenerSingle(isshowPassword);

		mCodeEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length()>=6&&mPasswordEtTwo.getText().length()>=6) {
					mSubmitBtn.setEnabled(true);
					mSubmitBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.white));
				}else{
					mSubmitBtn.setEnabled(false);
					mSubmitBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_et_unenable));
				}
			}
		});
		mPasswordEtTwo.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() >= 6 && mCodeEt.getText().length() >= 6) {
					mSubmitBtn.setEnabled(true);
					mSubmitBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.white));
				} else {
					mSubmitBtn.setEnabled(false);
					mSubmitBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_et_unenable));
				}
			}
		});
	}
	String password;
	@Override
	public void OnClickSingle(View v) {
		super.OnClickSingle(v);
		switch (v.getId()) {
			case R.id.submit_btn:
				if (!checkInput())
					return;
				String code = mCodeEt.getText().toString();
				password = mPasswordEtTwo.getText().toString();
//				if (type!=0)
					resetPassword(mPhone, code, password);
//				else
//					registerUser(mPhone, code, password);
				break;
			case R.id.get_checknum_btn:
				setGetCheckNumable(false);
//				if (type!=0)
					getPasswordCode(mPhone);
//				else 
//					getCheckNum(mPhone);
				break;
			case R.id.register_isshow_password:
				if (isShowPassword){
					mPasswordEtTwo.setTransformationMethod(PasswordTransformationMethod.getInstance());
//					mPasswordEtTwo.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
					isshowPassword.setBackgroundResource(R.drawable.resign_password_unview);
					isShowPassword =false;
				}else{
					mPasswordEtTwo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					isshowPassword.setBackgroundResource(R.drawable.resign_password_view);
					isShowPassword =true;
				}
				break;
		}
	}
	


	private boolean checkInput() {
		
		
		String passwordCheck = mPasswordEtTwo.getText().toString();
		
		if (!StringUtil.isValidPassword(passwordCheck)) {
			showToast(R.string.passwordInvalid);
			return false;
		}
		return true;
	}
	
	@Override
	public void OnClicked(View v) {
		super.OnClicked(v);
		switch(v.getId()) {
//		case R.id.gender_alert_close_tv:
//			hideGenderAlertDialog();
//			break;
		}
	}
	




	public void registerSuccess() {
//		showToast("注册成功");
//		ActivityUtils.gotoActivity(mActivity, CheckActivity.class, true);
		PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_PASSWORD, DESUtils.Encrypt(password));
		if (type==1){
			if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0){
				FragmentUtils.popBackStackAndNotify(getFragmentManager());
			}
		}else if (type==2){
			KGEventBus.post(EventType.EVENT_PASSWORD_UPDATE_LOGIN_INFO);
			mActivity.finish();
		}
//		ActivityUtils.gotoActivity(mActivity, TurnActivity.class, ActivityUtils.TURN_MAIN_AND_REFLUSE, true);
	}
	
//	private void registerUser(String phoneNum,String checkCode,String password){
//		HttpMessage msg = new HttpMessage(HTTP_POST_REGISTER_USER);
//		mHttpUserManger.registerUser(phoneNum, checkCode,password,this, msg);
//	}
	private void resetPassword(String phoneNum,String checkCode,String password){
		HttpMessage msg = new HttpMessage(HTTP_POSS_MODIFY_PASSWORD);
		mHttpUserManger.resetPassword(phoneNum, checkCode, password, this, msg);
	}



	private void getCheckNum(String phoneNum){
		HttpMessage msg = new HttpMessage(HTTP_GET_CHECKNUM);
		mHttpUserManger.getCheckNum(phoneNum, this, msg);
	}	
	private void getPasswordCode(String phoneNum){
		HttpMessage msg = new HttpMessage(HTTP_GET_PASSWORD_CODE);
		mHttpUserManger.getPasswdSendCode(phoneNum, this, msg);
	}


	@Override
	public void onHttpSuccess(Object data, HttpMessage msg) {
		int id = msg.what;
		switch (id) {
			case HTTP_POSS_MODIFY_PASSWORD:

				break;
			case HTTP_GET_PASSWORD_CODE:
			case HTTP_GET_CHECKNUM:

				break;
			default:
				break;
		}
	}


	/**
	 * 用户登录成功之后，清理掉用户之前保存的本地数据
	 */
	private void clenCrbtUser(){
		PreferencesUtils.putString(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.LOGIN_ACOUNT, "");
		PreferencesUtils.putBoolean(DevApplication.getMyApplication().getApplicationContext(), PreferenceConfig.RINGTON_UNC_TOKEN_IS_FAILURE, true);
	}
	
	@Override
	public void onHttpFail(int errorCode, String errorResponse, HttpMessage msg) {
		int id = msg.what;
		KGLog.d("debug","comment--==>"+errorCode);
		switch (id) {
			case HTTP_POSS_MODIFY_PASSWORD:
			case HTTP_GET_PASSWORD_CODE:
			case HTTP_POST_REGISTER_USER:
				ErrorCodeUtil.handleErrorCode(errorCode);
				break;
			case HTTP_GET_CHECKNUM:
				ErrorCodeUtil.handleErrorCode(errorCode);
				break;
			
			default:
				break;
		}
	}

	/**
	 * 设置获取按钮是否可用
	 *
	 * @param able
	 */
	public void setGetCheckNumable(boolean able) {
		if (able) {
			// 避免enable与press属性冲突，延时至点击事件结束后执行
			mGetCheckNumBtn.postDelayed(new Runnable() {
				@Override
				public void run() {
					mGetCheckNumBtn.setEnabled(true);
				}
			}, 50);
			mGetCheckNumBtn.setText("重新发送");
			mGetCheckNumBtn.setBackgroundResource(R.drawable.btn_one_key_set_selecter);
			mGetCheckNumBtn.setTextColor(Color.WHITE);
			
			try {
				timer.cancel();
			} catch (Exception e) {
			}
		} else {
			// 避免enable与press属性冲突，延时至点击事件结束后执行
			mGetCheckNumBtn.postDelayed(new Runnable() {
				@Override
				public void run() {
					mGetCheckNumBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_code_tv));
					mGetCheckNumBtn.setBackgroundResource(R.drawable.shape_gray_all_bg);
					mGetCheckNumBtn.setEnabled(false);
				}
			}, 50);
			timer.start();
		}
	}

	@Override
	public void onLeave() {
		super.onLeave();
		if (type==1) {
			if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0)
				getFragmentManager().popBackStackImmediate();
		}
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();

		KGEventBus.unregister(this);
	}


	@Override
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(EventMessage msg) {
		switch (msg.what) {
			case EventType.EVENT_SHOPPING_SMS_VERIFY_SUCCESS:
					
//					mCodeEt.post(new Runnable() {
//						@Override
//						public void run() {
//							mCodeEt.setText(PrefUtil.getSmsVerifiyCode(mActivity));
//							mCodeEt.setFocusable(true);
//							if(mCodeEt.getText().length()>0)
//							mCodeEt.setSelection(mCodeEt.getText().length());
//						}
//					});
				break;
		}
	}
}
