//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.android.dev.shop.http.okhttputil;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestEntity {
    boolean isRepeatable();

    void writeRequest(OutputStream var1) throws IOException;

    long getContentLength();

    String getContentType();
}
