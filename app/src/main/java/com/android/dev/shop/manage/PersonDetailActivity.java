package com.android.dev.shop.manage;

import android.os.Bundle;
import android.util.Log;

import com.android.common.http.utils.PreferencesUtils;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.android.base.BaseWorkerShowFragmentActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018-03-30.
 */

public class PersonDetailActivity extends BaseWorkerShowFragmentActivity {
    public static final String TAG = PersonDetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadMultiFile("");
            }
        }).start();

    }



    private void uploadMultiFile(String url) {
        url = "http://120.78.136.132:8081/jeecg/rest/tsBlackListController/fileUpload";
        File file = new File( "/mnt/sdcard/sample-debug.apk");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("File", "test.jpg", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url).addHeader("X-AUTH-TOKEN",PreferencesUtils.getString(DevApplication.getMyApplication(), PreferenceConfig.X_AUTH_TOKEN))
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","*/*")
                .post(requestBody)
                .build();


        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "uploadMultiFile() e=" + e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "uploadMultiFile() response=" + response.body().string());
            }
        });
    }
}
