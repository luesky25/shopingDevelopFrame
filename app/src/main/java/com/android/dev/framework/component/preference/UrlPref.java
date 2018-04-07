
package com.android.dev.framework.component.preference;

import android.content.Context;

import com.android.common.http.utils.PreferencesUtils;
import com.android.common.utils.KGLog;
import com.android.dev.basics.DevApplication;


public class UrlPref extends PreferenceOpenHelper {

    public static final String Release_Url = "http://120.78.136.132:8081";
    public static final String Test_Url = "http://120.78.136.132:8081";

    private static UrlPref mUrlPref;

    private Context mContext;

    public UrlPref(Context context, String prefname) {
        super(context, prefname);
        mContext = context;
    }

    public synchronized static UrlPref getInstance() {
        if (mUrlPref == null) {
            Context context = DevApplication.getInstance().getApplicationContext();
            String name = context.getPackageName() + "url";
            mUrlPref = new UrlPref(context, name);
        }
        return mUrlPref;
    }

    public static String getBaseUrl(){
        if(KGLog.isDebug()){
            KGLog.MODE = PreferencesUtils.getInt(DevApplication.getInstance().getApplicationContext(),KGLog.ENV_MODE,KGLog.Test_Env);
            switch (KGLog.MODE){
                case KGLog.Test_Env:
                    return Test_Url;
                case KGLog.Release_Env:
                    return Release_Url;
                default:
                    return Test_Url;
            }
        }else{
            return Release_Url;
        }

    }


    /**
     * 登录url
     */
    public final static String LOGIN_LOCAL= getBaseUrl() + "/jeecg/rest/tokens";
}
