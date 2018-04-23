package com.android.dev.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.common.http.utils.PreferencesUtils;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.android.activity.CheckActivity;
import com.android.dev.shop.android.eventbus.EventType;
import com.android.dev.shop.android.eventbus.KGEventBus;
import com.android.dev.shop.android.webview.WebViewActivity;

/**
 * Created by Administrator on 2018-03-25.
 */

public class ActivityUtils {
    private static final int flags = -1;
    /**
     * 跳去用户登录页
     *
     * @param formActivity
     * @param RESULT_CODE
     */
    public static void gotoCheckActivityForResult(Activity formActivity, final int RESULT_CODE) {
        Intent intentShare = new Intent(formActivity, CheckActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CheckActivity.LOGIN_INFO, 0);
        if (bundle != null)
            intentShare.putExtras(bundle);
        formActivity.startActivityForResult(intentShare, RESULT_CODE);
    }

    /**
     * 去 活动webView界面
     *
     * @param formActivity
     * @param url          url
     * @param isFinish
     */
    public static void gotoWebViewActivity(Context formActivity, String url, boolean isFinish) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.KEY_URL, url);
        gotoActivity(formActivity, WebViewActivity.class, bundle, isFinish);
    }

    /**
     * 跳去用户登录页
     *
     * @param formActivity
     * @param isLogin      //0---登录；1---忘记密码
     * @param isFinish
     * @param notify       是否刷新界面，用于sesstion过期
     */
    public static void gotoCheckActivity(Context formActivity, int isLogin, boolean notify, boolean isFinish) {
        if (isLogin == 0) {
            PreferencesUtils.putBoolean(formActivity, PreferenceConfig.AUTO_LOGIN, false);
            DevApplication.getMyApplication().setIsGuest(true);
            // 清理session值并初始化网络框架
            PreferencesUtils.putString(formActivity, PreferenceConfig.X_AUTH_TOKEN, null);
            PreferencesUtils.putString(formActivity, PreferenceConfig.LOGIN_USER_ID, null);
            PreferencesUtils.putString(formActivity, PreferenceConfig.LOGIN_ACOUNT, null);
//			PreferencesUtils.putString(formActivity, PreferenceConfig.ACCESS_TOKEN, "");

        }
        if (notify) {
            KGEventBus.post(EventType.EVENT_USER_LOGIN_UPDATE_INFO);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(CheckActivity.LOGIN_INFO, isLogin);
        gotoActivity(formActivity, CheckActivity.class, bundle, isFinish);
    }


    public static void gotoActivity(Context formActivity, Class<?> toActivity, boolean isFinish) {
        gotoActivity(formActivity, toActivity, null, null, flags, isFinish);
    }

    public static void gotoActivity(Context formActivity, Class<?> toActivity, String aciton, boolean isFinish) {
        gotoActivity(formActivity, toActivity, null, aciton, flags, isFinish);
    }

    public static void gotoActivity(Context formActivity, Class<?> toActivity, Bundle bundle, boolean isFinish) {
        gotoActivity(formActivity, toActivity, bundle, null, flags, isFinish);
    }

    public static void gotoActivity(Context formActivity, Class<?> toActivity, Bundle bundle, int flags, boolean isFinish) {
        gotoActivity(formActivity, toActivity, bundle, null, flags, isFinish);
    }

    /**
     * 基础Activity启动类
     *
     * @param formActivity 当前的Activity实例
     * @param toActivity   即将启动的Activity
     * @param bundle       数据包
     * @param flags        启动模式   -1则不添加
     * @param isFinish     启动完成是否结束当前的Activity
     */
    public static void gotoActivity(Context formActivity, Class<?> toActivity, Bundle bundle, String aciton, int flags,
                                    boolean isFinish) {
        Intent intent = new Intent(formActivity, toActivity);
        if (aciton != null)
            intent.setAction(aciton);
        if (bundle != null)
            intent.putExtras(bundle);
        if (!(flags == -1)) {
            intent.addFlags(flags);
        }
        if (!(formActivity instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        formActivity.startActivity(intent);
        if (isFinish) {
            if (formActivity instanceof Activity) {
                Activity activity = (Activity) formActivity;
                activity.finish();
            }
        }
        formActivity = null;
    }
}
