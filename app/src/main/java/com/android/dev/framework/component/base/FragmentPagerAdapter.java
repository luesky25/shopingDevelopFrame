
package com.android.dev.framework.component.base;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 此类重写android.support.v4.app.FragmentPagerAdapter
 * 描述:重写instantiateItem方法，更改makeFragmentName()方法所获取的name值，
 * 使FragmentPagerAdapter改变时不用移除原有Item就可以进行刷新页面，原有fragment状态进行保留。
 *
 * @author xuhaichao
 * @since 2013-7-4 下午4:44:51
 */
public abstract class FragmentPagerAdapter extends PagerAdapter {

    private final FragmentManager mFragmentManager;

    private FragmentTransaction mCurTransaction = null;

    private Fragment mCurrentPrimaryItem = null;

    public FragmentPagerAdapter(FragmentManager fm) {
        this.mFragmentManager = fm;
    }

    public abstract Fragment getItem(int paramInt);

    public void startUpdate(ViewGroup container) {
    }

    public Object instantiateItem(ViewGroup container, int position) {
        String fragmentName = getItem(position).getClass().getName();
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        long itemId = getItemId(position);

        String name = makeFragmentName(container.getId(), itemId, fragmentName);
        Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            this.mCurTransaction.attach(fragment);
            this.mCurTransaction.show(fragment);
        } else {
            fragment = getItem(position);

            this.mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId, fragmentName));
        }
        if (fragment != this.mCurrentPrimaryItem) {
            // 当前fragment和上个fragment不同时，判断position，
            // 如果pisition相同，则是同一页面间的跳转，由于未清除上一页面，所以要对不需要显示页面进行隐藏
            if (mCurrentPrimaryItem != null) {
                String mCurrentPrimaryItemPosition = mCurrentPrimaryItem.getTag().split(":")[3];
                if (Integer.parseInt(mCurrentPrimaryItemPosition) == position) {
                    this.mCurTransaction.hide(mCurrentPrimaryItem);
                }
            }
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        this.mCurTransaction.detach((Fragment) object);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = fragment;
        }
    }

    public void finishUpdate(ViewGroup container) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }
    }

    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public long getItemId(int position) {
        return position;
    }

    public static String makeFragmentName(int viewId, long id, String fragmentName) {
        return "android:switcher:" + viewId + ":" + id + ":" + fragmentName;
    }
}
