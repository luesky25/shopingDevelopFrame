package com.android.dev.shop.http.okhttputil;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.common.utils.KGLog;
import com.android.dev.framework.component.base.BaseApplication;
import com.android.dev.utils.ToolUtils;
import com.android.statistics.NetworkType;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.framed.Header;
import okhttp3.internal.http.HttpMethod;

/**
 * HTTP请求客户端
 *
 * @author chenys
 */
public class KGHttpClient {
    public static final int FILE_TIME_OUT = 120 * 1000;
    public static final int WIFI_TIME_OUT = FILE_TIME_OUT;
    public static final int WAP_TIME_OUT = FILE_TIME_OUT;
    private final static String TAG = "KGHttpClient";

    // 最大重试次数
    private final static int MAX_RETRY_NUM = 2;



//    /**
//     * 资源包下载
//     *
//     * @param requestPackage
//     * @throws AppException
//     */
//    public static void download(RequestPackage requestPackage,
//                                IDownloadResListener downloadResListener) throws AppException {
//        HttpClient httpClient = null;
//        HttpMethod httpMethod = null;
//
//        long fileSize = 0;
//        try {
//            httpClient = createHttpClient(requestPackage);
//            httpMethod = createHttpMethod(requestPackage);
//            int statusCode = httpClient.executeMethod(httpMethod);
//            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED
//                    && statusCode != HttpStatus.SC_NO_CONTENT
//                    && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {
//                throw AppException.http(statusCode);
//            }
//
//            downloadResListener.onDownloadStart(httpMethod);
//            Header header = httpMethod.getResponseHeader("Content-Length");
//            try {
//                fileSize = Long.parseLong(header.getValue());
//            } catch (Exception e) {
//            }
//            BufferedInputStream bis = new BufferedInputStream(httpMethod.getResponseBodyAsStream());
//            byte[] data = new byte[8 * 1024];
//            long haveRead = 0;
//            int read = 0;
//            int progress = 0;
//            while ((read = bis.read(data)) != -1) {
//                haveRead += read;
//                if (fileSize > 0) {
//                    progress = (int) ((float) haveRead / fileSize * 100);
//                }
//                downloadResListener.onProgressChanged(data, 0, read, progress);
//            }
//            downloadResListener.onProgressFinish();
//        } catch (HttpException e) {
//            // 发生致命的异常，可能是协议不对或者返回的内容有问题
//            // e.printStackTrace();
//            throw AppException.http(e);
//        } catch (IOException e) {
//            // 发生网络异常
//            // e.printStackTrace();
//            throw AppException.network(e);
//        } catch (Exception e) {
//            // 保存数据时异常
//            // e.printStackTrace();
//            throw AppException.run(e);
//        } finally {
//            if (httpMethod != null) {
//                // 释放连接
//                httpMethod.releaseConnection();
//            }
//            httpClient = null;
//        }
//    }



}
