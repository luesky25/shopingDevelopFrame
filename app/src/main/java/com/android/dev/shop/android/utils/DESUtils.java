package com.android.dev.shop.android.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtils {
	
	
	
	public static final String DES_KEY = "105154bc";
	public static final String DES_VI = "ringhsss";
	
	
	/**
	 * DES加密算法（CBC）
	 * @param data
	 * @param password
	 * @return
	 */
	public static String Encrypt(String sSrc) {
		try {
			//返回实现指定转换的 Cipher 对象
	        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); //“算法/模式/填充”
	        //创建一个 DESKeySpec 对象，使用 8 个字节的key作为 DES 密钥的密钥内容。
	        DESKeySpec desKeySpec = new DESKeySpec(DES_KEY.getBytes("UTF-8"));
	        //返回转换指定算法的秘密密钥的 SecretKeyFactory 对象。
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        //根据提供的密钥生成 SecretKey 对象。
	        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	        //使用 iv 中的字节作为 IV 来构造一个 IvParameterSpec 对象。复制该缓冲区的内容来防止后续修改。
	        IvParameterSpec iv = new IvParameterSpec(DES_VI.getBytes());
	        //用密钥和一组算法参数初始化此 Cipher;Cipher：加密、解密、密钥包装或密钥解包，具体取决于 opmode 的值。
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
	        //加密同时解码成字符串返回（十六进制）
	        return byteArrToHexStr(cipher.doFinal(sSrc.getBytes("UTF-8")));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * DES解密算法（CBC）
	 * @param decodeString
	 * @param decodeKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptDES(String decodeString) throws Exception {
		try {
			//使用指定密钥构造IV
			IvParameterSpec iv = new IvParameterSpec(DES_VI.getBytes());
			//根据给定的字节数组和指定算法构造一个密钥。 
			SecretKeySpec skeySpec = new SecretKeySpec(DES_KEY.getBytes(), "DES");
			//返回实现指定转换的 Cipher 对象
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			//解密初始化
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			//解码返回
			byte[] byteMi = hexStrToByteArr(decodeString);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
     * 将byte数组转换为表示16进制值的字符串，如：byte[]{8,18}转换为：0813， 和public static byte[] 
     * hexStrToByteArr(String strIn) 互为可逆的转换过程 
     * @param arrB 
     *            需要转换的byte数组 
     * @return 转换后的字符串 
     */  
    public static String byteArrToHexStr(byte[] arrB) {  
        int iLen = arrB.length;  
        // 每个byte(8位)用两个(16进制)字符才能表示，所以字符串的长度是数组长度的两倍  
        StringBuffer sb = new StringBuffer(iLen * 2);  
        for (int i = 0; i < iLen; i++) {  
            int intTmp = arrB[i];  
            while (intTmp < 0) {// 把负数转换为正数  
                intTmp = intTmp + 256;  
            }  
            if (intTmp < 16) {// 小于0F的数需要在前面补0  
                sb.append("0");  
            }  
            sb.append(Integer.toString(intTmp, 16));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 将表示16进制值的字符串转换为byte数组，和public static String byteArrToHexStr(byte[] arrB) 
     * 互为可逆的转换过程 
     * @param strIn 
     *            需要转换的字符串 
     * @return 转换后的byte数组 
     */  
    public static byte[] hexStrToByteArr(String strIn) {  
        byte[] arrB = strIn.getBytes();  
        int iLen = arrB.length;
        // 两个(16进制)字符表示一个字节(8位)，所以字节数组长度是字符串长度除以2  
        byte[] arrOut = new byte[iLen / 2];  
        for (int i = 0; i < iLen; i = i + 2) {  
            String strTmp = new String(arrB, i, 2);  
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);  
        }  
        return arrOut;  
    }  
  
	
}
