package com.android.dev.shop.android.base.temple;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;


import com.android.common.volley.VolleyError;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.R;

import java.lang.ref.WeakReference;

public class BaseLogic {

    protected String tag = "noTag";
    private WeakReference<LogicCallback> mCallback;
    protected static Handler mainHandler = new Handler(Looper.getMainLooper());

    public BaseLogic() {
    }

    public BaseLogic(String tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setmCallback(LogicCallback callback) {
        this.mCallback = new WeakReference<>(callback);
    }

    public BaseLogic(String tag, LogicCallback callback) {
        this.tag = tag;
        this.mCallback = new WeakReference<>(callback);
    }

    /**
     * logic回调到UI线程中
     *
     * @param geter        回调载体 {@link UIGeter}
     * @param callbackCode 回调码 用于区分回调内容
     */
    public void logicCallback(final UIGeter geter, final int callbackCode) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null && mCallback.get() != null) {
                    mCallback.get().onLogicCallback(geter, callbackCode);
                }
            }
        });
    }

    /**
     * 向UI层回调一个只带有一个文案message的{@link UIGeter}
     * <p>
     * 参考{@link BaseLogic#logicCallback(UIGeter, int)}
     *
     * @param message      回调文案
     * @param callbackCode 回调码 用于区分回调内容
     */
    public void logicCallback(final String message, int callbackCode) {
        UIGeter geter = new UIGeter();
        geter.setMessage(message);
        logicCallback(geter, callbackCode);
    }

    /**
     * 向UI层回调
     */
    public void logicCallback(final int callbackCode) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null && mCallback.get() != null) {
                    mCallback.get().onLogicCallback(null, callbackCode);
                }
            }
        });
    }

    /**
     * 根据resid获取对应文字
     *
     * @param id String资源id
     */
    public static String getContextString(@StringRes int id) {
        return DevApplication.getInstance().getResources().getString(id);
    }

    public static String getCommonErrString(VolleyError volleyError) {
        switch (volleyError.getType()) {
            case SERVER:
                return getContextString(R.string.server_err);
            case NETWORK:
                return getContextString(R.string.other_net_err);
            default:
                return "出现了一个错误:类型为:" + volleyError.getType();
        }
    }

    public interface LogicCallback {
        /**
         * 逻辑层回调到UI的函数
         *
         * @param geter
         * @param callbackCode
         */
        void onLogicCallback(UIGeter geter, int callbackCode);
    }

    public void onDestroy() {
        if (mainHandler != null) mainHandler.removeCallbacksAndMessages(null);
        if (mCallback != null) mCallback.clear();
        mCallback = null;
    }
}
