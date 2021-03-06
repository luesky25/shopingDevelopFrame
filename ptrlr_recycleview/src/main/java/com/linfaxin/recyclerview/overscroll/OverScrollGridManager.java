package com.linfaxin.recyclerview.overscroll;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by linfaxin on 15/8/29.
 * OverScroll For LinearLayout Manager
 */
public class OverScrollGridManager extends GridLayoutManager implements
        OverScrollImpl.OverScrollLayoutManager {
    OverScrollImpl overScrollImpl;

    public OverScrollGridManager(RecyclerView recyclerView, int spanCount) {
        super(recyclerView.getContext(), spanCount);
        overScrollImpl = new OverScrollImpl(recyclerView);
    }

    public OverScrollGridManager(RecyclerView recyclerView, int spanCount, int orientation,
                                 boolean reverseLayout) {
        super(recyclerView.getContext(), spanCount, orientation, reverseLayout);
        overScrollImpl = new OverScrollImpl(recyclerView);
    }

    @Override
    public void setCanOverTop(boolean canOverTop) {
        overScrollImpl.setCanOverTop(canOverTop);
    }

    @Override
    public void setCanOverBottom(boolean canOverBottom) {
        overScrollImpl.setCanOverBottom(canOverBottom);
    }

    @Override
    public void setOverScrollChangeListener(OverScrollImpl.OverScrollChangeListener overScrollChangeListener) {
        overScrollImpl.setOverScrollChangeListener(overScrollChangeListener);
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {//recycleView对其manager进行调用
        int i=overScrollImpl.scrollVerticallyBy(this, dy, recycler, state);
        return i;//计算出滑动距离的
    }

    @Override
    public int superScrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                       RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int getOverScrollDistance() {
        return overScrollImpl.getOverScrollDistance();
    }

    @Override
    public void setLockOverScrollTop(int topDistance) {
        overScrollImpl.setLockOverScrollTop(topDistance);
    }
}
