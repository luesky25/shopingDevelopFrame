package com.android.dev.shop.socket;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2018-03-09.
 */

public class SocketHeartBeat {
    public static final String TAG = SocketReceiver.class.getSimpleName();
    private BaseSocketManager manager;
    public static final int HEART_BEAT_SLEEP_TIME = 10*1000;
    public Timer timer;
    public TimerTask timerTask;


    public void setManager(BaseSocketManager manager){
        this.manager = manager;
    }

    public  void startHeartBeatThread(){
        if(timer==null){
            timer = new Timer();
            if(timerTask==null){
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if(getManager().isRunning){
                            try {
                                int ps = 12;
                                getManager().s_out.writeInt(ps);
                                getManager().s_out.writeShort(1000);
                                getManager().s_out.writeShort(3);
                                getManager().s_out.writeInt(0);
                                getManager().s_out.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                                getManager().isRunning = false;
                            }
                        }else{
                            try {
                                Log.d(TAG,"initSocket false");
                                getManager().initSocket();
                            } catch (IOException e) {
                                e.printStackTrace();
                                getManager().isRunning = false;
                            }
                        }


                    }
                };
                timer.schedule(timerTask,HEART_BEAT_SLEEP_TIME,HEART_BEAT_SLEEP_TIME);
            }


        }
    }

    public void stopHeartThread(){
        if(timer!=null){
            timer.cancel();
        }
        if(timerTask!=null){
            timerTask.cancel();
        }
        timerTask = null;
        timer = null;
    }


    public BaseSocketManager getManager(){
        return manager;
    }

}
