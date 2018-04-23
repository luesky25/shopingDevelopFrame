package com.android.dev.shop.android.publisher;

import com.android.dev.framework.component.preference.UrlPref;
import com.android.dev.shop.android.base.temple.JsonCallback;
import com.android.dev.shop.http.okhttputil.HttpClientManager;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018-03-31.
 */

public class ShopPublisher {

    private static ShopPublisher baseCommonPublisher;

    private ShopPublisher() {
    }

    public static synchronized ShopPublisher getInstance() {
        if (baseCommonPublisher == null) {
            baseCommonPublisher = new ShopPublisher();
        }
        return baseCommonPublisher;
    }

    /**
     * 获取黑名单测试
     *
     */

    public void getBlackListTest(int reqCode, String reqTag, JsonCallback jsonCallback) {
        String url = UrlPref.BLACK_LIST;
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        HttpClientManager.addToJsonObjectRequest(jsonCallback, url, params, reqCode, reqTag);
    }
}
