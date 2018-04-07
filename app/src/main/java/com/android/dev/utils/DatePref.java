package com.android.dev.utils;

import android.content.Context;

import com.android.dev.basics.DevApplication;
import com.android.dev.framework.component.preference.PreferenceOpenHelper;

import java.util.Date;

/**
 * 描述:简单数据保存对象
 * 
 * @author chenjinyuan
 * @since 2013-8-9 上午11:29:51
 */
public class DatePref extends PreferenceOpenHelper {

    private volatile static DatePref mInstance = null;

    private DatePref(Context context, String prefname) {
        super(context, prefname);

    }

    public static DatePref getInstance() {
        if (mInstance == null) {
            synchronized (DatePref.class) {
                if (mInstance == null) {
                    Context context = DevApplication.getMyApplication();
                    String name = context.getPackageName() + "musicbox";
                    mInstance = new DatePref(context, name);
                }
            }
        }

        return mInstance;
    }

    /**
     * 获取更新时间
     * 
     * @return
     */
    public long getUpdateDate(String key) {
        return mInstance.getLong("date[" + key + "]", (new Date()).getTime());
    }

    /**
     * 设置更新时间
     * 
     * @param date
     */
    public void setUpdateDate(String key, long date) {
        mInstance.putLong("date[" + key + "]", date);
    }

    /**
     * 获取更新时间
     * 
     * @return
     */
    public long getMyFragUpdateDate() {
        return mInstance.getLong("myfrag_date", (new Date()).getTime());
    }

    /**
     * 设置更新时间
     * 
     * @param date
     */
    public void setMyFragUpdateDate(long date) {
        mInstance.putLong("myfrag_date", date);
    }
}
