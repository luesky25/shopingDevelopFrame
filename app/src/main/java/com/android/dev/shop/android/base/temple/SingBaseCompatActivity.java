package com.android.dev.shop.android.base.temple;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.R;
import com.android.dev.shop.android.activity.CheckActivity;
import com.android.dev.shop.android.eventbus.KGEventBus;
import com.android.dev.shop.dialog.TipsDialog;
import com.android.dev.shop.http.base.RequestManager;
import com.android.dev.utils.DatePref;
import com.android.dev.utils.DateUtil;
import com.android.dev.utils.ImageLoaderUtils;

import java.util.Date;
import java.util.Vector;

/**
 * 新版样式的Activity父类,用于supportv7 22的新api上,可以兼容到api9
 *
 * @author lampartlin
 * @since 2016/1/29
 */
public abstract class SingBaseCompatActivity<L extends BaseLogic> extends AppCompatActivity implements  BaseLogic.LogicCallback {
    public final String TAG = this.getClass().getSimpleName();
    protected boolean mResumed;
    // 数据更新时间
    protected Date mUpdateDate;
    protected AnimationDrawable animaition;
    protected View commonMoreButton;

    protected L mLogic;
    protected ImageView commonTitleBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DevApplication.getMyApplication().addActivity(this);
        KGEventBus.register(this);
        setContentView(createContetntView());
        getCreateData(getIntent());
        mLogic = creatLogic();
        initClass();
        findViews();
        initViews();
        addListeners();
        setAdapter();
        beginAction();
    }

    /**
     * 创建你的逻辑类
     *
     * @return 本Activity所需要用到的逻辑类
     */
    protected abstract L creatLogic();



    /**
     * 这里解析创建时Intent传入的数据
     */
    protected abstract void getCreateData(Intent data);

    /**
     * 设置contentview
     */
    @LayoutRes
    protected abstract int createContetntView();

    /**
     * 创建些额外需要使用的类,比如一些视图的工具类,逻辑相关的类建议到Logic里面创建
     */
    protected abstract void initClass();

    /**
     * 基本只写入findview内容
     */
    protected abstract void findViews();

    /**
     * 基本只写入控件初始化内容
     */
    protected abstract void initViews();

    /**
     * 基本只写入添加监听器内容
     */
    protected abstract void addListeners();

    /**
     * 基本只写入添加适配器内容
     *
     * @deprecated 发现基类这个方法不是必要流程   适用性比较差  不再强制实现并且标识为弃用
     */
    @Deprecated
    protected void setAdapter() {
    }

    ;

    /**
     * 开始进行网络数据或其它页面行动操作
     */
    protected abstract void beginAction();

    @Override
    public abstract void onLogicCallback(UIGeter geter, int callbackCode);


    // 初始化views
    protected void initBaseToolBarView() {
    }




    /**
     * 点击了后退按钮
     */
    protected void onBackClick() {
        finish();
    }

    /**
     * 点击了更多按钮
     */
    protected void onMoreButtonClick() {
    }



    /**
     * 显示一个Toast类型的消息
     *
     * @param msg 显示的消息
     */
    @UiThread
    public void showToast(String msg) {
        Toast mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 显示{@link Toast}通知
     *
     * @param strResId 字符串资源id
     */
    @UiThread
    public void showToast(@StringRes int strResId) {
        Toast mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        mToast.setText(strResId);
        mToast.show();
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        if (SingBaseCompatActivity.this.getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(SingBaseCompatActivity.this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 控制软键盘的显示隐藏
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    @Override
    protected void onDestroy() {
        KGEventBus.unregister(this);
        DevApplication.getMyApplication().finishActivity(this);
     
//        RequestManager.cancelPendingRequests(TAG);
//        DevApplication.getMyApplication().clearMemory();
        super.onDestroy();
        if (mLogic != null) mLogic.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        // 当前显示activity
        DevApplication.getMyApplication().setCurrentActivity(this);
        ImageLoaderUtils.getInstance(DevApplication.getMyApplication()).resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        ImageLoaderUtils.getInstance(DevApplication.getMyApplication()).pause();
    }


    /**
     * 获取类名字
     *
     * @return
     */
    protected String getClassName() {
        return this.getClass().getName();
    }

    protected Date getUpdate() {
        long timestamp = DatePref.getInstance().getUpdateDate(getClassName());
        if (mUpdateDate == null) {
            mUpdateDate = new Date(timestamp);
        } else {
            mUpdateDate.setTime(timestamp);
        }
        return mUpdateDate;
    }

    protected void updateDate() {
        if (mUpdateDate == null)
            mUpdateDate = new Date();
        DatePref.getInstance().setUpdateDate(getClassName(), mUpdateDate.getTime());
    }

    /**
     * 获取时间格式
     *
     * @return
     */
    public String getDateFormat() {
        return String.format(getString(R.string.xlistview_header_last_time),
                DateUtil.twoDateDistance(this, mUpdateDate, new Date()));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                //EventBus.getDefault().post(new UpdateEvent(UpdateEvent.TYPE_UPDATE_DATA));//刷新添加关注页面的状态
                ImageLoaderUtils.getInstance(DevApplication.getInstance()).stop();
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }








    private TipsDialog choiceDialog;

    public void toLogin() {
        if (choiceDialog == null) {
            choiceDialog = new TipsDialog(this).setLeftText("取消").setRightText("登录").setTips("该操作要登录才能使用哦!").setOnLeftClick(new TipsDialog.OnLeftClick() {

                @Override
                public void leftClick() {
                    choiceDialog.cancel();
                }
            }).setOnRightClick(new TipsDialog.OnRightClick() {

                @Override
                public void rightClick() {
                    Intent intent = new Intent(SingBaseCompatActivity.this, CheckActivity.class);
                    startActivity(intent);
                    choiceDialog.cancel();
                }
            });
        }
        choiceDialog.show();
    }

    /**
     * 6.0以上系统需要处理权限问题
     */
    private int WRITE_CAMERA_REQUEST_CODE = 5;//

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Vector<String> v = new Vector();
            if (!selfPermissionGranted(Manifest.permission.BLUETOOTH)) {
                v.add(Manifest.permission.BLUETOOTH);
            }
            if (!selfPermissionGranted(Manifest.permission.GET_TASKS)) {
                v.add(Manifest.permission.GET_TASKS);
            }
            if (!selfPermissionGranted(Manifest.permission.ACCESS_NETWORK_STATE)) {
                v.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if (!selfPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
                v.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!selfPermissionGranted(Manifest.permission.INTERNET)) {
                v.add(Manifest.permission.INTERNET);
            }
            if (!selfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                v.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!selfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                v.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!selfPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                v.add(Manifest.permission.RECORD_AUDIO);
            }
            if (!selfPermissionGranted(Manifest.permission.WAKE_LOCK)) {
                v.add(Manifest.permission.WAKE_LOCK);
            }
            if (!selfPermissionGranted(Manifest.permission.CAMERA)) {
                v.add(Manifest.permission.CAMERA);
            }
            if (!selfPermissionGranted(Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
                v.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }
            if (!selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                v.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (!selfPermissionGranted(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)) {
                v.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
            }
//            if(!selfPermissionGranted(Manifest.permission.SYSTEM_ALERT_WINDOW)){
//                v.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
//            }
            if (!selfPermissionGranted(Manifest.permission.WRITE_SETTINGS)) {
                v.add(Manifest.permission.WRITE_SETTINGS);
            }
            if (!selfPermissionGranted(Manifest.permission.CHANGE_WIFI_STATE)) {
                v.add(Manifest.permission.CHANGE_WIFI_STATE);
            }
            if (!selfPermissionGranted(Manifest.permission.READ_SMS)) {
                v.add(Manifest.permission.READ_SMS);
            }
            if (!selfPermissionGranted(Manifest.permission.RECEIVE_SMS)) {
                v.add(Manifest.permission.RECEIVE_SMS);
            }
            if (!selfPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                v.add(Manifest.permission.READ_CONTACTS);
            }
            String[] strings = new String[v.size()];
            for (int i = 0; i < v.size(); i++) {
                String str = v.get(i);
                if (!TextUtils.isEmpty(str)) {
                    strings[i] = str;
                }
            }
            if (strings.length > 0) {
                ActivityCompat.requestPermissions(this, strings, WRITE_CAMERA_REQUEST_CODE);
            }
            v.clear();
        }
    }

    public boolean selfPermissionGranted(String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        KGLog.d("hzd", "  result " + result + "   permission :" + permission);
        return result;
    }


}
