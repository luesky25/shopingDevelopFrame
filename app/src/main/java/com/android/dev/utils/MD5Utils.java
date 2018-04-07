package com.android.dev.utils;

import com.android.common.utils.KGLog;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MD5Utils {

	
	public final static String getMD5ofStr(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes("utf-8");
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式 
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }  
            //返回经过加密后的字符串  
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
	
	public final static String getMD5ofByte(byte[] pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd;
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式 
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }  
            //返回经过加密后的字符串  
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
	
	public static final String TOKEN_KEY = "p?US?2EPu5RUChuwA@2?rEp!br6KeJu+";
   /* *//**
     * 获取数字签名
     * @return
     *//*
	@Deprecated
    public static String getToken(List<String> data) {
    	//按字典序排序请求参数
    	Collections.sort(data);
    	StringBuilder builder = new StringBuilder(TOKEN_KEY);
    	for (String str : data) {
    		builder.append(StringUtils.string2Unicode(str));
    	}
    	builder.append(TOKEN_KEY);
    	return MD5Utils.getMD5ofStr(builder.toString());
    } */
    
    /**
     * 获取数字签名
     * @param map
     * @param bracketAsArray   true---[xx]的内容是数组不计算在md5里面，false---[xxxx]内容是报文，计算在md5里面
     * @return
     */
    public static void dealToken(Map<String, String> map,boolean bracketAsArray) {
    	List<String> list = new ArrayList<String>();
    	for (String str : map.keySet()) {
    		//如[xx,xx,xxx]类型数据，不进行MD5，兼容IOS
    		if (bracketAsArray &&
    				map.get(str).startsWith("[") && map.get(str).endsWith("]"))
    			continue;
    		list.add(str + map.get(str));
    	}
    	//按字典序排序请求参数
    	Collections.sort(list);
    	StringBuilder builder = new StringBuilder(TOKEN_KEY);
    	for (String str : list) {
//    		builder.append(StringUtils.string2Unicode(str));
    		builder.append(str);
    	}
    	builder.append(TOKEN_KEY);
    	String result = MD5Utils.getMD5ofStr(builder.toString());
    	map.put("token", result);
    	KGLog.d("url", "token处理成功" + map.toString());
    } 
	
    /**
     * 获取数字签名,默认是,[xx]的内容是数组不计算在md5里面
     * @param map
     * @return
     */
    public static void dealToken(Map<String, String> map) {
        dealToken(map,true);
    } 
}
