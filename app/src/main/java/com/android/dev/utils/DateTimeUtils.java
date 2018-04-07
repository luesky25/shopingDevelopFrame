package com.android.dev.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {

	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/***
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String getCurDate() {

		Date date = new Date();
		return format.format(date);
	}

	/**
	 * 判断是有效时间
	 *
	 * @param date1
	 *            需判断的时间
	 * @param date2
	 *            基准时间
	 * @param diff
	 *            时间差
	 * @return
	 */
	public static boolean isAvailableDate(Date date1, Date date2, int diff) {
		return (date2.getTime() - date1.getTime()) / 1000 <= diff;
	}

	/**
	 * 格式化时间
	 *
	 * @param time
	 *            分
	 * @return
	 */
	public static String getMin(long time) {
		long min = time / 60;
		return min < 10 ? "0" + min : String.valueOf(min);
	}

	/**
	 * 格式化时间
	 *
	 * @param time
	 *            秒
	 * @return
	 */
	public static String getSec(long time) {
		long sec = time % 60;
		return sec < 10 ? "0" + sec : String.valueOf(sec);
	}

	/**
	 * 格式化时间
	 *
	 * @param date
	 *            2010 1-2 4:31
	 * @return 2010 01-02 04:31
	 */
	public static String formatCreateTime(String date) {
		String time = date.substring(date.indexOf(" ")).trim();
		if (time.substring(0, time.indexOf(":")).length() == 1) {
			time = "0" + time;
		}

		String year = date.substring(0, 4) + "-";

		int i = date.indexOf("-");
		int t = date.lastIndexOf("-");
		String month = date.substring(i + 1, t);
		if (month.length() == 1) {
			month = "0" + month;
		}

		String day = date.substring(t + 1, date.indexOf(" "));
		if (day.length() == 1) {
			day = "0" + day;
		}

		date = year + month + "-" + day + " " + time;
		return date;
	}

	/**
	 * 设置时间格式为，例如：2013.03.12
	 *
	 * @param time
	 * @return
	 */
	public static String changeTimeformat(String time) {
		StringBuffer sb = new StringBuffer();
		String str = time.substring(0, 4);
		sb.append(str + ".");
		str = time.substring(4, 6);
		sb.append(str + ".");
		str = time.substring(6, 8);
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 获取money的格式
	 *
	 * @param money
	 * @return
	 */
	public static Float getMoneyFormat(String money) {
		StringBuffer sb = new StringBuffer();
		int parseInt = Integer.parseInt(money);
		if (parseInt >= 100) {
			String str = money.substring(0, money.length() - 2);
			sb.append(str + ".");
			str = money.substring(money.length() - 2, money.length());
			sb.append(str);
		} else {
			sb.append("0.");
			sb.append(money);
		}
		Float aa = Float.parseFloat(sb.toString());
		return aa;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy.MM.dd
	 *
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy.MM.dd
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToMyDate(String strDate) {
		if (strDate.length() >= 8) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = formatter.parse(strDate, pos);
			return strtodate;
		}
		return null;
	}

}
