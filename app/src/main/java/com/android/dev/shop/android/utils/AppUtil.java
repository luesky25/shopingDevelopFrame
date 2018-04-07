package com.android.dev.shop.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;


import com.android.dev.shop.R;
import com.android.dev.utils.StringUtil;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述: App 通用工具
 * 
 */
public class AppUtil {

    public static String Version_Url = "";

    /**
     * 获取当前版本号
     * 
     * @param context
     * @return versionCode
     */
    public static int getCurVersion(Context context) {
        try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo("com.kugou.fm",
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = pinfo.versionCode;

            return versionCode;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取当前版本号
     * 
     * @param context
     * @return versionCode
     */
    public static String getCurVersionName(Context context) {
        try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo("com.kugou.fm",
                    PackageManager.GET_CONFIGURATIONS);
            String versionCode = pinfo.versionName;
            return "版本号  V " + versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "版本号  V1.0";
    }
    
    /**
     * 获取纯粹的版本号
     * @param context
     * @return
     */
    public static String getCurVer(Context context){
    	try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo("com.kugou.fm",
                    PackageManager.GET_CONFIGURATIONS);
            String versionCode = pinfo.versionName;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "3.0.0";
    }

    /**
     * 获取AndroidManifest.xml中的meta_date值
     * 
     * @param context
     * @param key
     * @return
     */
    public static String getMetaData(Context context, String key) {
        String metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            metaData = bundle.getString(key);
        } catch (Exception e) {
        }
        return metaData;
    }

    /**
     * 获取应用名称
     * 
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            appName = context.getString(ai.labelRes);
        } catch (Exception e) {
        }
        return appName;
    }

    /**
     * 判断是否有这个权限
     * @param permName
     * @return
     */
    public static boolean getAppPermission(Context context, String permName){
    	PackageManager pm = context.getPackageManager();  
        boolean permission = (PackageManager.PERMISSION_GRANTED ==   
                pm.checkPermission(permName, "com.kugou.fm"));  
        return permission;
    }
    
    

    public static int[] getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return new int[] {
                display.getWidth(), display.getHeight()
        };
    }



    /**
     * 应用是否被隐藏<在后台运行>
     * 
     * @param context
     * @return
     */
    public static boolean IsRunOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo!=null && tasksInfo.size()>0 && context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否锁屏
     * 
     * @return true 锁屏状态， false 非锁屏状态
     */
    public static boolean isScreenOff(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * 判断是否已经创建快捷方式
     * 
     * @param context
     * @return
     */
    public static boolean isAddShortCut(Context context) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = context.getContentResolver();
        // 本人的2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
        String AUTHORITY = "";
        if (android.os.Build.VERSION.SDK_INT < 8) {
            AUTHORITY = "com.android.launcher.settings";
        } else {
            AUTHORITY = "com.android.launcher2.settings";
        }
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[] {
                "title", "iconResource"
        }, "title=?", new String[] {
            context.getString(R.string.app_name)
        }, null);// XXX表示应用名称。
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
            c.close();
        }
        return isInstallShortcut;
    }

    /**
     * @param context
     * @param path
     * @return
     */
    public static InputStream getAssetsFile(Context context, String path) {
        AssetManager am = context.getAssets();
        InputStream is;
        try {
            is = am.open(path);
        } catch (IOException e) {
            is = null;
        }
        return is;
    }

    /**
     * 获取手机内部存储
     * 
     * @param context
     * @return
     */
    public static long getInternalAvailableBlocks(Context context) {
        // String mTotalSize = "内部总容量：";
        // String mAvailSize = "内部剩余容量：";
        StatFs statInternal = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statInternal.getBlockSize();
        long totalBlocks = statInternal.getBlockCount() * blockSize;
        long availableBlocks = statInternal.getAvailableBlocks() * blockSize;
        // mTotalSize += Formatter.formatFileSize(context, totalBlocks *
        // blockSize);
        // mAvailSize += Formatter.formatFileSize(context, availableBlocks *
        // blockSize);
        return availableBlocks;
    }

    /**
     * 资源包文件,一定要和/assets/下的资源包名称一样
     */
    public static final String SOURCE_FILE_NAME = "source.zip";

    public static String getPackagePath(Context context) {
        return context.getFilesDir().getParent();
    }

    public static String getSourcePath(Context context) {
        return context.getFilesDir().getPath() + "/" + SOURCE_FILE_NAME;
    }


    public static String getImagePath(Context context) {
        return context.getFilesDir().getParent() + "/image/";
    }

    public static String getFilesPath(Context context) {
        return context.getFilesDir().getParent() + "/files/";
    }


    /**
     * 获取中等图片缓存的key
     * 
     * @param radioKey
     * @return
     */
    public static String getPlayImageKey(long radioKey) {
        return new StringBuilder().append(radioKey).append("_medium").toString();
    }

    public static String getPlayImageUrl(String url) {
        return url.replace("90x90", "160x160");
    }
    /**
     * 获取状态栏高度
     * 
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }



    /**
     * 安装APK
     * 
     * @param context
     * @param filePath APK存放路径
     */
    public static void installApk(Context context, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 读取RAW文件内容
     * 
     * @param context
     * @param resid
     * @param encoding
     * @return
     */
    public static String getRawFileContent(Context context, int resid, String encoding) {
        InputStream is = context.getResources().openRawResource(resid);
        if (is != null) {
            ByteArrayBuffer bab = new ByteArrayBuffer(1024);
            int read;
            try {
                while ((read = is.read()) != -1) {
                    bab.append(read);
                }
                return EncodingUtils.getString(bab.toByteArray(), encoding);
            } catch (UnsupportedEncodingException e) {
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }



    /**
     * @return
     */
    public static long getRAMTotalSize() {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * @return
     */
    public static long getROMTotalSize() {
        StatFs statInternal = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statInternal.getBlockSize();
        return statInternal.getBlockCount() * blockSize;
    }
    /**
     * 获取字符串的长度，中文占2个字符,英文数字占1个字符
     * 
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static double length(String value) {
    	if(TextUtils.isEmpty(value)){
    		return 0;
    	}
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            // 获取一个字符
            String temp = value.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为2
                valueLength += 2;
            } else {
                // 其他字符长度为1
                valueLength += 1;
            }
        }
        // 进位取整
        return Math.ceil(valueLength);
    }
    
    /**
     * 判断Intent是否有效
     * 
     * @param context
     * @return true 有效
     */
    public static boolean isIntentAvailable(Context context, final Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    
    /**
     * 判断一个程序是否显示在前端,根据测试此方法执行效率在11毫秒,无需担心此方法的执行效率
     * 
     * @return true--->在前端,false--->不在前端
     */
    public static boolean isApplicationShowing(Context context) {
	     boolean result = false;
	     String packageName = context.getPackageName();
	     ActivityManager am = (ActivityManager) context
	       .getSystemService(Context.ACTIVITY_SERVICE);
	     List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
	     if (appProcesses != null) {
	      for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
	       if (runningAppProcessInfo.processName.equals(packageName)) {
	        int status = runningAppProcessInfo.importance;
	        if (status == RunningAppProcessInfo.IMPORTANCE_VISIBLE
	          || status == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
	         result = true;
	        }
	       }
	      }
	     }
	     return result;
    }
    /**
     * 检测是否是三星设备并且是5.0系统
     *
     * @return
     */
    public static boolean isBrokenSamsungDevice() {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung") && isBetweenAndroidVersions(
                Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.LOLLIPOP));
    }

    private static boolean isBetweenAndroidVersions(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }
    public static String getNum(int num){
        String nums;
        if (num>= 10000) {
            nums= (num / 10000) + "万";

        } else {
            int result_num = num / 10000;
            if (result_num > 0) {
                nums = result_num + "万";
            }else {
                nums =num+"";
            }
        }
        return nums;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     *
     * @param context
     * @return true 为是升级用户
     */
    public static boolean isUpadateApp(Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.firstInstallTime != pi.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
       return false;
    }
}
