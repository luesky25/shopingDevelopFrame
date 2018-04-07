package com.android.common.http.builder;

import android.net.Uri;

import com.android.common.http.https.HttpsUtils;
import com.android.common.http.request.GetRequest;
import com.android.common.http.request.RequestCall;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhy on 15/12/14.
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable {
    private boolean hasToken;
    private boolean dateline;

    @Override
    public RequestCall build() {
        if (dateline) {
            if (params == null) {
                params = new LinkedHashMap<>();
            }
            params.put("dateline", String.valueOf(System.currentTimeMillis() / 1000));
        }

        if (hasToken) {
            if (params == null) {
                params = new LinkedHashMap<>();
            }
            params.put("token", HttpsUtils.getToken(params));
        }

        if (params != null) {
            url = appendParams(url, params);
        }


        return new GetRequest(url, tag, params, headers, id).build();
    }

    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key,params.get(key));
        }
        return builder.build().toString();
    }


    @Override
    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public GetBuilder token() {
        hasToken = true;
        return this;
    }

    @Override
    public GetBuilder dateline() {
        dateline = true;
        return this;
    }


}
