package com.android.dev.shop.android.base;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

import com.android.common.utils.KGLog;

/**
 * 可做耗时操作的界面
 *
 * @author ldx
 *
 */
public abstract class BaseBackgroundActivity extends BaseActivity {

    /**
     * Ui handler
     */
    protected Handler mUiHandler;

    protected HandlerThread mHandlerThread;

    /**
     * 后台handler
     */
    protected BackgroundHandler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleUiMessage(msg);
            }
        };
        mHandlerThread = new HandlerThread("base thread");
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KGLog.d("xhc", this.getClass().getName());
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeCallbacksAndMessages(null);
            mBackgroundHandler.getLooper().quit();
        }
    }

    /**
     * 后台线程类
     *
     * @author ldx
     */
    public class BackgroundHandler extends Handler {

        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleBackgroundMessage(msg);
        }
    }

    /**
     * 处理更新UI任务
     *
     * @param msg
     */
    protected abstract void handleUiMessage(Message msg);

    /**
     * 处理后台耗时任务
     *
     * @param msg
     */
    protected abstract void handleBackgroundMessage(Message msg);

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int current= mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );;
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                //音乐音量
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current-1, 1);;
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current+1, 1);;
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 发送UI更新操作
     *
     * @param msg
     */
    protected void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }

    protected void sendUiMessage(Object object, int what) {
        if (mUiHandler != null) {
            Message message = mUiHandler.obtainMessage();
            message.what = what;
            message.obj = object;
            mUiHandler.sendMessage(message);
        }
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

}
