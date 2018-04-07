
package com.android.dev.framework.component.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.reflect.Field;


/**
 * 描述 具备后台线程和UI线程更新
 *
 * @author chenjinyuan
 * @since 2013-12-2 上午9:45:00
 */
public class BaseActivity extends FragmentActivity {

    private Toast mToast;

    protected HandlerThread mHandlerThread;

    protected BaseWorkerActivity.BackgroundHandler mBackgroundHandler;

    protected Handler mUiHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            handleUiMessage(msg);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // MobclickAgent.onError(this);
    }

    /**
     * 处理更新UI任务
     *
     * @param msg
     */
    protected void handleUiMessage(Message msg) {
    }

    /**
     * 发送UI更新操作
     *
     * @param msg
     */
    protected void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }

    /**
     * 发送UI更新操作
     *
     * @param what
     */
    protected void sendEmptyUiMessage(int what) {
        mUiHandler.sendEmptyMessage(what);
    }

    /**
     * 显示一个Toast类型的消息
     *
     * @param msg 显示的消息
     */
    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 显示{@link Toast}通知
     *
     * @param strResId 字符串资源id
     */
    public void showToast(int strResId) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(strResId);
        mToast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        if (BaseActivity.this.getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 控制软键盘的显示隐藏
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    /**
     * 添加整个界面的Fragment
     *
     * @param f
     * @param toBack 控制返回
     */
    public void attachFragmentToContent(Fragment f, boolean toBack) {
        attachFragment(android.R.id.content, f, toBack);
    }

    /**
     * 添加Fragment到指定LayoutID位置
     *
     * @param f
     * @param toBack 控制返回
     */
    public void attachFragment(int layoutID, Fragment f, boolean toBack) {
        commitFragment(f, layoutID, toBack, f.getClass().getName(), true);
    }

    private void commitFragment(Fragment pageFragment, int layoutID, boolean toBack, String tag, boolean isRetry) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        try {
            if (layoutID == android.R.id.content){
                transaction.replace(layoutID, pageFragment, tag);
            }else {
                transaction.add(layoutID, pageFragment, tag);
            }
            if (toBack) {
                transaction.addToBackStack(tag);
                transaction.commit();
            } else {
                transaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            if (isRetry) {
                try {
                    Field mStateSaved = manager.getClass().getDeclaredField("mStateSaved");
                    mStateSaved.setAccessible(true);
                    mStateSaved.set(manager, false);
                    commitFragment(pageFragment, layoutID, toBack, tag, false);
                    mStateSaved.set(manager, true);
                } catch (Exception e_) {
                    e_.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取Fragment通过Class
     *
     * @param tClass
     * @return
     */
    public <T extends Fragment> T findFragmentByClass(Class<T> tClass) {
        return (T) getSupportFragmentManager().findFragmentByTag(tClass.getName());
    }

    /**
     * 获取Fragment通过tag
     *
     * @param tag
     * @return
     */
    public Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    /**
     * 获取Fragment通过tag
     *
     * @param tag
     * @return
     */
    public Fragment findChildFragmentByTag(Fragment fragment,String tag) {
        return fragment.getChildFragmentManager().findFragmentByTag(tag);
    }
}
