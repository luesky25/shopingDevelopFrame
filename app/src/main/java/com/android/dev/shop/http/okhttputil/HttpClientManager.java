package com.android.dev.shop.http.okhttputil;

import android.util.Log;

import com.android.common.utils.KGLog;
import com.android.common.volley.AuthFailureError;
import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.Request;
import com.android.common.volley.Response;
import com.android.common.volley.VolleyError;
import com.android.common.volley.toolbox.JsonObjectRequest;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.android.base.temple.JsonCallback;
import com.android.dev.shop.http.HttpUtils;
import com.android.dev.shop.http.base.RequestManager;
import com.android.dev.utils.ToolUtils;
import com.android.statistics.NetworkType;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * http管理类，用于列表这些回调的集成
 */
public class HttpClientManager {
    public static final int WIFI_TIME_OUT = KGHttpClient.WIFI_TIME_OUT;
    public static final int WAP_TIME_OUT = KGHttpClient.WAP_TIME_OUT;

    /**
     * 以json格式为参数post请求一个json响应
     */
    public static void addToJsonObjectRequest(final JsonCallback callback, String url, HashMap<String,String> paramsJson, final int reqCode, String reqTag) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (KGLog.isDebug()) {
                    Log.i("http response", "返回:" + response.toString());
                }
                callback.onResponseJson(response, reqCode);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error, reqCode);
                if (KGLog.isDebug()) {
                    Log.i("http err", "请求出错:" + error.getLocalizedMessage());
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HttpUtils.getHeaders();
            }
        };
        req.setTag(reqTag);
        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getMyApplication());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.addRequest(req, reqTag);
    }




}
