package com.android.dev.shop.android.base.temple;

import android.text.TextUtils;


import com.android.common.utils.KGLog;
import com.android.dev.shop.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public abstract class TDataListLogic<D> extends BaseLogic implements JsonCallback {
    /**
     * Logic类自己使用的请求字段
     */
    protected static final int REQUSET_GET_DATA_LIST = 325100;

    /**
     * Logic类提供给外部使用的Callback字段
     */
    public static final int CALLBACK_GET_DATA_LIST_SUCCESS = 32500;

    public static final int CALLBACK_SERVER_ERR = 32501;
    public static final int CALLBACK_NET_ERR = 32502;
    public static final int CALLBACK_NODATA = 32503;
    public static final int CALLBACK_OTHER_ERR = 32504;

    public static final int CALLBACK_CACHE_DATA_LIST_SUCCESS = 32505;


    public TDataListLogic(String tag, LogicCallback callback) {
        super(tag, callback);
    }

    /**
     * 在这里面必须用publish发布网络任务,并且必须带上请求码 CALLBACK_GET_DATA_LIST_SUCCESS
     */
    public abstract void toGetDataList(Object... args);

    /**
     * 针对不同的json格式重写该方法
     *
     * @param response
     * @return
     */
    public UIGeter parseSuccessAndMessage(JSONObject response) {
        return SingJsonHelper.getInstance().parseSuccessAndMessage(response);
    }

    /**
     * 针对不同的json格式重写该方法
     *
     * @param response
     * @return
     */
    public String getResponseData(JSONObject response) {
        return response.optString("data");
    }

    @Override
    public void onResponseJson(JSONObject response, int reqCode) {
        KGLog.d(tag, response.toString());
        switch (reqCode) {
            case REQUSET_GET_DATA_LIST:
                try {
                    UIGeter geter = parseSuccessAndMessage(response);
                    KGLog.d(tag, response.toString());
                    if (geter.isSuccess()) {
                        String data = getResponseData(response);
                        if (TextUtils.isEmpty(data)) {
                            logicCallback(geter.getMessage(), CALLBACK_NODATA);
                            break;
                        }
                        ArrayList<D> channals = parseDataToList(data, geter);
                        if (channals == null) break;
                        if (channals.size() == 0) {
                            logicCallback(geter.getMessage(), CALLBACK_NODATA);
                            break;
                        }
                        geter.setReturnObject(channals);
                        logicCallback(geter, CALLBACK_GET_DATA_LIST_SUCCESS);
                    } else {
                        logicCallback(geter.getMessage(), CALLBACK_OTHER_ERR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    logicCallback(getContextString(R.string.server_err), CALLBACK_SERVER_ERR);
                }
                break;
        }
    }

    protected abstract ArrayList<D> parseDataToList(String data, UIGeter geter) throws JSONException;

    @Override
    public void onErrorResponse(VolleyError error, int reqCode) {
        switch (reqCode) {
            case REQUSET_GET_DATA_LIST:
                switch (error.getType()) {
                    case SERVER:
                        logicCallback(getContextString(R.string.server_err), CALLBACK_SERVER_ERR);
//                        logicCallback(error.getMessage(),CALLBACK_SERVER_ERR);
                        break;
                    case NETWORK:
                        logicCallback(getContextString(R.string.other_net_err), CALLBACK_NET_ERR);
                        break;
                    default:
                        logicCallback("出现了一个错误:类型为:" + error.getType(), CALLBACK_SERVER_ERR);
                        break;
                }
        }
    }

}
