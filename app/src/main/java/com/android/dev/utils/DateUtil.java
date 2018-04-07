package com.android.dev.utils;


import android.content.Context;

import com.android.dev.shop.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 描述: 日期操作类
 * 
 * @author chenjinyuan
 * @since 2013-7-22 下午3:19:42
 */
public class DateUtil {

    public static final String format1 = "yyyy-MM-dd";

    public static final String format2 = "yyyy-MM-dd HH:mm:ss";

    public static final String format3 = "HH:mm:ss";

    public static final String format4 = "yyyy-MM-dd HH";

    public static final String format5 = "yyyyMMddHHmmss";

    public static final String format6 = "yyyy年MM月dd日";

    public static final String format7 = "yyyy-MM-dd'T'HH:mm:ss";

    private static SimpleDateFormat format = null;

    private static long nd = 1000 * 24 * 60 * 60; // 一天的毫秒数

    /**
     * 获取当前时间字符串 格式是：YYYY-MM-DD HH.mm
     * 
     * @return
     */
    public static String getCurrentTime() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date());
    }

    /**
     * 根据提供时间格式和时间，返回指定时间格式字符串
     * 
     * @param format 时间格式
     * @param longTime 时间长度
     * @return
     */
    public static String getFormatedTime(String pattern, long longTime) {
        format = new SimpleDateFormat(pattern);
        return format.format(new Date(longTime));
    }

    /**
     * 计算两个日期型的时间相差多少时间
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public static String twoDateDistance(Context context, Date startDate, Date endDate) {

        if (startDate == null || endDate == null) {
            return null;
        }
        return twoDateDistance(context, startDate.getTime(), endDate.getTime());
    }

    /**
     * 字符串转化成日期
     * 
     * @param date 日期字符串
     * @param format 日期格式
     * @return
     */
    public static Date convert(String date, String format) {
        Date retValue = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            retValue = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    public static String getDateByToday(long startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return sdf.format(startTime);
    }

    public static String twoDateDistance(Context context, long startTime, long endTime) {
        if (isYeaterday(new Date(startTime), new Date(endTime)) == 0) {
            return context.getString(R.string.time_yesterday_ago);
        } else if (isYeaterday(new Date(startTime), new Date(endTime)) == 1) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = format.format(startTime);
            try {
                Date today = format.parse(todayStr);
                startTime = today.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        long timeLong = endTime - startTime;
        timeLong = Math.max(timeLong, 0);
        if (timeLong < (60 * 1000))
            return context.getString(R.string.time_seconds_ago);
        else if (timeLong < (60 * 60 * 1000)) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + context.getString(R.string.time_minutes_ago);
        } else if (timeLong < (60 * 60 * 24 * 1000)) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + context.getString(R.string.time_hours_ago);
        } else if (timeLong < (60 * 60 * 48 * 1000)) {
            return context.getString(R.string.time_yesterday_ago);
        } else {
            long date = timeLong / nd;
            // if (date < 3) {
            // return context.getString(R.string.time_before_yesterday);
            // } else if (date <= 30) {
            // return date + context.getString(R.string.time_before_day);
            // } else {
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            // return sdf.format(startTime);
            // }

            // 不包括几天前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startTime);
        }
    }

    /**
     * @author LuoB.
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空 默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一天. 0：昨天 . 1 ：至少是前天.
     * @throws ParseException 转换异常
     */
    public static int isYeaterday(Date oldTime, Date newTime) {
        if (newTime == null) {
            newTime = new Date();
        }
        // 将下面的 理解成 yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today;
        try {
            today = format.parse(todayStr);
            // 昨天 86400000=24*60*60*1000 一天
            if ((today.getTime() - oldTime.getTime()) > 0
                    && (today.getTime() - oldTime.getTime()) <= 86400000) {
                return 0;
            } else if ((today.getTime() - oldTime.getTime()) <= 0) { // 至少是今天
                return -1;
            } else { // 至少是前天
                return 1;
            }
        } catch (ParseException e) {

        }
        return -1;
    }

}
