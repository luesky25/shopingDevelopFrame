
package com.android.dev.framework.component.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.android.dev.shop.http.biz.HttpManagerFactory;

/**
 * 描述 具备后台线程和UI线程更新
 *
 * @author chenjinyuan
 * @since 2013-12-2 上午9:45:00
 */
public abstract class BaseWorkerActivity extends BaseActivity {
    /**
     * 请求工厂类
     */
    private HttpManagerFactory mFactory;
    /**
     * 异步请求
     */
    private HandlerThread mHandlerThread;

    private BackgroundHandler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFactory = new HttpManagerFactory(this);
        mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFactory!=null){
            mFactory.cancelAll();
        }
        if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit();
        }
    }

    public HttpManagerFactory getVolleyFactory() {
        return mFactory;
    }


    /**
     * 处理后台操作
     */
    protected abstract void handleBackgroundMessage(Message msg);

    /**
     * 发送后台操作
     *
     * @param msg
     */
    protected void sendBackgroundMessage(Message msg) {
        if (mBackgroundHandler != null) {

            mBackgroundHandler.sendMessage(msg);
        }
    }

    /**
     * 发送后台操作
     *
     * @param what
     */
    protected void sendEmptyBackgroundMessage(int what) {
        if (mBackgroundHandler != null) {

            mBackgroundHandler.sendEmptyMessage(what);
        }
    }

    // 后台Handler
    @SuppressLint("HandlerLeak")
    public class BackgroundHandler extends Handler {

        BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleBackgroundMessage(msg);
        }
    }
}
