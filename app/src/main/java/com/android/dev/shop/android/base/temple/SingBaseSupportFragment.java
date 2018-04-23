package com.android.dev.shop.android.base.temple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.common.utils.KGLog;
import com.android.dev.shop.R;
import com.android.dev.shop.android.eventbus.KGEventBus;
import com.android.dev.shop.dialog.TipsDialog;
import com.android.dev.utils.ActivityUtils;
import com.android.dev.utils.DatePref;
import com.android.dev.utils.DateUtil;
import com.pulltozoomview.IScrollerImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;


/**
 * 由 lampartlin 于 2016/2/18 创建.
 */
public abstract class SingBaseSupportFragment<L extends BaseLogic> extends Fragment implements   BaseLogic.LogicCallback, IScrollerImpl {
    public final String TAG = this.getClass().getSimpleName();


    protected L mLogic;

    private Date mUpdateDate;//数据更新时间
    protected TipsDialog choiceDialog;

    protected boolean visible = false;

    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogic = creatLogic();
        if(!EventBus.getDefault().isRegistered(this)){
            KGEventBus.register(this);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCreateData(getArguments());
        initClass();
        findViews(getView());
        initViews();
        addListeners();
        setAdapter();
        beginAction();
    }

    /**
     * 创建你的逻辑类
     *
     * @return 本Activity所需要用到的逻辑类
     */
    protected abstract L creatLogic();

    /**
     * 这里解析创建时Intent传入的数据
     */
    protected abstract void getCreateData(Bundle data);

    /**
     * 创建些额外需要使用的类,比如一些视图的工具类,逻辑相关的类建议到Logic里面创建
     */
    protected abstract void initClass();

    /**
     * 基本只写入findview内容
     */
    protected abstract void findViews(View contentView);

    /**
     * 基本只写入控件初始化内容
     */
    protected abstract void initViews();

    /**
     * 基本只写入添加监听器内容
     */
    protected abstract void addListeners();

    /**
     * 基本只写入添加适配器内容
     * @deprecated 发现基类这个方法不是必要流程   适用性比较差  不再强制实现并且标识为弃用
     */
    @Deprecated
    protected void setAdapter(){};

    /**
     * 开始进行网络数据或其它页面行动操作
     */
    protected abstract void beginAction();


    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void onResume() {
        super.onResume();
        isShow = true;
    }

    public void onPause() {
        super.onPause();
        isShow = false;
    }

    @Subscribe
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            KGEventBus.unregister(this);
        }
    }

    // 初始化views
    protected void findOldTitleView(View root) {
    }



    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示一个Toast类型的消息
     *
     * @param msg 显示的消息
     */
    @UiThread
    protected void showToast(String msg) {
        if (getActivity() == null) {
            return;
        }
        Toast mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 显示{@link Toast}通知
     *
     * @param strResId 字符串资源id
     */
    @UiThread
    protected void showToast(@StringRes int strResId) {
        if (getActivity() == null) {
            return;
        }
        Toast mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mToast.setText(strResId);
        mToast.show();
    }

    /**
     * 获取类名字
     *
     * @return
     */
    protected String getClassName() {
        return this.getClass().getName();
    }

    protected Date getUpdate() {
        long timestamp = DatePref.getInstance().getUpdateDate(getClassName());
        if (mUpdateDate == null) {
            mUpdateDate = new Date(timestamp);
        } else {
            mUpdateDate.setTime(timestamp);
        }
        return mUpdateDate;
    }

    protected void updateDate() {
        mUpdateDate = new Date();
        DatePref.getInstance().setUpdateDate(getClassName(), mUpdateDate.getTime());
    }

    /**
     * 获取时间格式
     *
     * @return
     */
    public String getDateFormat() {
        if (isAdded()) {
            return String.format(getString(R.string.xlistview_header_last_time),
                    DateUtil.twoDateDistance(getActivity(), mUpdateDate, new Date()));
        }
        return "刚刚";
    }

    public boolean isShow;

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        KGLog.e("infox", "setUserVisibleHint:" + getClassName());
    }


    public void OnLoginSuccess() {
    }

    public void OnLogoutSuccess() {
    }

    /**
     * 有歌曲收藏行为
     */
    public void OnNoticeCollect() {

    }




    public void fling(int initialVelocity) {
    }

    public void setLockResume(boolean lockResume) {
    }

    public void initScroller(ListView listview) {

    }

    public void toLogin() {
        if (choiceDialog == null) {
            choiceDialog = new TipsDialog(getActivity()).setLeftText("取消").setRightText("登录")
                    .setTips("该操作要登录才能使用哦!").setOnLeftClick(new TipsDialog.OnLeftClick() {
                        @Override
                        public void leftClick() {
                            choiceDialog.cancel();
                        }
                    }).setOnRightClick(new TipsDialog.OnRightClick() {
                        @Override
                        public void rightClick() {
                            ActivityUtils.gotoCheckActivity(getActivity(),0,true,false);
                            choiceDialog.cancel();
                        }
                    });
        }
        choiceDialog.show();
    }
}
