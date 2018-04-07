package com.android.dev.shop.http.biz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-11.
 */

public class HttpManagerFactory {
    private final List<HttpBaseManager> managerList;
    private final String tag;

    public static final int HTTP_USER_MANAGER = 0x001;
    public static final int HTTP_UPLOAD_MANAGER = 0x002;


    public HttpManagerFactory(Object instance) {
        managerList = new ArrayList<HttpBaseManager>();
        tag = instance.getClass().getSimpleName();
    }

    public HttpManagerFactory(Object instance, String tag) {
        managerList = new ArrayList<HttpBaseManager>();
        this.tag = tag;
    }

    public HttpBaseManager getInstance(int type) {
        HttpBaseManager manager = null;
        switch (type) {
            case HTTP_USER_MANAGER:
                manager = new HttpUserManager(tag);
                break;
            case HTTP_UPLOAD_MANAGER:
                manager = new HttpUploadManager(tag);
                break;
        }
        if (manager != null)
            managerList.add(manager);
        return manager;
    }


    public void cancelAll() {
        for (HttpBaseManager manager : managerList) {
            manager.cancelAll();
        }
    }
}
