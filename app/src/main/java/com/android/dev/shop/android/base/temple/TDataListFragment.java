package com.android.dev.shop.android.base.temple;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dev.basics.DevApplication;
import com.android.dev.shop.R;
import com.android.dev.utils.ToolUtils;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.RefreshView;
import com.sing.myrecycleview.PullRefreshLoadRecyclerViewFor5sing;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * 由 lampartlin 于 2016/2/23 创建.
 */
public abstract class TDataListFragment<L2 extends TDataListLogic, D, A extends RecyclerView.Adapter> extends SingBaseSupportFragment<L2> {
    protected ArrayList<D> mDataList;
    protected A mDataListAdapter;

    protected int pageSize = 10;
    protected int offset;
    protected RelativeLayout rl_find_front;
    protected TextView tv_no_data;
    protected ViewFlipperImpl vf_fing_front;
    protected TextView net_error_tv;
    protected ViewGroup net_errorLayout;
    protected RelativeLayout no_wifi_ll;
    protected PullRefreshLoadRecyclerViewFor5sing ptr_recycle_parent;

    protected L2 mLogic;
    protected View rootView;
    protected int pageIndex;

    @Override
    protected void getCreateData(Bundle data) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogic = creatLogic();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(createContetntView(), container, false);
        }

        // Cache rootView.
        // remove rootView from its parent
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected abstract L2 creatLogic();

    protected abstract boolean needLogin();

    @LayoutRes
    protected abstract int createContetntView();


    @Override
    @CallSuper
    protected void initClass() {
        mDataList = new ArrayList<>();
    }

    @Override
    @CallSuper
    protected void findViews(View contentView) {
        ptr_recycle_parent = (PullRefreshLoadRecyclerViewFor5sing) contentView.findViewById(R.id.ptr_recycle_parent);

        //异常页面初始化
        // 覆盖框
        rl_find_front = (RelativeLayout) contentView.findViewById(R.id.rl_find_front);
        // 无数据框
        tv_no_data = (TextView) contentView.findViewById(R.id.no_data_tv);
        // vf
        vf_fing_front = (ViewFlipperImpl) contentView.findViewById(R.id.data_error);
        // 错误框
        net_error_tv = (TextView) contentView.findViewById(R.id.net_error_tv);
        // 错误框
        net_errorLayout = (ViewGroup) contentView.findViewById(R.id.net_error);
        //无网络
        no_wifi_ll = (RelativeLayout) contentView.findViewById(R.id.no_wifi);

    }

    @Override
    @CallSuper
    protected void initViews() {
        //内部recycleview与普通recycleview使用方法完全一致
//        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
//        int width = (ToolUtils.getWidth(getActivity()) - DisplayUtil.dip2px(getActivity(), 40)) / 2;
//        int height = width / 340 * 192;
//        manager.setMeasuredDimension(ToolUtils.getWidth(getActivity()), width / 340 * 192+DisplayUtil.dip2px(getActivity(), 45));
        ptr_recycle_parent.getRecyclerView().setLayoutManager(getDataLayoutManager());

        //设置当加载数据全部完成时,不显示加载完成的loadmore view,true 时是隐藏,默认为false
        ptr_recycle_parent.setNoMoreHideWhenNoMoreData(true);
        //当不需要加载更多时,设置loadmoreview为空
//        ptr_recycle_parent.setLoadMoreView(null);
//        ptr_recycle_parent.setRefreshView(null);
    }

    protected abstract RecyclerView.LayoutManager getDataLayoutManager();

    @Override
    @CallSuper
    protected void addListeners() {
        ptr_recycle_parent.setLoadRefreshListener(new PullRefreshLoadRecyclerViewFor5sing.LoadRefreshListener() {
            @Override
            public void onRefresh(PullRefreshLoadRecyclerViewFor5sing pullRefreshLoadRecyclerView, RefreshView refreshView) {
                refreshData();
            }

            @Override
            public void onLoadMore(PullRefreshLoadRecyclerViewFor5sing pullRefreshLoadRecyclerView, LoadMoreView loadMoreView) {
                loadMoreData();
            }
        });
        // 网络错误时点击字符串刷新
        net_errorLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ToolUtils.checkNetwork(getActivity())) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.err_no_net), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                net_errorLayout.setEnabled(false);
                startLoadMore();
            }
        });
        no_wifi_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ToolUtils.checkNetwork(getActivity())) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.err_no_net), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                no_wifi_ll.setEnabled(false);
                startLoadMore();
            }
        });
        tv_no_data.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ToolUtils.checkNetwork(getActivity())) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.err_no_net), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                tv_no_data.setEnabled(false);
                startLoadMore();
            }
        });

    }

    protected void loadMoreData() {
        if (!ToolUtils.checkNetwork(getActivity())) {
            if (mDataList.size() == 0) {
                showNoWifi();
            } else {
                showToast(R.string.err_no_net);
                ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOAD_FAIL);
            }
            return;
        }
        //进入后会自动进行一次loadmore
        toGetDataList();
    }

    protected void refreshData() {
        offset = 0;
        pageIndex = 0;
        if (!ToolUtils.checkNetwork(getActivity())) {
            if (mDataList.size() == 0) {
                showNoWifi();
            } else {
                offset = mDataList.size();
                showToast(R.string.err_no_net);
                ptr_recycle_parent.getRefreshView().setState(RefreshView.STATE.NORMAL);
            }
            return;
        }
        toGetDataList();
    }

    /**
     * 只写入获取当前页的logic调用
     */
    protected abstract void toGetDataList();

    @Override
    @CallSuper
    protected void setAdapter() {
        // 初始化自定义的适配器
//        channalListAdapter = new ChannalListAdapter(getActivity(), mChannalList);
        mDataListAdapter = getDataAdapter();
        // 给内部的recyclerview设置适配器
        ptr_recycle_parent.getRecyclerView().setAdapter(mDataListAdapter);

    }

    /**
     * 创建对应的Adapter并返回
     */
    protected abstract A getDataAdapter();

    @Override
    protected void beginAction() {
        if (!needLogin()) startLoadMore();
    }

    @Override
    @CallSuper
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isAdded()) return;
        if (isVisibleToUser && offset == 0) {
            checkAndLoad();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @CallSuper
    public void onLogicCallback(UIGeter geter, int callbackCode) {
        if (ptr_recycle_parent == null) {
            return;
        }
        if (ptr_recycle_parent.getRefreshView() != null) {
            ptr_recycle_parent.getRefreshView().setState(RefreshView.STATE.NORMAL);
        }
        switch (callbackCode) {
            case TDataListLogic.CALLBACK_GET_DATA_LIST_SUCCESS:
                if (offset == 0) {
                    mDataList.clear();
                    pageIndex = 1;
                } else {
                    pageIndex++;
                }

                showContent();
                ArrayList<D> tlist = (ArrayList<D>) geter.getReturnObject();
                dealTempList(tlist);
                mDataList.addAll(tlist);
                offset = mDataList.size();
                deealData();
                mDataListAdapter.notifyDataSetChanged();
                // load more complete
                dealLoadmore(tlist);
                updateDate();//更新当前时间
                getUpdate();
                if (ptr_recycle_parent.getRefreshView() != null) {
                    ptr_recycle_parent.setRefreshTime(getDateFormat());//设置最后更新时间
                }
                break;
            case TDataListLogic.CALLBACK_CACHE_DATA_LIST_SUCCESS://缓存的列表数据
                ArrayList<D> cacheTlist = (ArrayList<D>) geter.getReturnObject();
                if (cacheTlist.size() <= 0) {
                    return;
                }
                dealTempList(cacheTlist);
                showContent();
                mDataList.addAll(cacheTlist);
                deealData();
                offset = mDataList.size();
                mDataListAdapter.notifyDataSetChanged();
                // load more complete
                dealLoadmore(cacheTlist);
                updateDate();//更新当前时间
                getUpdate();
                break;
            case TDataListLogic.CALLBACK_NET_ERR:
                if (mDataList.size() == 0) {
                    showNetErr();
                } else {
                    toastLoadFail(geter);
                }
                break;
            case TDataListLogic.CALLBACK_SERVER_ERR:
                if (mDataList.size() == 0) {
                    showServerErr(geter.getMessage());
                } else {
                    toastLoadFail(geter);
                }
                break;
            case TDataListLogic.CALLBACK_OTHER_ERR:
                if (mDataList.size() == 0) {
                    showServerErr(geter.getMessage());
                } else {
                    toastLoadFail(geter);
                }
                break;
            case TDataListLogic.CALLBACK_NODATA:
                dealNoData();
                break;
        }
    }

    protected void dealTempList(ArrayList<D> tlist) {
    }

    protected void toastLoadFail(UIGeter geter) {
        showToast(geter.getMessage());
        ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOAD_FAIL);
    }

    protected void dealLoadmore(ArrayList<D> cacheTlist) {
        if (cacheTlist.size() < pageSize) {//根据列表数量设置是否显示加载更多
            ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.NO_MORE);
        } else {
            ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.NORMAL);
        }
    }

    protected void dealNoData() {
        if (mDataList.size() == 0) {
            showNoData();
        } else {
            if(ptr_recycle_parent.getLoadMoreView()!=null){
                ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.NO_MORE);
            }
        }
    }

    @Override
    @CallSuper
    public void OnLoginSuccess() {
        checkAndLoad();
    }

    @Override
    @CallSuper
    public void OnLogoutSuccess() {
        checkAndLoad();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    // 页面显示的4种状态
    // 显示读取
    protected void startLoadMore() {
        rl_find_front.setVisibility(View.GONE);
        ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOADING);
    }

    // 显示服务器错误
    protected void showServerErr(String errStr) {
        show_front_view();
        ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOAD_FAIL);
        net_error_tv.setVisibility(View.VISIBLE);
        net_error_tv.setText(errStr);
        net_errorLayout.setEnabled(true);
        vf_fing_front.setDisplayedChild(1);
    }

    protected void showNoWifi() {
        show_front_view();
        ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOAD_FAIL);
        net_error_tv.setVisibility(View.VISIBLE);
        vf_fing_front.setDisplayedChild(2);
        no_wifi_ll.setEnabled(true);
    }

    // 显示w网络错误
    protected void showNetErr() {
        show_front_view();
        ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOAD_FAIL);
        tv_no_data.setText("网络堵车了\n点击屏幕再试试");
        tv_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setEnabled(true);
        vf_fing_front.setDisplayedChild(0);
    }

    // 显示无数据
    protected void showNoData() {
        show_front_view();
        if(ptr_recycle_parent.getLoadMoreView()!=null){
            ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.NO_MORE);
        }
        tv_no_data.setText(getNoDataString());
        tv_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setEnabled(false);
        vf_fing_front.setDisplayedChild(0);
    }

    protected String getNoDataString() {
        return "木有数据哦。";
    }

    protected void show_front_view() {
        if (ptr_recycle_parent.getRefreshView() != null) {
            ptr_recycle_parent.getRefreshView().setState(RefreshView.STATE.NORMAL);
        }
        if(ptr_recycle_parent.getLoadMoreView()!=null){
            ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.NORMAL);
        }
        rl_find_front.setVisibility(View.VISIBLE);
    }

    // 显示内容
    protected void showContent() {
        rl_find_front.setVisibility(View.GONE);
    }

    protected void checkAndLoad() {
        if (!needLogin()) return;
        if (!ToolUtils.checkNetwork(getActivity())) {
            if (mDataList.size() == 0) {
                showNoWifi();
            } else {
                showToast(R.string.err_no_net);
                ptr_recycle_parent.getLoadMoreView().setState(LoadMoreView.STATE.LOAD_FAIL);
            }
            return;
        }
        if (checkLogin()) {
            startLoadMore();
        } else {
            offset = 0;
            pageIndex = 0;
            mDataList.clear();
            mDataListAdapter.notifyDataSetChanged();
        }
    }

    protected boolean checkLogin() {
        // 如果是动态，则判断是否已经登陆
        if (DevApplication.getMyApplication().isGuest()) {

        } else {

        }
        return !DevApplication.getMyApplication().isGuest();
    }


    /**
     * 需要做数据处理可重写
     */
    protected void deealData() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLogic != null) mLogic.onDestroy();
        if (ptr_recycle_parent != null && ptr_recycle_parent.getRecyclerView() != null) {
            if (ptr_recycle_parent.getRecyclerView().getRecycledViewPool() != null)
                ptr_recycle_parent.getRecyclerView().getRecycledViewPool().clear();
        }

        //vf_fing_front.onDetachedFromWindow();
    }
}
