package com.android.dev.shop.android.base.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import com.android.dev.shop.R;


/**
 * 通用的dialog
 */
public class CommonDialog extends Dialog {
	
	private Object data;
	private View view;
	
	public void setTag(Object data) {
		this.data = data;
	}
	
	public Object getTag() {
		return data;
	}
	
	public void setView(View v) {
		view = v;
		setContentView(view);
	}
	
	public View getView() {
		return view;
	}
	
    public CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommonDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 
     * 
     * @param context 上下文
     * @param layout xml布局
     * @return
     */
    public static CommonDialog makeNewDialog(Context context, View layout, int[] gravity, Integer animations) {
    	CommonDialog dialog = new CommonDialog(context, R.style.dialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(layout);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if (gravity == null)
        	dialogWindow.setGravity(Gravity.CENTER);
        else {
        	switch (gravity.length) {
        	case 1:
               
        		dialogWindow.setGravity(gravity[0]);
        		break;
        	case 2:
        		dialogWindow.setGravity(gravity[0] | gravity[1]);
        		break;
        	case 3:
        		dialogWindow.setGravity(gravity[0] | gravity[1] | gravity[2]);
        		break;
        	case 4:
        		dialogWindow.setGravity(gravity[0] | gravity[1] | gravity[2] | gravity[3]);
        		break;
        	default:
        		dialogWindow.setGravity(Gravity.CENTER);
        		break;
        	}
        }
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        if (animations != null)
        	dialogWindow.setWindowAnimations(animations);
        dialogWindow.setAttributes(lp);
        return dialog;
    }
    /**
     *
     *
     * @param context 上下文
     * @param layout xml布局
     * @return
     */
    public static CommonDialog makeNewDialog(Context context, View layout) {
        CommonDialog dialog = new CommonDialog(context, R.style.menudialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//        dialogWindow.setWindowAnimations(R.style.mystyle);
        dialogWindow.setAttributes(lp);
        return dialog;
    }
}
