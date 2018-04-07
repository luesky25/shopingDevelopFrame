package com.android.common.http.request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhy on 15/12/14.
 */
public class PostJsonStringRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("application/json; charset=UTF-8");
    private final String content;

    public PostJsonStringRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String content, int id) {
        super(url, tag, params, headers, id);
        this.content = content;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (content == null) {
            if (params == null) {
                params = new HashMap<>();
            }
            return RequestBody.create(MEDIA_TYPE_PLAIN, new JSONObject(params).toString());
        }
        return RequestBody.create(MEDIA_TYPE_PLAIN, content);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }


}
