package com.android.dev.shop.android.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.dev.basics.DevApplication;
import com.android.dev.framework.component.base.BaseWorkerFragment;
import com.android.dev.shop.R;
import com.android.dev.utils.ActivityUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-03-25.
 */

public class ManagerFragment extends BaseWorkerFragment implements View.OnClickListener{
    private View head;
    private RelativeLayout meanage_login_content;


    @Override
    protected void handleBackgroundMessage(Message msg) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = view.findViewById(R.id.listview);
         head = LayoutInflater.from(getActivity()).inflate(R.layout.shopping_manage_head,null);
        listView.addHeaderView(head);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,new ArrayList<String>()));
        meanage_login_content = head.findViewById(R.id.meanage_login_content);
        meanage_login_content.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_body_layout,null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.meanage_login_content:
                if(DevApplication.getMyApplication().isGuest()){
                    ActivityUtils.gotoCheckActivity(mActivity,0,true,false);
                }
                break;
        }
    }
}
