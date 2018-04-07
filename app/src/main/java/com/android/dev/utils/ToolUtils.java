package com.android.dev.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.dev.shop.R;
import com.android.statistics.NetworkType;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class ToolUtils {
	private static Drawable drawable = null;

	private static Handler makeMusicHandler;

	private final static double PI = 3.1415926;




	public static Handler getMakeMusicHandler() {
		return makeMusicHandler;
	}

	public static void setMakeMusicHandler(Handler makeMusicHandler) {
		ToolUtils.makeMusicHandler = makeMusicHandler;
	}

	enum _Quadrant{
		eQ_NONE,									//  在坐标轴上
		eQ_ONE,										//  第一象限
		eQ_TWO,										//	第二象限
		eQ_THREE,									//	第三象限
		eQ_FOUR										//	第四象限
	}
	/**
	 * 获取应用下载渠道
	 *
	 * @param activity
	 * @param key
	 * @return
	 */
	public static String getChannelName(Activity activity, String key) {
		String mFrom = "";
		try {
			ApplicationInfo mApplicationInfo = activity.getPackageManager()
					.getApplicationInfo(activity.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle mBundle = mApplicationInfo.metaData;
			if (TextUtils.isEmpty(key)) {
				key = "UMENG_CHANNEL";
			}
			mFrom = mBundle.getString(key);
			;
		} catch (NameNotFoundException e) {
			mFrom = "";
		} catch (NullPointerException e) {
			mFrom = "";
		}
		return mFrom;
	}

	public static Spanned getColorFormat(String data,String keyword) {
		if (TextUtils.isEmpty(keyword))
			return Html.fromHtml(data);
		String html = data.replace(keyword, "<font color=\"#11c379\">" + keyword + "</font>");
		return Html.fromHtml(html);
	}

	/**
	 * 获取手机号
	 *
	 * @param context
	 * @return
	 */
	public static String getPhoneNum(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}

	/**
	 * 获取当前的网络类型
	 *
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		try {
			if (context == null) {
				return NetworkType.UNKNOWN;
			}
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
				return NetworkType.UNKNOWN;
			}
			// 某些手机获取时候报空指针
			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo == null) {
				return NetworkType.UNKNOWN;
			}
			if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return NetworkType.WIFI;
			}
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm == null) {
				return NetworkType.UNKNOWN;
			}
			int netType = tm.getNetworkType();

			// 已知3G类型
			// NETWORK_TYPE_UMTS 3
			// NETWORK_TYPE_EVDO_0 5
			// NETWORK_TYPE_EVDO_A 6
			// NETWORK_TYPE_HSDPA 8
			// NETWORK_TYPE_HSUPA 9
			// NETWORK_TYPE_HSPA 10
			// NETWORK_TYPE_EVDO_B 12
			// NETWORK_TYPE_LTE 13
			// NETWORK_TYPE_EHRPD 14
			// NETWORK_TYPE_HSPAP 15

			// 已知2G类型
			// NETWORK_TYPE_GPRS 1
			// NETWORK_TYPE_EDGE 2
			// NETWORK_TYPE_CDMA 4
			// NETWORK_TYPE_1xRTT 7
			// NETWORK_TYPE_IDEN 11

			switch (netType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					return NetworkType.NET_2G;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					return NetworkType.NET_3G;
				case TelephonyManager.NETWORK_TYPE_LTE:
					return NetworkType.NET_4G;
				default:
					return NetworkType.UNKNOWN;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return NetworkType.UNKNOWN;
		}
	}

	public static boolean isWeiboInstalled(Context context) {
		PackageManager pm;
		if ((pm = context.getApplicationContext().getPackageManager()) == null) {
			return false;
		}
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		for (PackageInfo info : packages) {
			String name = info.packageName.toLowerCase(Locale.ENGLISH);
			if ("com.sina.weibo".equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param point
	 * @return
	 *
	 * 获得点所在角度（点与坐标轴原点连线与Y正半轴的顺时针夹角）单位为度数
	 */
	public static int GetRadianByPos(Point point){
		double dAngle = GetRadianByPosEx(point);

		return (int) (dAngle * (360 / (2 * PI)));
	}
	/**
	 * @param point
	 * @return
	 *
	 * 获得Point点所在象限
	 */
	public static _Quadrant GetQuadrant(Point point){
		if (point.x == 0 || point.y == 0)
		{
			return _Quadrant.eQ_NONE;
		}

		if (point.x > 0)
		{
			if (point.y > 0)
			{
				return _Quadrant.eQ_ONE;
			}
			else
			{
				return _Quadrant.eQ_TWO;
			}

		}
		else
		{
			if (point.y < 0)
			{
				return _Quadrant.eQ_THREE;
			}
			else
			{
				return _Quadrant.eQ_FOUR;
			}
		}
	}
	/**
	 * @param point
	 * @return
	 *
	 * 获得点所在角度（点与坐标轴原点连线与Y正半轴的顺时针夹角）单位为弧度
	 */
	private static double GetRadianByPosEx(Point point){

		if (point.x == 0 && point.y == 0)
		{
			return 0;
		}


		double Sin = point.x / Math.sqrt(point.x * point.x + point.y * point.y);
		double dAngle = Math.asin(Sin);

		switch(GetQuadrant(point))
		{
			case eQ_NONE:
			{
				if (point.x == 0 && point.y == 0)
				{
					return 0;
				}

				if (point.x == 0)
				{
					if (point.y > 0)
					{
						return 0;
					}
					else
					{
						return PI;
					}
				}

				if (point.y == 0)
				{
					if (point.x > 0)
					{
						return PI/2;
					}
					else
					{
						return (float) (1.5*PI);
					}
				}
			}
			break;
			case eQ_ONE:
			{
				return dAngle;
			}
			case eQ_TWO:
			{
				dAngle = PI - dAngle;
			}
			break;
			case eQ_THREE:
			{
				dAngle = PI - dAngle;
			}
			break;
			case eQ_FOUR:
			{
				dAngle += 2*PI;
			}
			break;
		}

		return dAngle;

	}
	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Activity activity) {
		try {
			PackageManager manager = activity.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					activity.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取屏幕宽度
	 *
	 * @param activity
	 * @return
	 */
	public static int getWidth(Context activity) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = activity.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	public static int getMetricsHeight(Context context) {
		Rect rect = new Rect();
		Window win = ((Activity) context).getWindow();
		win.getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels - (statusBarHeight * 2);
	}

	public static Bitmap createReflectedImage(Bitmap originalImage) {
		// The gap we want between the reflection and the original image
		final int reflectionGap = 4;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matrix applied to it.
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 3, width, height / 3, matrix, false);

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 3), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// Draw in the gap
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		// Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/***
	 * 获取网络
	 *
	 * @param context
	 * @return
	 */
	public static String checkNetworkInfo(Context context) {
		if (context!=null) {
			ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conMan!=null) {
				NetworkInfo info = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				State mobile=null;
				State wifi=null;
				 if(info!=null){
					  mobile = info.getState();
				 }
				NetworkInfo infos = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(infos!=null) {
					 wifi = infos.getState();
				}
				if (wifi == State.CONNECTED || wifi == State.CONNECTING)
					return "wifi";
				if (mobile == State.CONNECTED || mobile == State.CONNECTING)
					return "mobile";
				if (mobile == NetworkInfo.State.UNKNOWN
						|| wifi == NetworkInfo.State.UNKNOWN)
					return "unknown";
			}
		}
		return null;
	}

	public static boolean checkNetwork(Context context) {
		boolean flag = false;
		NetworkInfo networkInfo = null;
		if (context != null){
			ConnectivityManager cwjManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cwjManager != null) {
				networkInfo = cwjManager.getActiveNetworkInfo();
				if (networkInfo != null)
					flag = networkInfo.isAvailable();
			}
		}
		return flag;
	}

	public static void showToast(Context context, CharSequence msg) {
		try {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showToastCenter(Context context, CharSequence msg) {
		Toast toast = null;
		toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		;
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToast(Context context, CharSequence msg, int duration) {
		Toast t = Toast.makeText(context, msg, duration);
		t.setDuration(duration);
		t.show();
	}
	//假网
	public static boolean isAvailableByPing() {
		ShellUtils.CommandResult result = ShellUtils.execCmd("ping -c 1 -w 1 223.5.5.5", false);
		boolean ret = result.result == 0;
		return ret;
	}


	/***
	 * 获取设备ID号
	 *
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/***
	 * 读文件
	 *
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath) {
		String res = null;
		try {
			File file = new File(filePath);
			FileInputStream fin = new FileInputStream(file);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return (res);
	}

	/***
	 * 写文件
	 *
	 * @param filePath
	 * @param content
	 */
	public static void writeFile(String filePath, String content) {
		if (filePath == null) {
			return;
		}
		try {
			FileWriter fw = new FileWriter(filePath);
			fw.write((content));
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 发送广播
	 *
	 * @param context
	 * @param action
	 * @param op
	 */
	public static void sendBroadcast(Context context, String action, int op) {
		Intent intent = new Intent(action);
		Bundle bundle = new Bundle();
		bundle.putInt("op", op);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}

	/***
	 * 写值 int
	 *
	 * @param prefName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writePrefValue(String prefName, Context context,
									  String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences(prefName,
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value).commit();
	}

	/***
	 * 写值 String
	 *
	 * @param prefName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writePrefValue(String prefName, Context context,
									  String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(prefName,
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value).commit();
	}

	/***
	 * 写值 boolean
	 *
	 * @param prefName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writePrefValue(String prefName, Context context,
									  String key, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(prefName,
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value).commit();
	}

	/***
	 * 取值 boolean
	 *
	 * @param prefName
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getPrefValue(String prefName, Context context,
									   String key, boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(prefName, 0);
		return settings.getBoolean(key, defaultValue);
	}

	/***
	 * 取值 String
	 *
	 * @param prefName
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPrefValue(String prefName, Context context,
									  String key, String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(prefName, 0);
		return settings.getString(key, defaultValue);
	}

	/***
	 * 取值 int
	 *
	 * @param prefName
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getPrefValue(String prefName, Context context,
								   String key, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(prefName, 0);
		return settings.getInt(key, defaultValue);
	}

	public static long getPrefValue(String prefName, Context context,
									String key, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(prefName, 0);
		return settings.getLong(key, defaultValue);
	}

	public static void writePrefValue(String prefName, Context context,
									  String key, long value) {
		SharedPreferences preferences = context.getSharedPreferences(prefName,
				0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(key, value).commit();
	}

	/***
	 * 保存对象到文件
	 *
	 * @param context
	 * @param fileName
	 * @param obj
	 */
	public static void saveObjectToFile(Context context, String fileName,
										Object obj) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(obj);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 从文件中读取对象
	 *
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Object loadObjectFromFile(Context context, String fileName) {
		Object obj = null;
		boolean isExit = true;
		String[] files = context.fileList();
		for (int i = 0; i < files.length; i++) {
			if (files[i].equals(fileName)) {
				isExit = false;
			}
		}
		if (!isExit) {
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try {
				fis = context.openFileInput(fileName);
				in = new ObjectInputStream(fis);
				obj = in.readObject();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (OptionalDataException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return obj;
	}

	/**
	 * 获取版本号
	 *
	 * @return
	 */
	public static int getCurrentVersion(Context context) {
		int mVersionCode = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			mVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return mVersionCode;
	}

	// 判断int类型的值有几位数
	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
			99999999, 999999999, Integer.MAX_VALUE };

	public static int sizeOfInt(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	// 判断sdCard 是否存在
	public static boolean isExistSdcard() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))// 判断sd卡是否存在
		{
			return true;
		} else {
			return false;
		}
	}

	public static int sdk_code() {
		String s = android.os.Build.VERSION.SDK;
		return Integer.parseInt(s);

	}

	public static ImageGetter imgGetter = new Html.ImageGetter() {
		public Drawable getDrawable(final String source) {
			new Thread() {
				public void run() {
					try {
						drawable = Drawable.createFromStream(
								getRequest(source), "src");
						if (drawable.getIntrinsicWidth() > 100
								&& drawable.getIntrinsicHeight() > 100) {
							drawable.setBounds(0, 0, 100, 100);

						} else {
							drawable.setBounds(0, 0,
									drawable.getIntrinsicWidth(),
									drawable.getIntrinsicHeight());
						}
					} catch (Exception e) {
					} catch (Error e) {
					}
				}

			}.start();
			return drawable;
		}
	};

	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = w / ((float) width);
		float scaleHeight = h / ((float) height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(1000); // 5秒
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;

	}

	// 转移特殊字符
	public static String StrReplace(String str) {
		String sd = str, ds;
		str = str.replace("&lt;", "<");
		str = str.replace("&gt;", ">");
		str = str.replace("&quot;", "\"");
		str = str.replace("<br>", "\n");
		// str=str.replace("</span>", "</span>\r");
		if (str.indexOf("[url=http:") >= 0 && str.indexOf("[/url") >= 0) {
			ds = str.substring(0, str.indexOf("[url=http:"));
			sd = sd.replace(ds, "");
			String d = "";
			d = sd.substring(sd.indexOf("[url=http:"), sd.indexOf("]") + 1);
			str = str.replace(d, "");
			str = str.replace("[/url]", "");
			return str;
		}
		// str=str.replace("[url]", "");
		// str=str.replace("[/url]", "");
		return str;

	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// 获取sd卡剩余大小
	public static int getAvailableBlocks() {
		File filePath = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(filePath.getPath());
		return (int) (statFs.getBlockSize()
				* ((long) statFs.getAvailableBlocks()) / 1024 / 1024);
	}

	/*
	 * public static void FileMkdirs() { File fileDirrd = new
	 * File(Environment.getExternalStorageDirectory()+ "/5sing" + "/download" +
	 * "/cache"); if (!fileDirrd.exists()) fileDirrd.mkdirs(); }
	 */

	public static void FileMkdirs() {
		File fileDir = new File(Environment.getExternalStorageDirectory()
				+ "/5sing");
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File fileDirr = new File(Environment.getExternalStorageDirectory()
				+ "/5sing" + "/download");
		if (!fileDirr.exists()) {
			fileDirr.mkdirs();
		}
		File fileDirrs = new File(Environment.getExternalStorageDirectory()
				+ "/5sing" + "/download" + "/cache");
		if (!fileDirrs.exists()) {
			fileDirrs.mkdirs();
		}
	}

	private static ProgressDialog m_pDialog;

	// 等待提醒
	public static void m_pDialog_code(Context context, String mess) {
		try {
			m_pDialog = new ProgressDialog(context);
			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			m_pDialog.setIndeterminateDrawable(context.getResources()
					.getDrawable(R.drawable.progressbar));
			m_pDialog.setMessage(Html.fromHtml("<font color=\"#969696\">"
					+ mess + "</font>"));
			m_pDialog.setIndeterminate(false);
			m_pDialog.setCancelable(true);
			m_pDialog.show();
		} catch (Exception e) {
		}
	}



	// 等待提醒
	public static void m_pDialog_code(Context context, String mess,
									  final Handler mHandler, final int what) {
		try {
			m_pDialog = new ProgressDialog(context);
			m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			m_pDialog.setIndeterminateDrawable(context.getResources()
					.getDrawable(R.drawable.progressbar));
			m_pDialog.setMessage(Html.fromHtml("<font color=\"#969696\">"
					+ mess + "</font>"));
			m_pDialog.setIndeterminate(false);
			m_pDialog.setCancelable(true);
			m_pDialog.show();
			m_pDialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					Message msg = mHandler.obtainMessage();
					msg.what = what;
					msg.obj = m_pDialog;
					mHandler.sendMessage(msg);
				}
			});
		} catch (Exception e) {
		}
	}

	// 关闭提醒
	public static void m_pDialog_code() {
		try {
			if (m_pDialog != null) {
				m_pDialog.dismiss();
			}
		} catch (Exception e) {
		}

	}

	// 转换时间
	public static String Creatime(String createTime) {
		try {
			if (!Locale.getDefault().getLanguage().equals("zh")) {
				return createTime.substring(0, createTime.lastIndexOf(":"));
			}
			String createtime = DateTimeUtils.formatCreateTime(createTime);
			// 如果返回 时间的年月日 与系统当前时间的年月日相等，就不显示 年月日
			if (createtime.length() > 5) {
				new DateFormat();
				createtime = createtime.replace(
						DateFormat.format("yyyy",
								Calendar.getInstance(Locale.CHINA))
								+ "-", "");
				createtime = createtime.substring(0, createtime.length() - 3);
				new DateFormat();
				new DateFormat();
				String s = DateFormat.format("MM",
						Calendar.getInstance(Locale.CHINA))
						+ "-"
						+ DateFormat.format("dd",
						Calendar.getInstance(Locale.CHINA));
				if (s.equals(createtime.subSequence(0, 5))) {

					// 今天发表的
					createtime = createtime.replace(s, "").trim();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					long millionSeconds = sdf.parse(createTime).getTime();// 毫秒
					long t = System.currentTimeMillis() / 1000 / 60;
					long ss = millionSeconds / 1000 / 60;
					if (ss >= t) {
						return "刚刚";
					}
					if (t > ss) {
						long sd = t - ss;
						if (sd >= 60) {
							return (int) sd / 60 + "小时前";
						}
						return sd + "分钟前";

					}
				}

				new DateFormat();
				int MM = Integer.parseInt(DateFormat.format("MM",
						Calendar.getInstance(Locale.CHINA)).toString());
				new DateFormat();
				int DD = Integer.parseInt(DateFormat.format("dd",
						Calendar.getInstance(Locale.CHINA)).toString());
				if (MM == Integer.parseInt((createtime.subSequence(0, 2)
						.toString()))) {
					if (DD - 1 == Integer.parseInt((createtime
							.subSequence(3, 5).toString()))) {
						String sd = (createtime.subSequence(0, 5).toString());
						createtime = createtime.replace(sd, "昨天").trim();
					}
					if (DD - 2 == Integer.parseInt((createtime
							.subSequence(3, 5).toString()))) {
					}
				}
				return createtime;
			}
			return createtime;
		} catch (Exception e) {
			return createTime;
		}
	}

	// 更新媒体库
	public static void onCreateMediaScanner(Context activity, String path) {
		// 加try catch方式，避免Nexus等机型安装失败
		try {
			activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse("file://" + path)));
		} catch (Exception ee) {

		} catch (Error er) {

		}

		// MediaScannerConnection.scanFile(activity,
		// new String[] { path }, null,
		// new MediaScannerConnection.OnScanCompletedListener() {
		// public void onScanCompleted(String path, Uri uri) {
		// KGLog.d("ExternalStorage", "Scanned " + path + ":");
		// KGLog.d("ExternalStorage", "-> uri=" + uri);
		// }
		// });
	}
	private static Uri getContactContentUri() {
		if (isEclairOrLater()) {
			return Uri.parse("content://com.android.contacts/contacts");
		} else {
			return Contacts.People.CONTENT_URI;
		}
	}
	private static boolean isEclairOrLater() {
		return Build.VERSION.SDK_INT >= 5;
	}


	//设置默认铃声
	public static void setDefaultRing(Context activity,int contactID,Uri contactUri){
		ContentValues values = new ContentValues();
		String ringUri = null;
		values.put(ContactsContract.Contacts.CUSTOM_RINGTONE,ringUri);
		values.put(ContactsContract.Contacts._ID, contactID);
		try{
			activity.getContentResolver().update(contactUri, values, null, null);
		}catch (Exception e){

		}
	}

	//获得汉语拼音首字母
	private static String getAlpha(String str) {
		if (TextUtils.isEmpty(str)) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母  
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		}
		return "#";
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp  
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	//计算某目录下的缓存大小，单位MB
	public static long getCacheSize(File f){
		long size = 0;
		File files[] = f.listFiles();
		if(files!=null){
			int fileLength = files.length;
			for (int i = 0; i < fileLength; i++) {
				if (files[i].isDirectory()) {
					size = size + getCacheSize(files[i]);
				} else {
					size = size + files[i].length();
				}
			}

		}
		//BigDecimal bg1 = new BigDecimal(1024);
		// BigDecimal result =  (new BigDecimal(size).divide(bg1,6,RoundingMode.HALF_UP)).divide(bg1, 2, RoundingMode.HALF_UP);
		return size;
	}

	/**
	 * 文件大小单位转换 
	 */
	public static String setFileSize(long size) {
		DecimalFormat df = new DecimalFormat("###.##");
		float f = ((float) size / (float) (1024 * 1024));
		if (f < 1.0) {
			float f2 = ((float) size / (float) (1024));

			return "当前缓存 "+df.format(Float.valueOf(f2).doubleValue()) + "KB";
		} else {
			return "当前缓存 "+df.format(Float.valueOf(f).doubleValue()) + "MB";
		}

	}
	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取屏幕密度
	 *
	 * @param activity
	 * @return
	 */
	public static int getDpi(Context activity) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = activity.getResources().getDisplayMetrics();
		int densityDpi = dm.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		return densityDpi;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// 将Bitmap转换成Drawable
	public static Drawable BitToDra(Bitmap bit) {
		Drawable drawable;
		drawable = new BitmapDrawable(bit);
		int w = (int) (drawable.getIntrinsicWidth() * 1.5);
		int h = (int) (drawable.getIntrinsicHeight() * 1.5);
		drawable.setBounds(0, 0, w, h);
		return drawable;
	}

	public static String bitmaptoString(Bitmap bitmap) {


// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 40, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}




	/**
	 * 判断是否是2G或3G网络
	 * @param context
	 * @return
	 */
	public static boolean is2GOr3G(Context context) {
		if (context == null){
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetInfo != null
					&& (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
					(activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
							|| activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
							.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前网络是否是2G网络
	 *
	 * @param context
	 * @return boolean
	 */
	public static boolean is2G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
				|| activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
				.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是3G网络
	 *
	 * @param context
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	private static String getAPKURL(String url) {
		return url.substring(url.lastIndexOf("."), url.length());
	}

	private static boolean checkURL(String url) {
		boolean value = false;
		if (".apk".equals(getAPKURL(url))) {
			value = true;
		}
		return value;
	}


	/**
	 * 检查手机格式是否合格
	 *
	 * @return
	 */
	public static boolean isPhoneCorrect(String phoneNumber) {
		if (!TextUtils.isEmpty(phoneNumber)) {
			if (phoneNumber.length() > 11)
				phoneNumber = phoneNumber
						.substring(phoneNumber.length() - 11, phoneNumber.length());
			Pattern pattern = Pattern.compile("^[1][3-8]\\d{9}$");
			Matcher matcher = pattern.matcher(phoneNumber);
			return matcher.find();
		}
		return false;
	}
	/**
	 * 判断是否是合法邮箱
	 */
	public static boolean patternEmail(String str) {
		Pattern pattern = Pattern
				.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 判断是否是合法的手机号码
	 */
	public static boolean patternPhoneNumber(String number) {
		Pattern pattern = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$");
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();

	}

	public static String getChannelId(Context context){
		String result;
		try {
			ApplicationInfo metaDataAppInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			result = String.valueOf(metaDataAppInfo.metaData.getString("UMENG_CHANNEL"));

		} catch (PackageManager.NameNotFoundException e) {
			result = "unknown";
		}
		return result;
	}

	/**
	 * 判断是否是移动的手机号码
	 */
	public static boolean isCMMPhone(String number) {
		Pattern p = Pattern.compile("^((13[4-9])|(15[0-2,7-9])|(18[2-4,7-8])|(147)|(178))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}
	/**
	 * 判断是否是电信的手机号码
	 */
	public static boolean isCTMPhone(String number) {
		Pattern p = Pattern.compile("^((133)|(153)|(149)|(18[0-1,9])|(17[3,7]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}
	/**
	 * 判断是否是联通的手机号码
	 */
	public static boolean isUNCPhone(String number) {
		Pattern p = Pattern.compile("^((13[0-2])|(15[5-6])|(18[5-6])|(145)|(17[5-6]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}



	public static String getDynamicPhoto(String photo, int w, int h) {
		try {
			//动态列表，动态详情，查看大图，消息中心
			// 判断是否是5sing 的图片
			if (photo.indexOf("5sing") < 0) {
				return photo;
			}
			if (photo.indexOf("http") < 0) {
				return photo;
			}
			// 不支持转码
			if (photo.indexOf("static.5sing") >= 0 || photo.endsWith(".gif")) {
				return photo;
			}


			String paths = getPhoto(photo);
			String rs = "_" + w + "_" + h + paths.substring(paths.lastIndexOf("."));
			String r1 = paths.substring(0, paths.lastIndexOf("."));
			return r1 + rs;
		} catch (Exception e) {
		}
		return photo;
	}

	public static Uri getBigPhotoUri(String photo) {
		if (TextUtils.isEmpty(photo))
			return null;
		return Uri.parse(getBigPhoto(photo));
	}

	/**
	 * 获取大图片
	 *
	 * @param photo 等比缩放，宽高最大值为800
	 * @return
	 */
	public static String getBigPhoto(String photo) {
		try {

			// 判断是否是5sing 的图片
			if (photo.indexOf("5sing") < 0) {
				return photo;
			}
			if (photo.indexOf("http") < 0) {
				return photo;
			}
			// 不支持转码
			if (photo.indexOf("static.5sing") >= 0 || photo.endsWith(".gif")) {
				return photo;
			}
			String paths = getPhoto(photo);
			String rs = "_600_600" + paths.substring(paths.lastIndexOf("."));
			String r1 = paths.substring(0, paths.lastIndexOf("."));
			return r1 + rs;
		} catch (Exception e) {
		}
		return photo;
	}

	/**
	 * 过滤已经添加过宽高的地址
	 *
	 * @param p
	 * @return
	 */
	public static String getPhoto(String p) {
		try {
			Pattern pat = Pattern.compile("\\_[0-9]+\\_[0-9]+");
			Matcher mat = pat.matcher(p);
			return mat.replaceAll("");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return p;

	}

	/**
	 * 方法1：检查某表列是否存在
	 *
	 * @param db
	 * @param tableName 表名
	 * @param columnName 列名
	 * @return
	 */
	public static boolean checkColumnExist(SQLiteDatabase db, String tableName, String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			// 查询�?��
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}

	public static void fixInputMethodManagerLeak(Context destContext) {
		if (destContext == null) {
			return;
		}

		InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}

		String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
		Field f = null;
		Object obj_get = null;
		for (int i = 0;i < arr.length;i ++) {
			String param = arr[i];
			try{
				f = imm.getClass().getDeclaredField(param);
				if (f.isAccessible() == false) {
					f.setAccessible(true);
				} // author: sodino mail:sodino@qq.com
				obj_get = f.get(imm);
				if (obj_get != null && obj_get instanceof View) {
					View v_get = (View) obj_get;
					if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
						f.set(imm, null); // 置空，破坏掉path to gc节点
					} else {
						// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//						if (QLog.isColorLevel()) {
//							QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
//						}
						break;
					}
				}
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
}
