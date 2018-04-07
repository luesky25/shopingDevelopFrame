package com.android.dev.shop.socket;

import android.util.Log;

import java.io.IOException;

/**
 * Created by Administrator on 2018-03-09.
 */

public class SocketReceiver implements Runnable{
    public static final String TAG = SocketReceiver.class.getSimpleName();
    public static final int RECEIVE_SLEEP_TIME = 100;
    public static final int MAX_PACKAGE_SIZE = 10*1024;
    public static final int SOCKET_PACK_HEAD_LENGTH = 12;
    public static final int MAX_TCP_PACK = 512;

    private boolean isStart;
    private boolean isInFinish;

    private Thread revThread = null;

    BaseSocketManager manager;
    public boolean isRunning;

    public void setManager(BaseSocketManager manager){
        this.manager = manager;
    }

    public void startReceiveThread(){
        if(!isStart){
            while (isInFinish){
                try {
                    Thread.sleep(RECEIVE_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(revThread==null){
            revThread = new Thread(this);
        }
        revThread.start();
        isStart = true;
    }

    public void stopReceiveThread(){
        if(revThread!=null){
            isStart = false;
            isInFinish = true;
        }
    }

    protected String receiveMessage()throws Exception{
        if(manager.isRunning){
            int packeSize = manager.getReader().readInt();
            if(packeSize>MAX_PACKAGE_SIZE){
                throw new RuntimeException("packsize is too large");//避免服务器写入的流过多，导致前端奔溃
            }
            Log.d(TAG,"packsize--->" + packeSize);
            short svrId = manager.getReader().readShort();
            short cmdId = manager.getReader().readShort();
            int flag = manager.getReader().readInt();

            int packSize = manager.getReader().readInt();

            StringBuilder stringBuilder = new StringBuilder();
            packeSize -= SOCKET_PACK_HEAD_LENGTH;//减去头部大小后面是body？
            while (packeSize>0){
                int buffersize = packeSize>MAX_TCP_PACK?MAX_TCP_PACK :packeSize;
                byte[] bypes = new byte[buffersize];
                int read = manager.getReader().read(bypes);
                stringBuilder.append(new String(bypes,buffersize));
                packeSize -= read;
            }

            if(flag!=0){
                //说明经过压缩，事先定义好？
            }
            String msg = stringBuilder.toString();
            Log.d(TAG,"receive msg== " +msg );
            if(cmdId==2){//定义好一种协议，说明不是需要的接收，是系统其它消息？
                return null;
            }else{
                return msg;
            }
        }else{
            Log.d(TAG,"recevier manager not running");
            return null;
        }
    }

    @Override
    public void run() {
        while (manager.isRunning && isStart){
            try {
                if(manager.isSocketConnected()){
                    String receiveMsg = receiveMessage();
                    if(receiveMsg!=null){
                        manager.dealReceiveMsg(receiveMsg);
                    }


                }else{
                    if(manager.isRunning){
                            manager.initSocket();
                        Log.d(TAG,"manager not connected 发现断链");
                    }
                }
                Thread.sleep(RECEIVE_SLEEP_TIME);
            } catch (Exception e) {
                e.printStackTrace();
                if(manager.isRunning){
                     Log.d(TAG,"receive 异常");
                    manager.isRunning = false;
                    break;//异常了就退出循环体，不然死循环处理错误信息，一直报错

                }
            }
        }
        isStart = false;
        isInFinish = true;
        revThread = null;
    }
}
