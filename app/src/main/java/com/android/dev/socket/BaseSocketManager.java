package com.android.dev.shop.socket;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-09.
 */

public abstract class BaseSocketManager {
    public static final String TAG = BaseSocketManager.class.getSimpleName();
    private Socket mSocket;
    public DataInputStream s_in;
    public DataOutputStream s_out;
    public SocketReceiver receiver;
    public SocketHeartBeat heart;
    //point 用于记录连接次数
    private int point = -1;
    public boolean isRunning = false;
    //初始化socket链接
    public void initSocket() throws IOException{
        point++;

        if(heart==null)
            heart = new SocketHeartBeat();
        heart.setManager(this);
        heart.startHeartBeatThread();
        List<SocketConnection> socketConnectionss = getSocketConnections(getRoomId());
        if(socketConnectionss==null || socketConnectionss.size()<=0){
            return;
        }
        Log.d(TAG,"开始连接");
        Socket socket = new Socket();
        int position = point%socketConnectionss.size();
        SocketConnection connection = socketConnectionss.get(position);
        SocketAddress innerAddress;
        innerAddress = new InetSocketAddress(connection.IP,Integer.valueOf(connection.port));
        mSocket.connect(innerAddress,15*1000);
        mSocket.setKeepAlive(true);
        mSocket.setTcpNoDelay(true);
        //获取与服务器的通信信道
        s_out = new DataOutputStream(mSocket.getOutputStream());
        s_in = new DataInputStream(mSocket.getInputStream());
        String body = "";

        try {
            JSONObject bodyjson = new JSONObject().put("roomId",getRoomId()).put("userId",getUserId()).put("platId",getPlatId());
            body = bodyjson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendClicentPackage(1000,1,0,body);
        isRunning = true;
        if(receiver==null)
            receiver = new SocketReceiver();
        receiver.setManager(this);
        receiver.startReceiveThread();
        point --;
    }

    public BaseSocketManager(){

    }


    public abstract int getRoomId();
    public abstract List<SocketConnection> getSocketConnections(int roomId);
    public abstract int getUserId();
    public abstract String getPlatId();
    public abstract void dealReceiveMsg(String receiveMsg);


    public void sendClicentPackage(int svrId,int cmdId,int flag,String body)throws IOException{
        int packeSize = body.getBytes().length + 4*2+2*2;
        Log.d(TAG,"ps:--->"+packeSize);
        Log.d(TAG,"body:--->"+body);
        s_out.writeInt(svrId);
        s_out.writeInt(cmdId);
        s_out.writeInt(flag);
        s_out.writeBytes(body);
        s_out.flush();
    }

    public boolean isSocketConnected(){
        return mSocket!=null && mSocket.isConnected();
    }

    private void close(){
        Log.d(TAG,">----close");
        isRunning = false;
        try {
            if(mSocket!=null&&mSocket.isConnected()){
                receiver.stopReceiveThread();
                s_in.close();
                s_out.close();
                s_out.flush();
                mSocket.close();
                mSocket =null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mSocket = null;
            if(heart!=null){
                heart.stopHeartThread();
            }
            s_out = null;
            s_in = null;
        }
    }

    public DataInputStream getReader(){
        return s_in;
    }

    public DataOutputStream getWriter(){
        return s_out;
    }
}
