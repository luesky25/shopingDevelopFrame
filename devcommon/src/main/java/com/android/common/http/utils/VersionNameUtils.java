package com.android.common.http.utils;

import android.content.Context;

import com.android.common.utils.KGLog;


/**
 * Created by zhaodehe on 2017/12/1.
 */

public class VersionNameUtils {
    public static final String ReleaseVersionName = "3.9.9";
    public static final String TestVersionName = "3.9.9.0";

    public static String getVersionName(Context context){
        KGLog.MODE = PreferencesUtils.getInt(context,KGLog.ENV_MODE,KGLog.Test_Env);
        switch (KGLog.MODE){
            case KGLog.Test_Env:
                return TestVersionName;
            case KGLog.Release_Env:
                return ReleaseVersionName;
            default:
                return TestVersionName;
        }
    }
}
