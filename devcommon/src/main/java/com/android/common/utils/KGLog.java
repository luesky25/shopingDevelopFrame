package com.android.common.utils;

import android.util.Log;

/**
 * 日志工具
 *
 * @author ldx
 */
public class KGLog {

    private static final String TAG = "KGLog";


    private static boolean isDebug = true;

    public static int MODE = 2;//0 测试环境 1、预发布环境  2、正式环境
    /**正式环境*/
    public static final int Release_Env = 1;
    /**测试环境*/
    public static final int Test_Env = 0;

    /**当前环境*/
    public static final String ENV_MODE = "env_mode";


    /**
     * 是否处于调试模式
     *
     */
    public static boolean isDebug() {
        return isDebug;
    }



    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

}
