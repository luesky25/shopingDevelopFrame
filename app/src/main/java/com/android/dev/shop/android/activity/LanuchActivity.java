package com.android.dev.shop.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;

import com.android.common.http.utils.PreferencesUtils;
import com.android.dev.basics.DevApplication;
import com.android.dev.framework.component.base.BaseWorkerActivity;
import com.android.dev.shop.MainActivity;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.R;
import com.android.dev.shop.http.biz.HttpManagerFactory;
import com.android.dev.shop.http.biz.HttpUserManager;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;

/**
 * Created by Administrator on 2018-03-31.
 */

public class LanuchActivity extends BaseWorkerActivity implements HttpRequestHelper.IHttpResponseListener<String>{

    public static final int GET_APP_UPDATE_MSG = 0x001;
    private HttpUserManager mHttpUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lanuch);
        mHttpUserManager = (HttpUserManager) getVolleyFactory().getInstance(HttpManagerFactory.HTTP_USER_MANAGER);
        mHttpUserManager.beginLogin("test","123456",this,new HttpMessage(GET_APP_UPDATE_MSG));
    }

    @Override
    protected void handleBackgroundMessage(Message msg) {

    }


    @Override
    public void onHttpSuccess(String data, HttpMessage msg) {
        switch (msg.what){
            case GET_APP_UPDATE_MSG:
                if(!TextUtils.isEmpty(data)){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        finish();
                    }
                    PreferencesUtils.putString(DevApplication.getMyApplication(),PreferenceConfig.X_AUTH_TOKEN,data);
                    Intent i = new Intent(LanuchActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onHttpFail(int errorCode, String errorResponse, HttpMessage msg) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(LanuchActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
