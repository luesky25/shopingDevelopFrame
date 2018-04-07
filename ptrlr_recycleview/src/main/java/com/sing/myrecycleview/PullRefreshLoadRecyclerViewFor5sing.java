package com.sing.myrecycleview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;
import com.linfaxin.recyclerview.overscroll.OverScrollGridManager;
import com.linfaxin.recyclerview.overscroll.OverScrollImpl;
import com.linfaxin.recyclerview.overscroll.OverScrollLinearLayoutManager;

/**
 * Created by linfaxin on 15/8/31.
 * Pull To Refresh & Load RecyclerView
 */
public class PullRefreshLoadRecyclerViewFor5sing extends FrameLayout implements RefreshView.StateListener, LoadMoreView.StateListener {
    InnerRecyclerView recyclerView;
    LoadMoreViewFor5sing loadMoreView;
    RefreshViewFor5sing refreshView;
    LoadRefreshListener loadRefreshListener;
    private LoadMoreViewFor5sing zLoadMoreView;//用于拆装的loadmoreview

    public PullRefreshLoadRecyclerViewFor5sing(Context context) {
        super(context);
        init();
    }

    public PullRefreshLoadRecyclerViewFor5sing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullRefreshLoadRecyclerViewFor5sing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshLoadRecyclerViewFor5sing(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        recyclerView = new InnerRecyclerView(getContext());
        recyclerView.setLayoutManager(new OverScrollLinearLayoutManager(recyclerView));
        addView(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zLoadMoreView = new LoadMoreViewFor5sing(getContext());
        setLoadMoreView(zLoadMoreView);
        setRefreshView(new RefreshViewFor5sing(getContext()));
    }

    public void setLoadMoreView(LoadMoreViewFor5sing loadMoreView) {
        if (this.loadMoreView != null) {
            this.loadMoreView.setStateListener(null);
            this.loadMoreView.unBind();
            removeView(this.loadMoreView);
        }
        this.loadMoreView = loadMoreView;

        if (loadMoreView != null) {
            addView(loadMoreView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL));
            loadMoreView.bindWith(recyclerView);
            loadMoreView.setStateListener(this);
        }
    }




    /**
     * 设置当加载数据全部完成时,不显示加载完成的loadmore view,
     *
     * @param hide true 时是隐藏,默认为false
     */
    public void setNoMoreHideWhenNoMoreData(boolean hide) {
        if (loadMoreView == null) return;
        loadMoreView.setNoMoreHideWhenNoMoreData(hide);
    }

    public void setRefreshTime(String time) {
        refreshView.setRefreshTime(time);
    }

    public void setRefreshView(RefreshViewFor5sing refreshView) {
        if (this.refreshView != null) {
            this.refreshView.setStateListener(null);
            this.refreshView.unBind();
            removeView(this.refreshView);
        }
        this.refreshView = refreshView;
        if (refreshView != null) {
            addView(refreshView, new LayoutParams(-1, -2, Gravity.TOP));
            refreshView.bindWith(recyclerView);
            refreshView.setStateListener(this);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(LoadRefreshAdapter adapter) {
        recyclerView.setAdapter(adapter);
        setLoadRefreshListener(adapter);
    }

    public LoadMoreViewFor5sing getLoadMoreView() {
        return loadMoreView;
    }

    public RefreshViewFor5sing getRefreshView() {
        return refreshView;
    }

    /**
     * 设置是否能下拉
     */
    public void setCanOverTop(boolean canOverTop) {
        recyclerView.setCanOverTop(canOverTop);
    }

    /**
     * 设置是否能上拉
     */
    public void setCanOverBottom(boolean canOverBottom) {
        recyclerView.setCanOverBottom(canOverBottom);
    }

    public void setOverScrollChangeListener(OverScrollImpl.OverScrollChangeListener overScrollChangeListener) {
        recyclerView.setOverScrollChangeListener(overScrollChangeListener);
    }

    public boolean isLoading() {
        return (loadMoreView != null && loadMoreView.getState() == LoadMoreView.STATE.LOADING) ||
                (refreshView != null && refreshView.getState() == RefreshView.STATE.LOADING);
    }

    @Override
    public boolean interceptStateChange(RefreshView refreshView, RefreshView.STATE state, RefreshView.STATE oldState) {
        if (state != RefreshView.STATE.NORMAL &&
                loadMoreView != null && loadMoreView.getState() == LoadMoreView.STATE.LOADING) {
            return true;
        }
        return false;
    }

    @Override
    public void onStateChange(RefreshView refreshView, RefreshView.STATE state) {
        if (loadRefreshListener != null && state == RefreshView.STATE.LOADING) {
            loadRefreshListener.onRefresh(this, refreshView);

            if (loadMoreView != null && loadMoreView.getState() == LoadMoreView.STATE.LOAD_FAIL) {
                loadMoreView.setState(LoadMoreView.STATE.NORMAL);
            }
        }
    }

    @Override
    public boolean interceptStateChange(LoadMoreView loadMoreView, LoadMoreView.STATE state, LoadMoreView.STATE oldState) {
        if (state == LoadMoreView.STATE.LOADING &&
                refreshView != null && refreshView.getState() == RefreshView.STATE.LOADING) {
            return true;
        }
        return false;
    }

    @Override
    public void onStateChange(LoadMoreView loadMoreView, LoadMoreView.STATE state) {

        if (loadRefreshListener != null && state == LoadMoreView.STATE.LOADING) {
            loadRefreshListener.onLoadMore(this, loadMoreView);
        }
    }

    public void setLoadRefreshListener(LoadRefreshListener loadRefreshListener) {
        this.loadRefreshListener = loadRefreshListener;
    }

//    public void setAdapter(RecyclerView.Adapter adapter) {
//        recyclerView.setAdapter(adapter);
//    }
//
//    public void setAdapter(LoadRefreshAdapter adapter) {
//        recyclerView.setAdapter(adapter);
//        setLoadRefreshListener(adapter);
//    }

    public static abstract class LoadRefreshAdapter<T extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter<T> implements LoadRefreshListener {
    }

    public interface LoadRefreshListener {
        void onRefresh(PullRefreshLoadRecyclerViewFor5sing pullRefreshLoadRecyclerView, RefreshView refreshView);

        void onLoadMore(PullRefreshLoadRecyclerViewFor5sing pullRefreshLoadRecyclerView, LoadMoreView loadMoreView);
    }

    private static class InnerRecyclerView extends RecyclerView {
        private OverScrollImpl.OverScrollLayoutManager mLayoutManager;

        public InnerRecyclerView(Context context) {
            super(context);
        }

        public void setCanOverTop(boolean canOverTop) {
            mLayoutManager.setCanOverTop(canOverTop);
        }

        public void setCanOverBottom(boolean canOverBottom) {
            mLayoutManager.setCanOverBottom(canOverBottom);
        }

        @Override
        public void setLayoutManager(LayoutManager layout) {

            if (layout != null && !(layout instanceof OverScrollImpl.OverScrollLayoutManager)) {
                if (layout instanceof GridLayoutManager) {
                    GridLayoutManager grid = (GridLayoutManager) layout;
                    OverScrollGridManager layout1 = new OverScrollGridManager(this, grid.getSpanCount(), grid.getOrientation(), grid.getReverseLayout());
                    this.mLayoutManager=layout1;
                    super.setLayoutManager(layout1);
                } else if (layout instanceof LinearLayoutManager) {
                    LinearLayoutManager linear = (LinearLayoutManager) layout;
                    OverScrollLinearLayoutManager layout2 = new OverScrollLinearLayoutManager(this, linear.getOrientation(), linear.getReverseLayout());
                    this.mLayoutManager=layout2;
                    super.setLayoutManager(layout2);
                } else {
                    throw new IllegalArgumentException("LayoutManager " + layout +
                            " should be subclass of: " + OverScrollLinearLayoutManager.class.getName());
                }
            }
        }

        public void setOverScrollChangeListener(OverScrollImpl.OverScrollChangeListener overScrollChangeListener) {
            if (mLayoutManager==null) return;
            mLayoutManager.setOverScrollChangeListener(overScrollChangeListener);
        }

        @Override
        public int computeVerticalScrollOffset() {
            int overScrollDistance = 0;
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof OverScrollImpl.OverScrollLayoutManager) {
                overScrollDistance = ((OverScrollImpl.OverScrollLayoutManager) layoutManager).getOverScrollDistance();
            }
            return super.computeVerticalScrollOffset() - overScrollDistance;
        }

        @Override
        public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
            Log.d("c325","dispatchNestedPreScroll:"+dy);

            return super.dispatchNestedPreScroll(dx,dy,consumed,offsetInWindow);
        }

        @Override
        public boolean fling(int velocityX, int velocityY) {
            Log.d("c325","fling:"+velocityY);
            return super.fling(velocityX, velocityY);
        }
    }
}
