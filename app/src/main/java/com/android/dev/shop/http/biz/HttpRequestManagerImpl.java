package com.android.dev.shop.http.biz;

import com.android.common.utils.KGLog;
import com.android.common.volley.Request;
import com.android.common.volley.VolleyError;
import com.android.dev.framework.component.preference.UrlPref;
import com.android.dev.shop.http.HttpUtils;
import com.android.dev.shop.http.base.HttpErrorCode;
import com.android.dev.shop.http.base.RequestManager;
import com.android.dev.shop.http.framework.HttpMessage;
import com.android.dev.shop.http.framework.HttpRequestHelper;
import com.android.dev.shop.http.framework.HttpStringRequest;
import com.android.dev.utils.MD5Utils;
import com.android.dev.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-03-12.
 */

public class HttpRequestManagerImpl implements IHttpUploadManager,IHttpUserManager{
    private static HttpRequestManagerImpl manager;

    private HttpRequestManagerImpl() {
        HttpRequestHelper.initDefaultHeader(getDefaultHeader());
        HttpRequestHelper.initCommonHttpResponseDeal(getCommonDeal());
    };

    public synchronized static HttpRequestManagerImpl getInstance() {
        if (manager == null) {
            manager = new HttpRequestManagerImpl();
        }
        return manager;
    }

    public static void reset() {
        if (manager != null) {
            HttpRequestHelper.initDefaultHeader(getDefaultHeader());
        }
    }

    /**************************** 通用操作函数 ***********************************/
    public void addRequest(Request<?> request, Object tag) {
        RequestManager.addRequest(request, tag);
    }

    public void cancelAll(String tag) {
        RequestManager.cancelAll(tag);
    }

    /**
     * 获取默认的网络请求头部
     */
    private static Map<String, String> getDefaultHeader() {
        return HttpUtils.getHeaders();
    }

    /**
     * 通用网络业务处理
     */
    private HttpRequestHelper.ICommonHttpResponseDeal getCommonDeal() {
        HttpRequestHelper.ICommonHttpResponseDeal mCommonDeal;
        mCommonDeal = new HttpRequestHelper.ICommonHttpResponseDeal() {
            @Override
            public void dealSuccess(Object data, int tag) {

            }

            // 通用业务错误处理------每个网络请求错误都会走这个逻辑
            @Override
            public void dealError(VolleyError error, int errorCode, String errorMessage, int tag) {
                KGLog.e("http_volley", "errorCode : " + errorCode + " ,errorMessage : " + errorMessage);

                int customErrorCode = -1;
                if (errorCode == HttpErrorCode.BadRequest) {
                    try {
                        JSONObject obj = new JSONObject(errorMessage);
                        customErrorCode = obj.getInt("code");
                        // 400情况，细化处理
                        errorCode = customErrorCode;
                    } catch (Exception e) {
                    }
                }
                if (error.networkResponse==null){
                    return;
                }
                switch (error.networkResponse.statusCode) {//403
                    case HttpErrorCode.Forbidden:
                        // session过期，重新初始化默认头部
//                        KGLog.e("http_volley", "error code = " + errorCode + " ,session 过期，重新实例化默认请求头部，刷新session值");
//                        HttpRequestHelper.initDefaultHeader(getDefaultHeader());
//                        EventMessage eventMsg = new EventMessage(EventType.EVENT_SESSION_FAIL_TO_LOGIN);
////					// 由于session过期会相互冲突，所以采用错误码作为toast的tag
//                        eventMsg.arg1 = HttpErrorCode.ExperationError;
//                        KGEventBus.post(eventMsg);
                        break;
//				default:
//					// 在default分支，补充处理那些400到600的服务器异常码，增加toast打印
//					// HttpErrorCode.BadRequest(400) 这一边是等号的原因是：
//					// 增加处理处理400但是errorMessage为空或者没有内容的那些情况
//					if (ErrorCodeUtil.isOtherServerError(errorCode)) {
//						KGLog.e("error", "into default branch errorCode=" + errorCode);
//					}
                }
            }
        };
        return mCommonDeal;
    }

    private Map<String, String> getParamsMap() {
        Map<String, String> map = new HashMap<String, String>();
        //map.put("dateline", getDateline());
        return map;
    }

    private String getDateline() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取数字签名
     * @param bracketAsArray   true---[xx]的内容是数组不计算在md5里面，false---[xxxx]内容是报文，计算在md5里面
     * @return
     */
    private void dealToken(Map<String, String> map,boolean bracketAsArray) {
        List<String> list = new ArrayList<String>();
        for (String str : map.keySet()) {
            // 如[xx,xx,xxx]类型数据，不进行MD5，兼容IOS
            if (bracketAsArray
                    &&map.get(str).startsWith("[") && map.get(str).endsWith("]"))
                continue;
            list.add(str + map.get(str));
        }
        // 按字典序排序请求参数
        Collections.sort(list);
        StringBuilder builder = new StringBuilder(MD5Utils.TOKEN_KEY);
        for (String str : list) {
            // builder.append(StringUtils.string2Unicode(str));
            builder.append(str); // 解决token验证问题
        }
        builder.append(MD5Utils.TOKEN_KEY);
        String result = MD5Utils.getMD5ofStr(builder.toString());
        map.put("token", result);
        KGLog.d("http_volley", "token处理成功" + map.toString());
    }

    /**
     * 获取数字签名
     * 默认[xx]的内容是数组不计算在md5里面
     * @return
     */
    private void dealToken(Map<String, String> map) {
        dealToken(map, true);
    }

    /**
     * 拼接网络请求GET的参数串
     */
    public String getGetParamsString(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        if (map.size() == 0)
            return "";
        builder.append("?");
        for (String key : map.keySet()) {
            builder.append(key).append("=").append(map.get(key)).append("&");
        }
        builder.append("&&&");
        return builder.toString().replace("&&&&", "");
    }

    /**
     * 拼接网络请求的BOBY体字符串
     *
     * @param bracketAsArray true---[xx]的内容是数组不加""，false---[xxxx]内容是报文，加""
     * @return
     */
    public static String getPostBobyString(Map<String, String> map,boolean bracketAsArray) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String key : map.keySet()) {
            if (bracketAsArray
                    &&map.get(key).startsWith("[") && map.get(key).endsWith("]"))
                builder.append("\"").append(key).append("\":").append(StringUtils.uniCodeEncode(map.get(key)))
                        .append(",");
            else
                builder.append("\"").append(key).append("\":\"").append(StringUtils.uniCodeEncode(map.get(key)))
                        .append("\"").append(",");
        }
        builder.append("}");
        return builder.toString().replace(",}", "}");
    }

    /**
     * 用户输入的时候，加上英文引号的判断  拼接网络请求的BOBY体字符串
     *
     * @param bracketAsArray true---[xx]的内容是数组不加""，false---[xxxx]内容是报文，加""
     * @return
     */
    public static String getPostBobyStringByEdit(Map<String, String> map,boolean bracketAsArray) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String key : map.keySet()) {
            if (bracketAsArray
                    &&map.get(key).startsWith("[") && map.get(key).endsWith("]"))
                builder.append("\"").append(key).append("\":").append(StringUtils.uniCodeEncodeEditText(map.get(key)))
                        .append(",");
            else
                builder.append("\"").append(key).append("\":\"").append(StringUtils.uniCodeEncodeEditText(map.get(key)))
                        .append("\"").append(",");
        }
        builder.append("}");
        return builder.toString().replace(",}", "}");
    }

    /**
     * 拼接网络请求的BOBY体字符串
     * 默认[xx]的内容当做数组处理不加""
     * @return
     */
    public static String getPostBobyString(Map<String, String> map) {
        return getPostBobyString(map, true);
    }

    /**
     * 拼接网络请求的BOBY体字符串二进制
     *
     * @return
     */
    public static byte[] getPostBobyByte(Map<String, String> map) {
        return getPostBobyString(map).getBytes();
    }


    /***************************** 业务接口实现 ************************************************/
    @Override
    public HttpStringRequest beginLogin(String userName, String password, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
        final Map<String, String> map = getParamsMap();
        map.put("username", userName);
        map.put("password", password);
        final String url = UrlPref.LOGIN_LOCAL+getGetParamsString(map);
        return HttpRequestHelper.getRequestUsingString(HttpRequestHelper.POST, url, null, null,
                 listener, msg);
    }

    @Override
    public HttpStringRequest getCheckNum(String phone, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
        return null;
    }

    @Override
    public HttpStringRequest registerUser(String phone, String checkCode, String password, String imageHash, String imageName, String nickname, String user_sex, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
        return null;
    }

    @Override
    public HttpStringRequest resetPassword(String phone, String checkCode, String newPassword, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
        return null;
    }

    @Override
    public HttpStringRequest getPasswdSendCode(String phone, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
        return null;
    }

    @Override
    public Request<?> upLoadImageWithThumbCloud(String filePath, HttpRequestHelper.IHttpResponseListener<String> listener, HttpMessage msg) {
        return null;
    }



}
