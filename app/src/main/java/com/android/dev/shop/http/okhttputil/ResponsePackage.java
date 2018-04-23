package com.android.dev.shop.http.okhttputil;

/**
 * 响应包
 * 
 * @author chenys
 * @param <T>
 */
public interface ResponsePackage<T extends Object> {

    /**
     * 设置返回的字符串
     * 
     * @param s
     */
    public void setContext(byte[] data);

    
    public void setStatusCode(int statusCode);
    /**
     * 服务器返回错误信息
     * 
     * @param data
     */
    public void setResponseError(byte[] data);

    /**
     * 获取返回对象集合
     * 
     * @return
     */
    public void getResponseData(T t);

}
