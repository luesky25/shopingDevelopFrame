
package com.android.dev.framework.component.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.view.View;

/**
 * 描述:所有对话框基类
 *
 * @author chenys
 * @since 2013-7-29 上午10:55:20
 */
public abstract class BaseDialog extends Dialog {

    public BaseDialog(Activity activity) {
        super(activity);
        setOwnerActivity(activity);
    }

    /**
     * 设置位置
     *
     * @param rect
     * @param contentView
     */
    public void onMeasureAndLayout(Rect rect, View contentView) {
    }

}
