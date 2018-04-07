package com.android.dev.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.common.http.utils.PreferencesUtils;
import com.android.common.utils.KGLog;

import java.lang.reflect.Field;

/**
 * ScreenUtils 
 * @author levinlee
 */
public class ScreenUtils {

	private static final String KEYBOARD_HEIGHT = "keyboard_height";
    private static int mScreenWidth;
	private static int mScreenHeight;
	private static int mKeyBoardMinHeight = -1;

	private ScreenUtils() {
        throw new AssertionError();
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int)(dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpCeilInt(Context context, float px) {
        return (int)(pxToDp(context, px) + 0.5f);
    }
    
    
    
    /***********************  软键盘控制逻辑  *******************************************/
    public static void hideKeyboard(Activity activity, MotionEvent ev) {
    	if (ev.getAction() == MotionEvent.ACTION_DOWN) {
    		// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
    		 View v = activity.getCurrentFocus();
    		 if (isShouldHideInput(v, ev)) {
    			 if (v.getWindowToken() != null) {
    		    		InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    		    		im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		    	}
    		 }
    	}
    }
    
    
    private static boolean isShouldHideInput(View v, MotionEvent event) {
    	 if (v != null && (v instanceof EditText)) {
    		 int[] l = {0, 0};
    		 v.getLocationInWindow(l);
    		 int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
    		 if (event.getX() > left && event.getX() < right
    				 && event.getY() > top && event.getY() < bottom) {
    			 // 点击EditText的事件，忽略它。
    			 return false;
    		 } else {
    			 return true;
    		 }
    	 }
    	 // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
    	 return false; 
    }
    
    /**
     * 获取屏幕宽度(像素)
     * 
     * @param activity
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context activity) {
        if (mScreenWidth == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            dm = activity.getResources().getDisplayMetrics();
            mScreenWidth = dm.widthPixels;
        }
        return mScreenWidth;
    }
    
    /**
     * 获取屏幕高度(像素)
     * 
     * @param activity
     * @return 屏幕宽度
     */
    public static int getScreenHeight(Context activity) {
        if (mScreenHeight == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            dm = activity.getResources().getDisplayMetrics();
            mScreenHeight = dm.heightPixels;
        }
        return mScreenHeight;
    }
    
    /**
     * 获取StatusBar的高度
     * @param paramActivity
     * @return
     */
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }
    
    /**
     * 获取键盘的高度(从preference中，如果没有值会提供一个默认值)
     * @param context
     * @return
     */
//    public static int getKeyboardHeight(Context context){
//    	int defautHeight = getKeyBoardMinHeight(context);
//    	return PreferencesUtils.getInt(context, KEYBOARD_HEIGHT, defautHeight);
//    }
    
//    /**
//     * 获取keyboard最小的height
//     * @param context
//     * @return
//     */
//    public static int getKeyBoardMinHeight(Context context){
//    	if(mKeyBoardMinHeight == -1){
//	    	Resources r = context.getResources();
//	    	int emojiHeight = r.getDimensionPixelSize(R.dimen.normal_emoji_iv_height)*3;
//	    	int emojiVppHeight = r.getDimensionPixelSize(R.dimen.emojiicon_vpp_height)*2;
//	    	int emojiControlHeight = r.getDimensionPixelSize(R.dimen.emoji_input_control_height);
//	    	mKeyBoardMinHeight = emojiHeight + emojiVppHeight + emojiControlHeight;
//    	}
//    	return mKeyBoardMinHeight;
//    }
//    
    /**
     * 保存键盘的高度到preference中
     * @param context
     * @return
     */
    public static boolean storeKeyboardHeight(Context context, int keyboardHeight){
    	return PreferencesUtils.putInt(context, KEYBOARD_HEIGHT, keyboardHeight);
    }
    
    /**
     * 获得app的高度
     * @param paramActivity
     * @return
     */
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }
    
    /**
     * 现在是否显示了键盘
     * @param paramActivity
     * @return
     */
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        if(isMeizuPhone()){
        	height -= getNavigationBarHeight(paramActivity);
        }
        return height != 0;
    }
    
    /**
     * 是否是魅族手机
     * @return
     */
    public static boolean isMeizuPhone(){
    	return Build.MANUFACTURER.equalsIgnoreCase("Meizu");    	
    }
    
    /**
     * 获取navigation bar的高度
     * 对于meizu手机如果当时系统没有显示navigationbar 则返回0，有显示的时候才返回具体的高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if (isMeizuPhone()) {
        	// 如果是魅族手机，获取魅族手机的settings里面的mz_smartbar_auto_hide设置的值
        	final boolean autoHideSmartBar = Settings.System.getInt(context.getContentResolver(),
        			"mz_smartbar_auto_hide", 0) == 1;
        	// autoHideSmartBar为0表示目前没有navigationBar，如果为1则表示目前有navigationBar
            if (autoHideSmartBar) {
                return 0;
            } else {
                try {
                	// flymeos 5以下的系统有些存在系统dimen资源用于获取smartbar高度，有些则不再存在，所以西药先用这个取法去找smartbar的高度
                    Class c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("mz_action_button_min_height");
                    int height = Integer.parseInt(field.get(obj).toString());
                    return context.getResources().getDimensionPixelSize(height);
                } catch (Throwable e) { 
                	// 不自动隐藏smartbar同时又没有smartbar高度字段供访问，取系统navigationbar的高度
                    return getNormalNavigationBarHeight(context);
                }
            }
        } else {
            return getNormalNavigationBarHeight(context);
        }
    }

    /**
     * 普通手机获取navigationbar 高度的代码
     * @param ctx
     * @return
     */
    private static int getNormalNavigationBarHeight(final Context ctx) {
        try {
            final Resources res = ctx.getResources();
            int rid = res.getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid > 0) {
                boolean flag = res.getBoolean(rid);
                // 如果config_showNavigationBar是打开的，则获取系统的navigation_bar_height
                if (flag) {
                    int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        return res.getDimensionPixelSize(resourceId);
                    }
                }
            }
        } catch (Throwable e) {
            KGLog.d("ScreenUtils", "getNormalNavigationBarHeight() exception:" + e.getMessage());
        }
        return 0;
    }

}
