package com.android.dev.shop.socket;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.dev.shop.basics.BaseEntity;

/**
 * Created by Administrator on 2018-03-09.
 */

public class SocketConnection implements Parcelable,BaseEntity{
    public String IP;
    public String port;
    public String web_port;


    public static final Creator<SocketConnection> CREATOR = new Creator<SocketConnection>() {
        @Override
        public SocketConnection createFromParcel(Parcel in) {
            SocketConnection socketConnection = new SocketConnection();
            socketConnection.IP = in.readString();
            socketConnection.port = in.readString();
            socketConnection.web_port = in.readString();
            return socketConnection;
        }

        @Override
        public SocketConnection[] newArray(int size) {
            return new SocketConnection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(IP);
        parcel.writeString(port);
        parcel.writeString(web_port);
    }

    @Override
    public String toString() {
        return "socketConnection:{"+"ip="+IP +"\'"+"port=" + port+"\'"+"web_port=" + web_port +"}";
    }
}
