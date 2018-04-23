package com.android.dev.shop.android.logic;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018-03-31.
 */

public class ShopBuilder {

    public static String buildShopList(JSONObject data){
        String name = data.optString("createName");

        return name;
    }
}
