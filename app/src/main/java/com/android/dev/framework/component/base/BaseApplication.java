
package com.android.dev.framework.component.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * 描述:全局Application
 *
 * @author chenys
 * @since 2013-8-7 下午6:16:10
 */
public abstract class BaseApplication extends Application {

    protected static BaseApplication mApplication;

    // 应用全局变量存储在这里
    private static Hashtable<String, Object> mAppParamsHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mAppParamsHolder = new Hashtable<String, Object>();
    }

    /**
     * 获取Application实例
     *
     * @return
     */
    public static BaseApplication getInstance() {
        if (mApplication == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mApplication;
    }

    /**
     * 存储全局数据
     *
     * @param key
     * @param value
     */
    public void putValue(String key, Object value) {
        mAppParamsHolder.put(key, value);
    }

    /**
     * 获取全局数据
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return mAppParamsHolder.get(key);
    }

    /**
     * 是否已存放
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return mAppParamsHolder.containsKey(key);
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    private HashMap<String, Long> categoryTime = new HashMap<String, Long>();

    public Long getCategoryTime(String key) {
        return categoryTime.get(key) == null ? 0 : categoryTime.get(key);
    }

    public void putCategoryTime(String key, long value) {
        categoryTime.put(key, value);
    }


}
