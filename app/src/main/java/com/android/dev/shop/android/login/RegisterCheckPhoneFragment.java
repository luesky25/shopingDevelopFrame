package com.android.dev.shop.android.login;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.dev.basics.DevApplication;
import com.android.dev.shop.R;
import com.android.dev.shop.android.activity.CheckActivity;
import com.android.dev.shop.android.base.ShowLoadingTitleBarFragment;
import com.android.dev.shop.android.view.ClearEditText;
import com.android.dev.shop.http.ErrorCodeUtil;
import com.android.dev.shop.http.biz.HttpManagerFactory;
import com.android.dev.shop.http.biz.HttpUserManager;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.utils.ActivityUtils;
import com.android.dev.utils.ToolUtils;


/**
 * 手机验证码
 * 
 */
public class RegisterCheckPhoneFragment extends ShowLoadingTitleBarFragment implements HttpRequestHelper.IHttpResponseListener<String> {

	private ClearEditText mUserPhoneTv;
	private Button mGetCheckNumBtn;
	private static final int HTTP_GET_CHECKNUM = 0x1;
	private static final int HTTP_GET_FORGET_PASSSWORD_CODE = 0x2;

	private CheckActivity mCheckActivity;
	private HttpUserManager mHttpUserManger;

	private TextView mCopyrightBtn;
	private TextView mSecretBtn;
	private int type;//0--注册  1--找回密码
	/**
	 * 用户协议连接
	 */
	private static final String url="http://mobilering.kugou.com/help/protocol.html";
	/**
	 * 隐私条款
	 */
	private static final String secretUrl="http://ring.kugou.com/help/privacy.html";
	public static RegisterCheckPhoneFragment getInstance(int type) {
		RegisterCheckPhoneFragment fragment = new RegisterCheckPhoneFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("mType", type);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		type = bundle.getInt("mType");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_check_phone, container, false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	protected void findView(View v) {
		mUserPhoneTv = (ClearEditText) v.findViewById(R.id.user_phone_et);
		mGetCheckNumBtn = (Button) v.findViewById(R.id.get_checknum_btn);
		mCopyrightBtn = (TextView)v.findViewById(R.id.text_copyright_btn);
		mSecretBtn = (TextView)v.findViewById(R.id.text_secret_btn);
		mUserPhoneTv.setSaveEnabled(true);
		
		mUserPhoneTv.setSaveFromParentEnabled(true);
		
	}

	@Override
	protected void initData() {
		if (type==1)
			setTitleContent("找回密码");
		else if (type==2)
			setTitleContent("修改密码");
		else 
			setTitleContent("注册");
		
		mCheckActivity = (CheckActivity) getActivity();
		mHttpUserManger = (HttpUserManager) getVolleyFactory().getInstance(HttpManagerFactory.HTTP_USER_MANAGER);
		mGetCheckNumBtn.setEnabled(false);
		mGetCheckNumBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_et_unenable));
		mCopyrightBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
		mCopyrightBtn.getPaint().setAntiAlias(true);//抗锯齿
		mSecretBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
		mSecretBtn.getPaint().setAntiAlias(true);//抗锯齿
	}

	@Override
	protected void setListener() {
		setOnClickListenerSingle(mGetCheckNumBtn);
		setOnClickListenerSingle(mCopyrightBtn);
		setOnClickListenerSingle(mSecretBtn);
		mUserPhoneTv.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length()>=11) {
					mGetCheckNumBtn.setEnabled(true);
					mGetCheckNumBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.white));
				}else{
					mGetCheckNumBtn.setEnabled(false);
					mGetCheckNumBtn.setTextColor(DevApplication.getMyApplication().getResources().getColor(R.color.com_get_et_unenable));
				}
			}
		});
	}

	@Override
	public void OnClickSingle(View v) {
		super.OnClickSingle(v);
		switch (v.getId()) {
			case R.id.get_checknum_btn:
				final String phoneNum = mUserPhoneTv.getText().toString();
				if (TextUtils.isEmpty(phoneNum)) {
					showToast(" 请输入手机号");
					return;
				}else if(!ToolUtils.isPhoneCorrect(phoneNum)) {
					showToast("手机号不存在, 请输入正确手机号");
					return;
				}
				if (type!=0)
					getForgetPasswordCode(phoneNum);
				else
					getCheckNum(phoneNum);
		
				break;
			case R.id.text_copyright_btn:
					ActivityUtils.gotoWebViewActivity(mActivity,url,false);
				break;
			case R.id.text_secret_btn:
				ActivityUtils.gotoWebViewActivity(mActivity,secretUrl,false);
				break;
		}
	}

	/**
	 * 校验验证码成功,跳转完善资料界面
	 */
	public void checkingNumSuccess(String phone,int type) {
		try {
			if(mCheckActivity!=null&&!mCheckActivity.isFinishing())
                mCheckActivity.addSetDataFragment(phone,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	

	private void getCheckNum(String phoneNum){
		HttpMessage msg = new HttpMessage(HTTP_GET_CHECKNUM);
		mHttpUserManger.getCheckNum(phoneNum, this, msg);
	}
	private void getForgetPasswordCode(String phoneNum){
		HttpMessage msg = new HttpMessage(HTTP_GET_FORGET_PASSSWORD_CODE);
		mHttpUserManger.getPasswdSendCode(phoneNum, this, msg);
	}

	

	@Override
	public void onHttpSuccess(String data, HttpMessage msg) {
		int id = msg.what;
		hideLoading();
		switch (id) {
			case HTTP_GET_FORGET_PASSSWORD_CODE:

				break;
			case HTTP_GET_CHECKNUM:

				break;
			default:
				break;
		}

	}

	@Override
	public void onHttpFail(int errorCode, String errorResponse, HttpMessage msg) {
		int id = msg.what;
		hideLoading();
		switch (id) {
			case HTTP_GET_FORGET_PASSSWORD_CODE:
			case HTTP_GET_CHECKNUM:
				ErrorCodeUtil.handleErrorCode(errorCode);
				break;

			default:
				break;
		}
	}
}
