package com.android.dev.shop.android.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.dev.framework.component.base.BaseWorkerFragment;
import com.android.dev.shop.R;

/**
 * Created by Administrator on 2018-03-25.
 */

public class ShoppingListFragment extends BaseWorkerFragment{

    private int type;
    public static final int TYPE_SHOP = 1;
    public static final int TYPE_COURSE = 2;

    private static ShoppingListFragment mfragment;
    public static ShoppingListFragment getInstance(int type){

        if(mfragment==null){
            mfragment = new ShoppingListFragment();
        }
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
    protected void handleBackgroundMessage(Message msg) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_model,null);
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
