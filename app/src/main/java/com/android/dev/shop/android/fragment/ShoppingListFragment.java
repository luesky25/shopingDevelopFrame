package com.android.dev.shop.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.common.volley.VolleyError;
import com.android.dev.shop.R;
import com.android.dev.shop.android.adapter.ShoppingListAdapter;
import com.android.dev.shop.android.base.temple.JsonCallback;
import com.android.dev.shop.android.base.temple.TDataListFragment;
import com.android.dev.shop.android.logic.ShopLogic;
import com.android.dev.shop.android.publisher.ShopPublisher;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018-03-25.
 */

public class ShoppingListFragment extends TDataListFragment<ShopLogic,String,ShoppingListAdapter> {



    private int type;
    public static final int TYPE_SHOP = 1;
    public static final int TYPE_COURSE = 2;

    private static ShoppingListFragment mfragment;
    public static ShoppingListFragment getInstance(int type){
            mfragment = new ShoppingListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        mfragment.setArguments(bundle);
        return mfragment;
    }

    public static ShoppingListFragment newPage(ShoppingListFragment fragment,int type){
        if(mfragment==null && mfragment.getType()!=type){
            mfragment = new ShoppingListFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        mfragment.setArguments(bundle);
        return mfragment;
    }


    @Override
    protected ShopLogic creatLogic() {
        return new ShopLogic(getTag(),this);
    }

    @Override
    protected boolean needLogin() {
        return false;
    }

    @Override
    protected int createContetntView() {
        return R.layout.tdata_list_layout;
    }

    @Override
    protected RecyclerView.LayoutManager getDataLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected void toGetDataList() {
            mLogic.toGetDataList();
    }

    @Override
    protected ShoppingListAdapter getDataAdapter() {
        return new ShoppingListAdapter(getActivity(),mDataList);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = mfragment.getArguments();
        type = bundle.getInt("type");
    }

    public int getType() {
        return type;
    }

}
