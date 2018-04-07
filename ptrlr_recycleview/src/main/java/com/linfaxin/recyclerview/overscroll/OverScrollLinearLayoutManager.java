package com.linfaxin.recyclerview.overscroll;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by linfaxin on 15/8/29.
 * OverScroll For LinearLayout Manager
 */
public class OverScrollLinearLayoutManager extends LinearLayoutManager implements
        OverScrollImpl.OverScrollLayoutManager {
    OverScrollImpl overScrollImpl;

    public OverScrollLinearLayoutManager(RecyclerView recyclerView) {
        super(recyclerView.getContext());
        overScrollImpl = new OverScrollImpl(recyclerView);
    }

    public OverScrollLinearLayoutManager(RecyclerView recyclerView, int orientation, boolean reverseLayout) {
        super(recyclerView.getContext(), orientation, reverseLayout);
        overScrollImpl = new OverScrollImpl(recyclerView);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //dy应该是滑动距离
//        super.scrollVerticallyBy()
        //recyclerView 取这个方法作为自定义滑动距离
        return overScrollImpl.scrollVerticallyBy(this, dy, recycler, state);
    }


    //****  自定义方法处理各种需求 *****//

    @Override
    /**
     * 返回默认的滚动实际距离
     */
    public int superScrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                       RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int getOverScrollDistance() {
        return overScrollImpl.getOverScrollDistance();
    }



    @Override
    public void setOverScrollChangeListener(OverScrollImpl.OverScrollChangeListener overScrollChangeListener) {//设置 超出了屏幕距离的回调
        overScrollImpl.setOverScrollChangeListener(overScrollChangeListener);
    }

    @Override
    public void setLockOverScrollTop(int topDistance) {//设置悬停时的高度
        overScrollImpl.setLockOverScrollTop(topDistance);
    }

    @Override
    public void setCanOverTop(boolean canOverTop) {//设置头部是否能滑出边界
        overScrollImpl.setCanOverTop(canOverTop);
    }

    @Override
    public void setCanOverBottom(boolean canOverBottom) {//设置底部是否能滑出边界
        overScrollImpl.setCanOverBottom(canOverBottom);
    }
}
