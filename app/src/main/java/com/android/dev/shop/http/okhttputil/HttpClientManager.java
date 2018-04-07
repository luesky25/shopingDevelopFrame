package com.android.dev.shop.http;

import android.net.Uri;
import android.util.Log;

import com.android.common.utils.KGLog;
import com.android.common.volley.DefaultRetryPolicy;
import com.android.common.volley.Request;
import com.android.common.volley.Response;
import com.android.common.volley.VolleyError;
import com.android.common.volley.toolbox.JsonObjectRequest;
import com.android.dev.basics.DevApplication;
import com.android.dev.shop.android.base.temple.JsonCallback;
import com.android.dev.utils.ToolUtils;
import com.android.internal.http.multipart.MultipartEntity;
import com.android.statistics.NetworkType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.internal.http.CacheRequest;

import static com.android.dev.shop.http.base.HttpErrorCode.AuthFailureError;

public class HttpClientManager {
    public static final int WIFI_TIME_OUT = KGHttpClient.WIFI_TIME_OUT;
    public static final int WAP_TIME_OUT = KGHttpClient.WAP_TIME_OUT;

    /**
     * 以json格式为参数post请求一个json响应
     */
    public static void addToJsonObjectRequest(final JsonCallback callback, String url, JSONObject paramsJson, int reqCode, String reqTag) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, paramsJson, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response, int reqCode) {
                if (KGLog.isDebug()) {
                    Log.i("http response", "返回:" + response.toString());
                }
                callback.onResponseJson(response, reqCode);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                callback.onErrorResponse(error, reqCode);
                if (KGLog.isDebug()) {
                    Log.i("http err", "请求出错:" + error.getLocalizedMessage());
                }
            }
        }, reqCode) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
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

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 以json格式为参数post请求一个json响应
     */
    public static void addToJsonObjectRequest(final JsonCallback callback, String url, JSONArray paramsJson, int reqCode, String reqTag) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, paramsJson, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response, int reqCode) {
                if (KGLog.isDebug()) {
                    Log.i("http response", "返回:" + response.toString());
                }
                callback.onResponseJson(response, reqCode);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                callback.onErrorResponse(error, reqCode);
                if (KGLog.isDebug()) {
                    Log.i("http err", "请求出错:" + error.getLocalizedMessage());
                }
            }
        }, reqCode) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
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

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 文件上传
     */
    public static void addToMultipartRequest(final JsonCallback callback, String url, MultipartEntity entity, int reqCode, String reqTag) {
        MultipartRequest req = new MultipartRequest(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                callback.onErrorResponse(error, reqCode);
                MultipartRequest d;
            }
        }, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response, int reqCode) {
                callback.onResponseJson(response, reqCode);
            }
        }, entity, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };


        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getMyApplication());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setTag(reqTag);
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 文件上传,带进度回调
     */
    public static void addToMultipartProgressRequest(final JsonCallback callback, String url, MultipartEntity entity, ProgressRequestListener progressListener, int reqCode, String reqTag) {
        MultipartRequest req = new MultipartRequest(url, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                callback.onErrorResponse(error, reqCode);
            }
        }, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response, int reqCode) {
                callback.onResponseJson(response, reqCode);
            }
        }, entity, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };
        req.setProgressRequestListener(progressListener);
        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getMyApplication());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setTag(reqTag);
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 以get方式请求一个json响应
     */
    public static void addToJsonObjectGetRequest(final JsonCallback callback, String url, Map<String, String> params, int reqCode, String reqTag) {
        addToJsonObjectGetRequest(callback, url, params, reqCode, reqTag, 0);
    }

    /**
     * 以get方式请求一个json响应
     */
    public static void addToJsonObjectGetRequest(final JsonCallback callback, final String url, Map<String, String> params, int reqCode, String reqTag, int retry) {
        Uri uri = Uri.parse(url);

        if (params != null) {
            Uri.Builder uriBuilder = uri.buildUpon();
            for (String key : params.keySet()) {
                uriBuilder.appendQueryParameter(key, params.get(key));
            }
            uri = uriBuilder.build();
        }

        if (params != null && DevApplication.getMyApplication() != null) {
            try {
                params.put("version", ToolUtils.getVersionName(DevApplication.getMyApplication()));
            } catch (Exception e) {
            }
        }

        KGLog.d("requrl", uri.toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, uri.toString(), new JSONObject(), new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response, int reqCode) {
                KGLog.d("http response", "返回:" + response.toString());
                callback.onResponseJson(response, reqCode);
            }

        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                KGLog.d("http err", "请求出错:" + error.toString());
                KGLog.d("http err", "请求出错  url:" + url);
                callback.onErrorResponse(error, reqCode);
            }
        }, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
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
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, retry, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 以get方式请求一个jsonArr响应
     */
    public static void addToJsonArrGetRequest(final JsonArrCallback callback, String url, Map<String, String> params, int reqCode, String reqTag) {
        addToJsonArrGetRequest(callback, url, params, reqCode, reqTag, 0);
    }

    /**
     * 以get方式请求一个jsonArr响应
     */
    public static void addToJsonArrGetRequest(final JsonArrCallback callback, String url, Map<String, String> params, int reqCode, String reqTag, int retry) {
        Uri uri = Uri.parse(url);

        if (params != null) {
            Uri.Builder uriBuilder = uri.buildUpon();
            for (String key : params.keySet()) {
                uriBuilder.appendQueryParameter(key, params.get(key));
            }
            uri = uriBuilder.build();
        }

        if (params != null && DevApplication.getMyApplication() != null) {
            try {
                params.put("version", ToolUtils.getVersionName(DevApplication.getMyApplication()));
            } catch (Exception e) {
            }
        }

        Log.i("requrl", uri.toString());

        JsonArrayRequest req;
        req = new JsonArrayRequest(uri.toString(), new Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response, int reqCode) {
                if (KGLog.isDebug()) {
                    Log.i("http response", "返回:" + response.toString());
                }
                callback.onResponseJson(response, reqCode);
            }

        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                if (KGLog.isDebug()) {
                    Log.i("http err", "请求出错:" + error.toString());
                }
                callback.onErrorResponse(error, reqCode);
            }
        }, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
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
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, retry, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    public static void addToJsonObjectPostRequest(final JsonCallback callback, String url, Map<String, String> params, int reqCode, String reqTag) {
        addToJsonObjectPostRequestWithTimeOut(callback, url, params, reqCode, reqTag, 0, 0);
    }


    public static void addToJsonObjectPostRequestWithTimeOut(final JsonCallback callback, String url, Map<String, String> params, int reqCode, String reqTag, int wifiTimeOut, int wapTimeout) {

        if (KGLog.isDebug()) {
            Uri uri = Uri.parse(url);
            if (params != null && params.size() > 0) {
                Uri.Builder uriBuilder = uri.buildUpon();
                for (String key : params.keySet()) {
                    uriBuilder.appendQueryParameter(key, params.get(key));
                }
                uri = uriBuilder.build();
            }
            Log.i("requrl", uri.toString());
        }
        NormalPostRequest req = new NormalPostRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response, int reqCode) {
                        if (KGLog.isDebug()) {
                            Log.i("http response", "返回:" + response.toString());
                        }
                        if (callback != null) {
                            callback.onResponseJson(response, reqCode);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                if (KGLog.isDebug()) {
                    Log.i("http err", "请求出错:" + error.toString());
                }
                if (callback != null) {
                    callback.onErrorResponse(error, reqCode);
                }
            }
        }, params, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };

        req.setTag(reqTag);
        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getMyApplication());
        if (NetworkType.WIFI.equals(networkType)) {
            if (wifiTimeOut == 0) {
                timeout = WIFI_TIME_OUT;
            } else {
                timeout = wifiTimeOut;
            }
        } else {
            if (wapTimeout == 0) {
                timeout = WAP_TIME_OUT;
            } else {
                timeout = wapTimeout;
            }
        }
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }


    /**
     * 还处于测试阶段
     * 以get方式请求一个json响应 允许从缓存中获取文件
     */
    public static void addToJsonObjectGetToCacheRequest(final JsonCallback callback, String url, Map<String, String> params, int reqCode, String reqTag) {
        Uri uri = Uri.parse(url);
        if (params != null && params.size() > 0) {
            Uri.Builder uriBuilder = uri.buildUpon();
            for (String key : params.keySet()) {
                uriBuilder.appendQueryParameter(key, params.get(key));
            }
            uri = uriBuilder.build();
        }
        Log.i("requrl", uri.toString());
        final String urls = uri.toString();
        JsonObjectRequest req;
        req = new JsonObjectRequest(Request.Method.GET, urls, new JSONObject(), new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response, int reqCode) {
                Log.i("http response", "返回:" + response.toString());
                callback.onResponseJson(response, reqCode);
            }

        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                if (DevApplication.getRequestQueenManager().getRequestQueue().getCache() != null
                        && DevApplication.getRequestQueenManager().getRequestQueue().getCache().get(urls) != null
                        && DevApplication.getRequestQueenManager().getRequestQueue().getCache().get(urls).data != null
                        ) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(DevApplication.getRequestQueenManager().getRequestQueue().getCache().get(urls).data));
                        callback.onResponseJson(jsonObject, reqCode);
                        Log.i("http err", "请求出错:缓存中获取了数据");
                        return;
                    } catch (JSONException e2) {
                        Log.i("http err", "请求出错:JSONException" + e2.getLocalizedMessage());
                    }
                } else {
                    Log.i("http err", "没有缓存");
                }
                Log.i("http err", "请求出错:" + error.toString() + "<----->url=" + urls);
                callback.onErrorResponse(error, reqCode);
            }
        }, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
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

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 上传文件
     *
     * @param callback
     * @param url
     * @param asyncBodyInterface
     * @param progressListener
     * @param reqCode
     * @param reqTag
     */
    public static void addToNameValuePostRequest(final JsonCallback callback, String url, AsyncBodyInterface asyncBodyInterface, ProgressRequestListener progressListener, int reqCode, String reqTag) {
        NameValuePostRequest req = new NameValuePostRequest(url, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                KGLog.d("upload", "返回错误");
                callback.onErrorResponse(error, reqCode);
            }
        }, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response, int reqCode) {
                KGLog.d("upload", "正常返回");
                callback.onResponseJson(response, reqCode);
            }
        }, asyncBodyInterface, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };
        req.setProgressRequestListener(progressListener);

        req.setTag(reqTag);
        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getInstance());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KGLog.d("upload", "发送");
        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    public static void addToNameValueGetRequest(final JsonCallback callback, String url, AsyncBodyInterface asyncBodyInterface, ProgressRequestListener progressListener, int reqCode, String reqTag) {
        NameValueGetRequest req = new NameValueGetRequest(url, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                KGLog.d("upload", "返回错误");
                callback.onErrorResponse(error, reqCode);
            }
        }, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response, int reqCode) {
                KGLog.d("upload", "正常返回");
                callback.onResponseJson(response, reqCode);
            }
        }, asyncBodyInterface, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };
        req.setProgressRequestListener(progressListener);

        req.setTag(reqTag);
        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getInstance());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KGLog.d("upload", "发送");
        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }

    /**
     * 下载文件
     *
     * @param callback
     * @param url
     * @param progressListener
     * @param path
     * @param reqCode
     * @param reqTag
     */
    public static void addToDownloadRequest(final FileDownloadCallback callback, String url, ProgressResponseListener progressListener, String path, int reqCode, String reqTag) {
        DownloadRequest req = new DownloadRequest(url, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                KGLog.d("upload", "返回错误");
                callback.onErrorResponse(error, reqCode);
            }
        }, path, progressListener, callback, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };

        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getInstance());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setTag(reqTag);
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KGLog.d("upload", "发送");
        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }


    /**
     * 带缓存的网络请求
     *
     * @param callback
     * @param url
     * @param params
     * @param reqCode
     * @param reqTag
     */
    public static void addToJsonObjectGetCacheRequest(final JsonCallback callback, final CacheCallback cacheCallback, final String url, Map<String, String> params, int reqCode, final String reqTag) {
        if (callback == null) {
            addToJsonObjectGetRequest(callback, url, params, reqCode, reqTag);
            return;
        }
        Uri uri = Uri.parse(url);

        if (params != null) {
            Uri.Builder uriBuilder = uri.buildUpon();
            for (String key : params.keySet()) {
                uriBuilder.appendQueryParameter(key, params.get(key));
            }
            uri = uriBuilder.build();
        }
        final Uri u = uri;

        if (params != null && DevApplication.getMyApplication() != null) {
            try {
                params.put("version", ToolUtils.getVersionName(DevApplication.getMyApplication()));
            } catch (Exception e) {
            }
        }

        KGLog.d("requrl", uri.toString());

        CacheRequest req = new CacheRequest(uri.toString(), new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response, int reqCode) {

                KGLog.d("http response", "返回:" + response.toString());
                callback.onResponseJson(response, reqCode);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error, int reqCode) {
                KGLog.d("http err", "请求出错:" + error.toString());
                KGLog.d("http err", "请求出错  url:" + url);
                callback.onErrorResponse(error, reqCode);
            }
        }, new Response.CacheListener<JSONObject>() {
            @Override
            public void cacheListener(JSONObject response, int requestCode) {
                if (cacheCallback != null) cacheCallback.onCacheResponseJson(response, requestCode);
            }
        }, reqCode) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderSettingUtls.getHeaders();
            }
        };
        req.isConnectivity = DevApplication.getInstance().isConnectivity;
        req.setTag(reqTag);
        int timeout;
        String networkType = ToolUtils.getNetworkType(DevApplication.getMyApplication());
        if (NetworkType.WIFI.equals(networkType)) {
            timeout = WIFI_TIME_OUT;
        } else {
            timeout = WAP_TIME_OUT;
        }
        req.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        DevApplication.getRequestQueenManager().addToRequestQueue(req, reqTag);
    }


}
