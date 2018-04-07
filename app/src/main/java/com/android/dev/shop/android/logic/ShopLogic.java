package com.android.dev.shop.android.logic;

import com.android.dev.shop.android.base.temple.TDataListLogic;
import com.android.dev.shop.android.base.temple.UIGeter;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-03-31.
 */

public class MyLogic extends TDataListLogic<String> {
    public MyLogic(String tag, LogicCallback callback) {
        super(tag, callback);
    }

    @Override
    public void toGetDataList(Object... args) {

    }

    @Override
    protected ArrayList parseDataToList(String data, UIGeter geter) throws JSONException {
        return null;
    }
}
