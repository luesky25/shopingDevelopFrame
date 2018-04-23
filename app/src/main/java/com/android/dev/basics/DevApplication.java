package com.android.dev.basics;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Process;

import com.android.common.http.utils.PreferencesUtils;
import com.android.common.utils.KGLog;
import com.android.dev.framework.component.base.BaseApplication;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.android.model.User;
import com.android.dev.shop.http.base.RequestManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2018-03-11.
 */

public class DevApplication extends BaseApplication {
   static DevApplication app;
    /**
     * 是否是游客(true 表示游客，false表示是用户)
     */
    private boolean mIsGuest = true;

    //当前用户资料，可作为全局变量
    private User mUserData;

    private Activity mCurrentActivity;//当前显示activity

    private HashMap<String, Activity> activityStack = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        init();
    }

    private void init(){
        RequestManager.init(this);
//        initXinge();
    }

    public static DevApplication getMyApplication(){
        if(app==null){
            return null;
        }
        return app;
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isGuest() {
        return mIsGuest;
    }

    public void setIsGuest(boolean mIsGuest) {
        this.mIsGuest = mIsGuest;
    }

    public void setUserData(User data) {

    }

    public User getUserData() {
        //非游客状态才会刷新数据
        if(mUserData == null && PreferencesUtils.getBoolean(this, PreferenceConfig.AUTO_LOGIN, false)){
//            String key = PreferencesUtils.getString(this, PreferenceConfig.LOGIN_ACOUNT);
            String key = PreferencesUtils.getString(this, PreferenceConfig.USER_KEY);
//            mUserData = UserDBManager.getInstance().getUserData(key);
        }
        return mUserData;
    }

    // 添加Activity
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new HashMap<>();
        }
        if (!activityStack.containsKey(activity.getComponentName().getClassName())) {
            activityStack.put(activity.getComponentName().getClassName(), activity);
        }

    }

    //移除指定的Activity
    public void finishActivity(Activity activity) {
        try {
            if (activity != null && activityStack != null && activityStack.size() > 0
                    && activityStack.containsKey(activity.getComponentName().getClassName())) {
                activityStack.remove(activity.getComponentName().getClassName());
            }
        } catch (Exception e) {
        }
    }

    public void crash() {
        Set<String> set = activityStack.keySet();
        for (String s : set) {
            if (s != null && activityStack.containsKey(s)) {
                Activity activity = activityStack.get(s);
                if (activity != null) {
                    activity.finish();
                    KGLog.d("lcxx", "移除Activity" + activity.getClass().getName());
                }
            }
        }
        activityStack.clear();
    }


    /**
     * 删除所有的Activity
     */
    public void finishAllActivity() {
        crash();
        //int fore_pid= DatePref.getInstance().getInt(Constant.SING_FORE_PID, 0);
        int fore_pid = Process.myPid();
        if (fore_pid > 0) {
            //杀死前台进程
            KGLog.e("infox", "前台进程:" + fore_pid);
            killPro(fore_pid);
        }
    }

    private void killPro(int pid) {
        //kill ，主要是为了下一次 打开应用时，能够执行onCreate
        Process.killProcess(pid);
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void appExit(){
        finishAllActivity();
        XGPushManager.unregisterPush(this);
    }

    private void initXinge(){
         XGPushConfig.enableDebug(this,KGLog.isDebug());
        //token注册
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
            //token在设备卸载重装的时候有可能会变
                PreferencesUtils.putString(getApplicationContext(),PreferenceConfig.XINGE_PHONE_TOKEN, (String) data);
                KGLog.d("TPush", "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                KGLog.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }
}
