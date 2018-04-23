package com.android.dev.shop.android.logic;

import com.android.dev.shop.android.base.temple.TDataListLogic;
import com.android.dev.shop.android.base.temple.UIGeter;
import com.android.dev.shop.android.publisher.ShopPublisher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-03-31.
 */

public class ShopLogic extends TDataListLogic<String> {


    public ShopLogic(String tag, LogicCallback callback) {
        super(tag, callback);
    }

    @Override
    public void toGetDataList(Object... args) {
            ShopPublisher.getInstance().getBlackListTest(REQUSET_GET_DATA_LIST, tag, this);
    }

    @Override
    protected ArrayList parseDataToList(String data, UIGeter geter) throws JSONException {
        ArrayList<String> mliList = new ArrayList<String>();
        if(geter.isSuccess()){
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject item = new JSONObject(arr.get(i).toString());
                mliList.add(ShopBuilder.buildShopList(item));
            }
        }else{
            logicCallback(geter.getMessage(),REQUSET_GET_DATA_LIST);
        }
        return mliList;
    }
}
