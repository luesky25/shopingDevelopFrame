package com.android.dev.shop;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.dev.framework.component.base.BaseWorkerFragmentActivity;
import com.android.dev.shop.android.fragment.IndexFragment;
import com.android.dev.shop.android.fragment.ManagerFragment;
import com.android.dev.shop.android.fragment.MessageFragment;
import com.android.dev.shop.android.fragment.ShoppingCarFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseWorkerFragmentActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private MyViewPageAdapter myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        fragments = new ArrayList<>();
        IndexFragment indexFragment = new IndexFragment();
        MessageFragment messageFragment = new MessageFragment();
        ShoppingCarFragment shoppingCarFragment = new ShoppingCarFragment();
        ManagerFragment managerFragment = new ManagerFragment();
        fragments.add(indexFragment);
        fragments.add(messageFragment);
        fragments.add(shoppingCarFragment);
        fragments.add(managerFragment);
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setOffscreenPageLimit(fragments.size());
        myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(myViewPageAdapter);
        findViewById(R.id.index).setOnClickListener(this);
        findViewById(R.id.message).setOnClickListener(this);
        findViewById(R.id.manage).setOnClickListener(this);
        findViewById(R.id.shopping_car).setOnClickListener(this);
    }

    @Override
    protected void handleBackgroundMessage(Message msg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.index:
                viewPager.setCurrentItem(0);
                break;
            case R.id.message:
                viewPager.setCurrentItem(1);
                break;
            case R.id.manage:
                viewPager.setCurrentItem(3);
                break;
            case R.id.shopping_car:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    class MyViewPageAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public MyViewPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }



}
