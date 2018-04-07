package com.android.dev.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.TypedValue;


import com.android.dev.shop.android.model.KeyWordPos;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作类，如截取、合并等操作
 */
public class StringUtil {

    /**
     * 解析文件所在的文件夹
     *
     * @param filePath 文件路径
     * @return 文件所在的文件夹路径
     */
    public static String getFileFolderPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int last = filePath.lastIndexOf("/");
        if (last == -1) {
            return null;
        }
        return filePath.substring(0, last + 1);
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dipToPx(Context context, int dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context
                .getResources().getDisplayMetrics());
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || "null".equals(s);
    }

    /**
     * 解析文件全名,不带扩展名
     *
     * @param filePath 文件路径
     * @return 文件全名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int last = filePath.lastIndexOf("/");
        int index = filePath.lastIndexOf(".");
        if (last == -1 && index == -1) {
            return filePath;
        } else if (index > last) {
            return filePath.substring(last + 1, index);
        } else {
            return filePath.substring(last);
        }
    }

    /**
     * 对乱码进行转码
     *
     * @param s
     * @return
     */
    public static String errEncode(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(s);
        if (!matcher.find()) {
            try {
                return s = new String(s.getBytes("iso-8859-1"), "GBK");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return s;
    }

    /**
     * 是否乱码
     *
     * @param s
     * @return true 乱码
     */
    public static boolean isErrCode(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(s);
        return !matcher.find();
    }

    /**
     * 小于10的正整数前面补0
     *
     * @param i
     * @return
     */
    public static String add0IfLgTen(int i) {
        if (0 < i && i < 10) {
            return "0" + i + ".";
        } else {
            return i + ".";
        }
    }

    /**
     * 格式化文件路径（去除一些特殊字符）
     *
     * @param filePath
     * @return
     */
    public static String formatFilePath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return filePath.replace("\\", "").replace("/", "").replace("*", "").replace("?", "")
                .replace(":", "").replace("\"", "").replace("<", "").replace(">", "")
                .replace("|", "");
    }

    /**
     * 单位换算
     *
     * @param fileSize
     * @return
     */
    public static String getSizeText(long fileSize) {
        if (fileSize <= 0) {
            return "0.0M";
        }
        if (fileSize > 0 && fileSize < 100 * 1024) {
            // 大于0小于100K时，直接返回“0.1M”（产品需求）
            return "0.1M";
        }
        float result = fileSize;
        String suffix = "M";
        result = result / 1024 / 1024;
        return String.format("%.1f", result) + suffix;
    }

    /**
     * 返回文件大小表示
     *
     * @param context
     * @param bytes 字节数
     * @return
     */
    public static String getSizeText(Context context, long bytes) {
        String sizeText = "";
        if (bytes < 0) {
            return sizeText;
        } else {
            sizeText = Formatter.formatFileSize(context, bytes);
        }
        return sizeText;

    }

    /**
     * 截取图片名称
     *
     * @param filePath
     * @return
     */
    public static String spiltImageName(String imageurl) {
        if (TextUtils.isEmpty(imageurl)) {
            return null;
        }
        imageurl = imageurl.toLowerCase();
        int start = imageurl.lastIndexOf("filename");
        if (start == -1) {
            start = imageurl.lastIndexOf("/");
            if (start == -1) {
                return null;
            } else {
                start += 1;
            }
        } else {
            start += 9;
        }
        int end = imageurl.indexOf(".jpg", start);
        if (end == -1) {
            end = imageurl.indexOf(".png", start);
            if (end == -1) {
                return null;
            } else {
                end += 4;
            }
        } else {
            end += 4;
        }
        return imageurl.substring(start, end);
    }

    public static String getExceptionString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replace("\n", "<br />");
    }

    /**
     * 获取文件名，带后缀名
     *
     * @param filePath
     * @return
     */
    public static String getShortFileName(String filePath) {
        if (filePath == null) {
            return null;
        }
        if (filePath.indexOf("/") == -1) {
            return filePath;
        }
        int begin = filePath.lastIndexOf("/");
        return filePath.substring(begin + 1);
    }

    /**
     * 把IMEI用一大整数代替
     *
     * @param imei
     * @return
     */
    public static String imeiToBigInteger(String imei) {
        BigInteger result = new BigInteger("0");
        try {
            BigInteger n = new BigInteger("16");
            String md5 = new MD5Utils().getMD5ofStr(imei);
            int size = md5.length();
            for (int i = 0; i < size; i++) {
                BigInteger a = new BigInteger("" + md5.charAt(i), 16);
                BigInteger b = n.pow(size - 1 - i);
                result = result.add(a.multiply(b));
            }
            return result.toString();
        } catch (Exception e) {
        }
        return result.toString();
    }

    /**
     * 找出关键字是否在目标数字串中存在
     *
     * @return
     */
    public static KeyWordPos indexOfKeyWord(String pinyinSimpleStr, String keyWord) {
        int startPos = -1;
        int endPos = -1;
        byte[] byte_pinyinSimpleStr = pinyinSimpleStr.getBytes();
        byte[] byte_keyWord = keyWord.getBytes();
        // 遍历目的串的字节数组
        for (int i = 0; i < byte_pinyinSimpleStr.length; i++) {

            // 遍历关键字的字节数组
            for (int j = 0; j < byte_keyWord.length; j++) {
                // startPos=-1代表一个字符都没匹配到
                if (startPos == -1) {
                    if (byte_pinyinSimpleStr[i] == byte_keyWord[j]) {
                        startPos = i;
                        endPos = i;
                    }
                } else {
                    // 如果是#的话，则跳过,搜索下一个
                    if (byte_pinyinSimpleStr[i] == ((int) '#')) {
                        continue;
                    } else {
                        // 一旦出现字符不匹配，则可以认为是字符串搜索不命中
                        if (byte_pinyinSimpleStr[i] != byte_keyWord[j]) {
                            return null;
                        } else
                            endPos = i;
                    }
                }

            }
        }
        // if(startPos!=-1&& endPos!=-1)
        // return new
        // return new KeyWordPos();
        return null;

    }
    /**
     * 判断输入的密码是否是数字，字母，符号(不包括空格，密码中不允许出现空格)的组合,6~16位 如果strInput为空直接返回false
     *
     * @param strInput
     *            输入的字符串
     * @return 是否符合
     */
    public static boolean isValidPassword(String strInput) {
        if (isEmpty(strInput)) {
            return false;
        }
        String strPattern = "^[0-9a-zA-Z-`=\\\\|\\[\\];',.\\/~!@#$%^&*()_+{}:<>?]{6,16}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strInput);
        return m.matches();
    }
}
