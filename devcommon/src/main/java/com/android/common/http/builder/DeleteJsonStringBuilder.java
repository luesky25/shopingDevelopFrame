package com.android.common.http.builder;

import com.android.common.http.OkHttpUtils;
import com.android.common.http.https.HttpsUtils;
import com.android.common.http.request.OtherRequest;
import com.android.common.http.request.RequestCall;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 */
public class DeleteJsonStringBuilder extends OkHttpRequestBuilder<DeleteJsonStringBuilder> implements HasParamsable {
    private boolean hasToken;
    private boolean dateline;


    @Override
    public DeleteJsonStringBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public DeleteJsonStringBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public DeleteJsonStringBuilder token() {
        hasToken = true;
        return this;
    }

    @Override
    public DeleteJsonStringBuilder dateline() {
        dateline = true;
        return this;
    }

    @Override
    public RequestCall build() {
        if (params == null) {
            params = new LinkedHashMap<>();
        }

        if (dateline){
            params.put("dateline", String.valueOf(System.currentTimeMillis() / 1000));
        }

        if (hasToken) {
            params.put("token", HttpsUtils.getToken(params));
        }

        OtherRequest request = new OtherRequest(null, new JSONObject(params).toString(), OkHttpUtils.METHOD.DELETE, url, tag, params, headers, id);
        request.setMediaType(MediaType.parse("application/json; charset=UTF-8"));
        return  request.build();
    }

}
