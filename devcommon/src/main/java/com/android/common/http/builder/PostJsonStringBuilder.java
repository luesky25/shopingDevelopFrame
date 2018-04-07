package com.android.common.http.builder;

import com.android.common.http.https.HttpsUtils;
import com.android.common.http.request.PostStringRequest;
import com.android.common.http.request.RequestCall;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 */
public class PostJsonStringBuilder extends OkHttpRequestBuilder<PostJsonStringBuilder> implements HasParamsable {

    private boolean hasToken;
    private boolean dateline;


    @Override
    public PostJsonStringBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostJsonStringBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public PostJsonStringBuilder token() {
        hasToken = true;
        return this;
    }

    @Override
    public PostJsonStringBuilder dateline() {
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

        return new PostStringRequest(url, tag, params, headers, new JSONObject(params).toString(), MediaType.parse("application/json; charset=UTF-8"), id).build();
    }


}
