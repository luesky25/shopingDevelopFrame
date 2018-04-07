package com.android.dev.shop.android.base.temple;

import android.support.annotation.CheckResult;


import org.json.JSONObject;

/**
 * 由 lampartlin 于 2016/2/22 创建.
 */
public class SingJsonHelper {
    private static SingJsonHelper ourInstance = new SingJsonHelper();

    public static SingJsonHelper getInstance() {
        return ourInstance;
    }

    private SingJsonHelper() {
    }

    @CheckResult
    public UIGeter parseSuccessAndMessage(JSONObject responseJson) {
        UIGeter geter = new UIGeter();
        geter.setSuccess(responseJson.optBoolean("success"));
        geter.setMessage(responseJson.optString("message"));
        geter.setReturnCode(responseJson.optInt("code",-1));
        return geter;
    }

    @CheckResult
    public UIGeter parseJson(JSONObject responseJson) {
        UIGeter geter = new UIGeter();
        if (!responseJson.isNull("errcode")) {
            geter.setSuccess(responseJson.optInt("errcode", 0) == 1);
            geter.setReturnCode(responseJson.optInt("errcode", 1));
        }
        geter.setMessage(responseJson.optString("msg"));
        return geter;
    }

    @CheckResult
    public UIGeter parseUgcJson(JSONObject responseJson) {
        UIGeter geter = new UIGeter();
        int code = responseJson.optInt("code", -1);
        geter.setSuccess(code == 0 ? true : false);
        geter.setMessage(responseJson.optString("message"));
        geter.setReturnCode(code);
        return geter;
    }
}
