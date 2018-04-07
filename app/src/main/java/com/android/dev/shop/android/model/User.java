package com.android.dev.shop.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018-03-25.
 */

public class User implements Parcelable {
    private String name;
    private String gender;
    private String icon;

    private User(Parcel in) {
        this.name = in.readString();
        this.gender = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
