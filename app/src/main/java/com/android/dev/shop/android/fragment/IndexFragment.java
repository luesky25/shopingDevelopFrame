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

import com.android.dev.framework.component.base.BaseWorkerFragment;
import com.android.dev.shop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-25.
 */

public class IndexFragment extends BaseWorkerFragment {

    private RelativeLayout color_tab;
    private TabLayout tabLayout;

    private ViewPager pager;

    private List<ShoppingListFragment> mFragmentList;

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


        mFragmentList = new ArrayList<ShoppingListFragment>();
//        ShoppingListFragment shoppingListFragment = ShoppingListFragment.getInstance(ShoppingListFragment.TYPE_SHOP);
//        ShoppingListFragment courseListFragment = ShoppingListFragment.getInstance(ShoppingListFragment.TYPE_COURSE);
//        mFragmentList.add(shoppingListFragment);
//        mFragmentList.add(courseListFragment);
        List<Integer> types = new ArrayList<>();
        types.add(ShoppingListFragment.TYPE_SHOP);
        types.add(ShoppingListFragment.TYPE_COURSE);
        mCategoryList = new ArrayList<>();
        mCategoryList.add("课程");
        mCategoryList.add("商品");
        adapter = new MyViewPageAdapter(getChildFragmentManager(), mFragmentList,types);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(pager);
//        tabLayout.getTabAt(0).setText("课程");
//        for (int i = 1; i <= mCategoryList.size(); i++) {
//            tabLayout.getTabAt(i).setText(mCategoryList.get(i - 1));
//        }
//
////		tabLayout.getTabAt(2).setText(tabTitles[2]);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


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


    class MyViewPageAdapter extends FragmentPagerAdapter {

        List<ShoppingListFragment> fragmentList = new ArrayList<ShoppingListFragment>();
        List<Integer> typeList = new ArrayList<>();

        public MyViewPageAdapter(FragmentManager fm, List<ShoppingListFragment> fragmentList,List<Integer> typeList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.typeList = typeList;
        }

        private List<Fragment> pages;

        //...

        public Fragment getItem(int position) {
            Fragment page = null;
            if (pages.size() > position) {
                page = pages.get(position);
                if (page != null) {
                    return page;
                }
            }
            while (position >= pages.size()) {
                pages.add(null);
            }
            page = ShoppingListFragment.newPage(mFragmentList.get(position), typeList.get(position));
            pages.set(position, page);
            return page;

        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


    }
}
