
package com.android.dev.framework.component.base;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.android.dev.shop.R;


/**
 * 描述:可处理耗时操作的fragment
 *
 * @author xuhaichao
 * @since 2013-8-12 上午11:51:02
 */
public abstract class BaseWorkerFragment extends BaseFragment {

    protected HandlerThread mHandlerThread;

    protected BackgroundHandler mBackgroundHandler;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit();
        }
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


    /**
     * 延时发送后台操作，delayMillis为延时发送
     */
    public void sendBackgroundMessageDelayed(Message msg, long delayMillis) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendMessageDelayed(msg, delayMillis);
        }
    }

    /**
     * 延时发送后台操作，delayMillis为延时发送
     */
    public void sendEmptyBackgroundMessageDelayed(int what, long delayMillis) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    /**
     * 移除消息队列对应的消息 
     */
    public void removeBackgroundMessages(int what) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeMessages(what);
        }
    }

    /**
     * 从消息池获取一个msg实例
     */
    public Message obtainBgMessage() {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.obtainMessage();
        }
        return new Message();
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
    
   public void  showLoadingRingView(View view){
       if (view!=null) {
           ImageView mImageViewFilling = (ImageView) view.findViewById(R.id.show_loading);
           AnimationDrawable animationDrawable = (AnimationDrawable) mImageViewFilling.getBackground();
           if (animationDrawable.isRunning()) {
               //停止动画播放  
               animationDrawable.stop();
           } else {
               //开始或者继续动画播放  
               animationDrawable.start();
           }
       }
   }
//    public void  stopLoadingRingView(View view){
//        ImageView mImageViewFilling = (ImageView)view.findViewById(R.id.show_loading);
//        ((AnimationDrawable) mImageViewFilling.getBackground()).stop();
//    }
}
