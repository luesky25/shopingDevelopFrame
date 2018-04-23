package com.android.dev.shop.android.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.common.http.utils.PreferencesUtils;
import com.android.dev.framework.component.base.BaseWorkerFragment;
import com.android.dev.shop.PreferenceConfig;
import com.android.dev.shop.R;
import com.android.dev.shop.android.base.BaseWorkerOnClickFragment;
import com.android.dev.shop.android.eventbus.EventBusCallback;
import com.android.dev.shop.android.eventbus.EventMessage;
import com.android.dev.shop.android.eventbus.EventType;
import com.android.dev.shop.android.eventbus.KGEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-25.
 */

public class IndexFragment extends BaseWorkerOnClickFragment implements EventBusCallback {
    private RelativeLayout color_tab;
    private TabLayout tabLayout;

    private ViewPager pager;

    private List<Fragment> mFragmentList;

    private int currentPosition = 0;

    /**
     * 标签list
     */
    private List<String> mCategoryList;

    private MyViewPageAdapter adapter;

    @Override
    protected void handleBackgroundMessage(Message msg) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, null);
    }

    private void initViews(View v) {
        pager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.ring_title_bar);


        mFragmentList = new ArrayList<Fragment>();
        ShoppingListFragment shoppingListFragment = ShoppingListFragment.getInstance(ShoppingListFragment.TYPE_SHOP);
        ShoppingListFragment courseListFragment = ShoppingListFragment.getInstance(ShoppingListFragment.TYPE_COURSE);
        mFragmentList.add(shoppingListFragment);
        mFragmentList.add(courseListFragment);
        List<Integer> types = new ArrayList<>();
        types.add(ShoppingListFragment.TYPE_SHOP);
        types.add(ShoppingListFragment.TYPE_COURSE);
        mCategoryList = new ArrayList<>();
        mCategoryList.add("课程");
        mCategoryList.add("商品");
        adapter = new MyViewPageAdapter(getChildFragmentManager(), mFragmentList);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.addTab(tabLayout.newTab().setText(mCategoryList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mCategoryList.get(1)));
        tabLayout.setupWithViewPager(pager);
//        tabLayout.getTabAt(0).setText("课程");
//        for (int i = 1; i <= mCategoryList.size(); i++) {
//            tabLayout.getTabAt(i).setText(mCategoryList.get(i - 1));
//        }

//		tabLayout.getTabAt(2).setText(tabTitles[2]);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                currentPosition = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
//				mv.move(arg0, arg1);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Subscribe
    @Override
    public void onEventMainThread(EventMessage msg) {

        TextView tv;
        switch (msg.what) {
            case EventType.EVENT_XINGE_MSG_FROM_APP:
                tv = (TextView) getView().findViewById(R.id.test);
                tv.setVisibility(View.VISIBLE);
                tv.setText("当前设备token"+ PreferencesUtils.getString(getContext(), PreferenceConfig.XINGE_PHONE_TOKEN,"")+"应用内信息" + (String) msg.obj1);
                break;
            case EventType.EVENT_XINGE_MSG_NOTI_FROM_APP:
                 tv = (TextView) getView().findViewById(R.id.test);
                tv.setVisibility(View.VISIBLE);
                tv.setText("当前设备token"+ PreferencesUtils.getString(getContext(), PreferenceConfig.XINGE_PHONE_TOKEN,"")+"应用外通知栏信息" + (String) msg.obj1);

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
