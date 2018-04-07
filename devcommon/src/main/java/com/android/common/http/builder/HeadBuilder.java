package com.android.common.http.builder;

import com.android.common.http.OkHttpUtils;
import com.android.common.http.request.OtherRequest;
import com.android.common.http.request.RequestCall;

/**
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
