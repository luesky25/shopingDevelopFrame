package com.android.dev.shop.android.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.permissiions.UseCameraPermissionUtils;
import com.android.common.utils.KGLog;
import com.android.common.view.RoundedImageView;
import com.android.dev.basics.DevApplication;
import com.android.dev.framework.component.imagecrop.CropImage;
import com.android.dev.shop.R;
import com.android.dev.shop.android.activity.CheckActivity;
import com.android.dev.shop.android.base.ShowLoadingTitleBarFragment;
import com.android.dev.shop.android.base.ui.CommonDialog;
import com.android.dev.shop.android.eventbus.EventBusCallback;
import com.android.dev.shop.android.eventbus.EventMessage;
import com.android.dev.shop.android.eventbus.EventType;
import com.android.dev.shop.android.eventbus.KGEventBus;
import com.android.dev.shop.android.model.CloudUriData;
import com.android.dev.shop.android.utils.AppUtil;
import com.android.dev.shop.android.utils.ImageUtil;
import com.android.dev.shop.android.view.ClearEditText;
import com.android.dev.shop.dialog.EditTextDialog;
import com.android.dev.shop.http.ErrorCodeUtil;
import com.android.dev.shop.http.biz.HttpManagerFactory;
import com.android.dev.shop.http.biz.HttpUploadManager;
import com.android.dev.shop.http.biz.HttpUserManager;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.utils.FileUtil;
import com.android.dev.utils.SDCardUtil;
import com.android.dev.utils.StringUtil;
import com.android.dev.utils.ToolUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * 注册/忘记密码
 */
public class RegisterPasswordDateFragment extends ShowLoadingTitleBarFragment implements HttpRequestHelper.IHttpResponseListener, EventBusCallback {
	
	private Button mSubmitBtn;
	


	private ClearEditText mCodeEt;
	private EditText mPasswordEtTwo;

	

	private CheckActivity mCheckActivity;

	
	
	private String mPhone;

	
	
	private HttpUserManager mHttpUserManger;

	private static final int HTTP_POST_REGISTER_USER = 0x01;
	private static final int HTTP_GET_CHECKNUM = 0x02;
	private static final int HTTP_GET_PASSWORD_CODE = 0x03;
	private static final int HTTP_GET_PHONE_TYPE = 0x5;

	private final int HTTP_CRBT_MODIFYUSERNICKNAME = 0x6;

	private static final int HTTP_UPLOAD_FACE_IMAGE = 0x6;

	/**
	 * 用户剪辑完的要上传的头像路径
	 */
	public static final String UPLOAD_USER_IMAGE_FILE_PATH = SDCardUtil.IMAGE_CACHE_FOLDER
			+ "user_upload_image.jpg";
	/**
	 * 给摄像机存放拍照相片的路径
	 */
	public static final String CAMERA_TMP_FILE_PATH = SDCardUtil.IMAGE_CACHE_FOLDER
			+ "user_image_tmp.jpg";

	private Button mGetCheckNumBtn;
	private TextView mShowPhoneNum;
	private TextView isshowPassword;
	private boolean isShowPassword =false;

	private LinearLayout head_info_ll;
	private LinearLayout nick_name_ll;
	private RelativeLayout sex_ll;

	private EditTextDialog mDialog;
	private String newNickName;
	private TextView nickName;
	private boolean isCamera = false;
	public static final int SELECT_PICTURE_FROM_LIB = 11;

	public static final int SELECT_PICTURE_FROM_CAMERA = 12;

	public static final int CROP_IMAGE_RESULT = 13;
	private Uri SMS_INBOX = Uri.parse("content://sms/");

	String imageHash, imageName;

	private HttpUploadManager mHttpUploadManager;

	private RoundedImageView headImage;
	private TextView sex_tv;
	private int userSex = 0;
	public static RegisterPasswordDateFragment getInstance(String phone) {
		RegisterPasswordDateFragment fragment = new RegisterPasswordDateFragment();
		Bundle bundle = new Bundle();
		bundle.putString("mPhone", phone);
	
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mPhone = bundle.getString("mPhone");

		if (savedInstanceState != null) {
			isCamera = savedInstanceState.getBoolean("isCamera");
		}
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_register_password, container, false);
	}


	@Override
	protected void findView(View v) {
		mSubmitBtn = (Button) v.findViewById(R.id.submit_btn);

		mGetCheckNumBtn = (Button) v.findViewById(R.id.get_checknum_btn);
		mPasswordEtTwo = (EditText) v.findViewById(R.id.user_check_password_et);
		mCodeEt = (ClearEditText) v.findViewById(R.id.user_code_et);
		mShowPhoneNum = (TextView) v.findViewById(R.id.register_show_phoneNum);
		isshowPassword = (TextView) v.findViewById(R.id.register_isshow_password);
		head_info_ll = (LinearLayout) v.findViewById(R.id.head_info_ll);
		nick_name_ll = (LinearLayout) v.findViewById(R.id.nick_name_ll);
		sex_ll = (RelativeLayout) v.findViewById(R.id.sex_ll);
		headImage = (RoundedImageView) v.findViewById(R.id.head_portrait_image);
		nickName = (TextView) v.findViewById(R.id.nick_name);
		mPasswordEtTwo.setSaveEnabled(true);
	
		mCodeEt.setSaveEnabled(true);
		
		mCodeEt.setSaveFromParentEnabled(true);
		mPasswordEtTwo.setSaveFromParentEnabled(true);
		sex_tv = (TextView) v.findViewById(R.id.sex_tv);
	}
	
	@Override
	protected void initData() {
		setTitleContent("注册");

		KGEventBus.register(this);
		mCheckActivity = (CheckActivity) mActivity;
		mHttpUserManger = (HttpUserManager) getVolleyFactory().getInstance(HttpManagerFactory.HTTP_USER_MANAGER);
		mHttpUploadManager = (HttpUploadManager) getVolleyFactory().getInstance(HttpManagerFactory.HTTP_UPLOAD_MANAGER);
		mShowPhoneNum.setText(mPhone+"");

		mDialog = new EditTextDialog(mActivity);
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
		setOnClickListener(head_info_ll);
		setOnClickListener(nick_name_ll);
		setOnClickListener(sex_ll);
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
				String code = mCodeEt.getText().toString().trim();
				password = mPasswordEtTwo.getText().toString();
				String nick_name = nickName.getText().toString();
				if (TextUtils.isEmpty(nick_name)) {
					Toast.makeText(DevApplication.getMyApplication(),
							R.string.kg_reg_toast_nickname_err,
							Toast.LENGTH_LONG).show();
					return;
				} else if (AppUtil.length(nick_name) > 20
						|| AppUtil.length(nick_name) < 1) {
					Toast.makeText(DevApplication.getMyApplication(),
							R.string.kg_reg_toast_nickname_err,
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(imageHash)&&TextUtils.isEmpty(imageName)) {
					Toast.makeText(DevApplication.getMyApplication(),
							"请选择头像",
							Toast.LENGTH_LONG).show();
					return;
				}	
				if (TextUtils.isEmpty(sex_tv.getText().toString())&&userSex==0) {
					Toast.makeText(DevApplication.getMyApplication(),
							"请选择性别",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				registerUser(mPhone, code, password,imageHash,imageName,nick_name,userSex+"");
				break;
			case R.id.get_checknum_btn:
				setGetCheckNumable(false);
				getCheckNum(mPhone);
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
			case R.id.head_info_ll:
				showModifyHeadDialog();
				break;
			case R.id.sex_ll://性别
				   if (mActivity!=null&&!mActivity.isFinishing())
				showModifySexDialog();
				break;
			case R.id.nick_name_ll:
				mDialog.setCancelable(false);
				mDialog.setTitle("修改昵称");
				mDialog.setHintText("请输入您的个性签名...");
			    mDialog.setEditTextString("");
				
				mDialog.setPositiveButtonClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 如果当前没有网络
						if (!ToolUtils.checkNetwork(DevApplication.getMyApplication())) {
							showToast(R.string.no_network);
						} else {
							newNickName = mDialog.getEditTextString();
							if (TextUtils.isEmpty(newNickName)) {
								Toast.makeText(DevApplication.getMyApplication(),
										R.string.kg_reg_toast_nickname_err,
										Toast.LENGTH_LONG).show();
								return;
							} else if (AppUtil.length(newNickName) > 20
									|| AppUtil.length(newNickName) < 1) {
								Toast.makeText(DevApplication.getMyApplication(),
										R.string.kg_reg_toast_nickname_err,
										Toast.LENGTH_LONG).show();
								return;
							} else {
								nickName.setText(newNickName+"");
							}

						}
						mDialog.dismiss();
					}
				});
				mDialog.show();
				break;
		}
	}

	private void showModifyHeadDialog() {
		View contentView = LayoutInflater.from(mActivity).inflate(R.layout.photo_choose_layout, null);
		final CommonDialog commonDialog = CommonDialog.makeNewDialog(mActivity,
				contentView);
		TextView cancelBtn = (TextView) contentView
				.findViewById(R.id.cancel_txt);
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commonDialog.cancel();
			}
		});
		TextView cameraBtn = (TextView) contentView
				.findViewById(R.id.camera_txt);
		TextView albumBtn = (TextView) contentView.findViewById(R.id.album_txt);
		albumBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commonDialog.cancel();
				// 从相册选择照片
				takePicFromLibrary();
			}
		});
		cameraBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commonDialog.cancel();
				// 打开照相机
				if (UseCameraPermissionUtils.haveEnoughUseCameraPermissions(mActivity)) {
					takePicFromCamera();
				} else {
					UseCameraPermissionUtils.requestUseCameraPermission(mActivity);
				}
			}
		});
		if (!commonDialog.isShowing()) {
			commonDialog.show();
		}
	}


	private void takePicFromLibrary() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		if (AppUtil.isIntentAvailable(mActivity, intent)) {
			startActivityForResult(intent, SELECT_PICTURE_FROM_LIB);
		}
		KGLog.d("debug", "CROP_IMAGE_RESULT0000");
	}

	private void takePicFromCamera() {
		FileUtil.deleteFile(CAMERA_TMP_FILE_PATH);
		Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		FileUtil.createFile(CAMERA_TMP_FILE_PATH,FileUtil.MODE_COVER);
		intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(CAMERA_TMP_FILE_PATH)));
		isCamera = true;
		KGLog.d("debug", "是否存在" + FileUtil.isExist(CAMERA_TMP_FILE_PATH));
		startActivityForResult(intentCamera, SELECT_PICTURE_FROM_CAMERA);
	}
	
	private void registerUser(String phoneNum,String checkCode,String password,String imageHash,String imageName,String nickname,String user_sex){
		HttpMessage msg = new HttpMessage(HTTP_POST_REGISTER_USER);
		mHttpUserManger.registerUser(phoneNum, checkCode,password,imageHash,imageName,nickname,user_sex,this, msg);
	}
	

	private void getCheckNum(String phoneNum){
		HttpMessage msg = new HttpMessage(HTTP_GET_CHECKNUM);
		mHttpUserManger.getCheckNum(phoneNum, this, msg);
	}	



	@Override
	public void onHttpSuccess(Object data, HttpMessage msg) {
		int id = msg.what;
		switch (id) {
			case HTTP_POST_REGISTER_USER:
				break;
			case HTTP_GET_CHECKNUM:
				break;

			case HTTP_UPLOAD_FACE_IMAGE:
				KGLog.d("debug", "HTTP_UPLOAD_FACE_IMAGE--==>");
				CloudUriData responseData = HttpRequestHelper.fromJson((String) data, CloudUriData.class);
				if (responseData != null) {
					imageHash = responseData.getContent_MD5();
					imageName =  responseData.getUri();
				} else {
					//ToolUtils.showToast(QuitActivity.this, "上传失败");
				}
				hideLoading();
				break;
			default:
				break;
		}
	}



	@Override
	public void onHttpFail(int errorCode, String errorResponse, HttpMessage msg) {
		int id = msg.what;
		switch (id) {
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
			mGetCheckNumBtn.setBackgroundResource(R.drawable.shape_green_all_bg);
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
					
					mCodeEt.post(new Runnable() {
						@Override
						public void run() {
//							mCodeEt.setText(PrefUtil.getSmsVerifiyCode(mActivity));
//							mCodeEt.setFocusable(true);
//							if(mCodeEt.getText().length()>0)
//							mCodeEt.setSelection(mCodeEt.getText().length());
						}
					});
				break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if (isCamera && FileUtil.isExist(CAMERA_TMP_FILE_PATH)) {
			Intent intent = createCropIntent(mActivity);
			intent.setData(Uri.fromFile(new File(CAMERA_TMP_FILE_PATH)));
			startActivityForResult(intent, CROP_IMAGE_RESULT);
			isCamera = false;
		}
	}

	private Intent createCropIntent(Context context) {
		Intent crop = new Intent(context, CropImage.class);
		crop.putExtra("aspectX", 1);// 宽高比
		crop.putExtra("aspectY", 1);
		crop.putExtra("outputX", 400);// 保存图片大小
		crop.putExtra("outputY", 400);
		crop.putExtra("scale", true);
		crop.putExtra("setWallpaper", false);
		crop.putExtra("noFaceDetection", true);
		return crop;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case SELECT_PICTURE_FROM_CAMERA:
					if (isCamera && FileUtil.isExist(CAMERA_TMP_FILE_PATH)) {
						Intent crop = createCropIntent(mActivity);
						crop.setData(Uri.fromFile(new File(CAMERA_TMP_FILE_PATH)));
						startActivityForResult(crop, CROP_IMAGE_RESULT);
						isCamera = false;
					}
					break;
				case SELECT_PICTURE_FROM_LIB:
					KGLog.d("debug", "CROP_IMAGE_RESULT111");
					if (data == null) {
						return;
					}
					KGLog.d("debug", "CROP_IMAGE_RESULT222");
					Intent intent = createCropIntent(mActivity);
					intent.setData(data.getData());
					startActivityForResult(intent, CROP_IMAGE_RESULT);
					break;
				case CROP_IMAGE_RESULT:
					KGLog.d("debug", "CROP_IMAGE_RESULT");
					if (data == null) {
						showToast("图片不可用");
						return;
					}
					String action = data.getAction();
					Bitmap bm = null;

					if (!TextUtils.isEmpty(action) && "inline-data".equals(action)) {
						// 从相机拍摄
						bm = (Bitmap) data.getExtras().get("data"); // 返回一个Bitmap图片
					} else {
						Uri uri = Uri.parse(action);
						try {
							bm = MediaStore.Images.Media.getBitmap(DevApplication.getMyApplication().getContentResolver(), uri);
						} catch (FileNotFoundException e) {
							KGLog.d(e.getMessage());
						} catch (IOException e) {
							KGLog.d(e.getMessage());
						}
					}
					if (bm != null) {
						showLoading("",true);
						if (bm != null) {
							FileUtil.createFile(UPLOAD_USER_IMAGE_FILE_PATH, FileUtil.MODE_COVER);
							ImageUtil.saveBitmap(bm, UPLOAD_USER_IMAGE_FILE_PATH,
									Bitmap.CompressFormat.JPEG);
							headImage.setImageBitmap(bm);
							sendUserHead();
						}
					}
					break;
				default:
					break;
			}
		}
	}
	/**
	 * 上传用户头像
	 */
	private void sendUserHead() {
		HttpMessage upLoadMsg = new HttpMessage(HTTP_UPLOAD_FACE_IMAGE);
		mHttpUploadManager.upLoadImageWithThumbCloud(UPLOAD_USER_IMAGE_FILE_PATH, this, upLoadMsg);
	}

	/**
	 * 页面权限反馈回调
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (UseCameraPermissionUtils.onRequestPermissionsResult(mActivity, requestCode, permissions, grantResults)){
			takePicFromCamera();
		}
	}

	private void showModifySexDialog() {
		View contentView = LayoutInflater.from(mActivity).inflate(R.layout.sex_dialog_layout, null);
		final CommonDialog commonDialog = CommonDialog.makeNewDialog(mActivity,
				contentView);
		TextView cancelBtn = (TextView) contentView
				.findViewById(R.id.cancel_txt);
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commonDialog.cancel();
			}
		});
		TextView man_tv = (TextView) contentView
				.findViewById(R.id.man_tv);
		TextView women_tv = (TextView) contentView.findViewById(R.id.women_tv);
		man_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commonDialog.cancel();
				userSex = 1;
				sex_tv.setText("男");
				
			}
		});
		women_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				commonDialog.cancel();
				userSex = 2;
				sex_tv.setText("女");
			}
		});

		if (!commonDialog.isShowing()) {
			commonDialog.show();
		}
	}
}
