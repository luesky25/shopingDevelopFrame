package com.android.dev.shop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.dev.shop.R;


public class TipsDialog extends Dialog implements
        View.OnClickListener {
    private OnRightClick onRightClick;
    private OnLeftClick onLeftClick;
    private TextView tv_logoutdialog_title;
    private Button bt_left;
    private Button bt_right;

    private Context context;
    private TextView tv_title_msg;

    public TipsDialog(Context context) {
        super(context, R.style.dialogStyle);
        this.context = context;
        init();
    }
    public Context getMyContext() {
        return context;
    }

    /**
     * 设置右边点击的回调
     *
     * @param onRightClick
     * @return
     */
    public TipsDialog setOnRightClick(OnRightClick onRightClick) {
        this.onRightClick = onRightClick;
        return this;
    }

    /**
     * 设置左边点击的回调
     *
     * @param onLeftClick
     * @return
     */
    public TipsDialog setOnLeftClick(OnLeftClick onLeftClick) {
        this.onLeftClick = onLeftClick;
        return this;
    }

    /**
     * 5.9.0版本 废弃
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public TipsDialog setTipsPadding(int left, int top, int right, int bottom) {
        //tv_logoutdialog_title.setPadding(left, top, right, bottom);
        return this;
    }

    public TipsDialog setTipsLinesSpaces(float add, float mult) {
        tv_logoutdialog_title.setLineSpacing(add, mult);
        return this;
    }

    public TipsDialog setTipsGravity(int gravity) {
        tv_logoutdialog_title.setGravity(gravity);
        return this;
    }


    /**
     * 设置提示信息
     *
     * @param tips
     * @return
     */
    public TipsDialog setTips(String tips) {
        tv_logoutdialog_title.setText(tips);
        return this;
    }

    public TipsDialog setTips(String tips, boolean isHtml) {
        if (isHtml) {
            tv_logoutdialog_title.setText(Html.fromHtml(tips));
        } else {
            tv_logoutdialog_title.setText(tips);
        }
        return this;
    }

    public TipsDialog setTips(CharSequence tips) {
        tv_logoutdialog_title.setText(tips);
        return this;
    }

    public TipsDialog setTips(SpannableStringBuilder tips) {
        tv_logoutdialog_title.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        tv_logoutdialog_title.setText(tips);
        return this;
    }

    /**
     * 设置左边按钮的文案
     *
     * @param text
     * @return
     */
    public TipsDialog setLeftText(String text) {
        bt_left.setText(text);
        return this;
    }

    /**
     * 设置右边按钮的文案
     *
     * @param text
     * @return
     */
    public TipsDialog setRightText(String text) {
        bt_right.setText(text);
        return this;
    }





    /**
     * 设置为单键
     *
     * @return
     */
    public TipsDialog setSingleButton(boolean singleButton) {
        bt_left.setVisibility(View.GONE);
        findViewById(R.id.lineView).setVisibility(View.GONE);
        //LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ToolUtils.dip2px(getContext(), 220), LinearLayout.LayoutParams.WRAP_CONTENT);

        //params.rightMargin=ToolUtils.dip2px(getContext(), 18);
        //params.leftMargin=ToolUtils.dip2px(getContext(), 19);

        //bt_right.setLayoutParams(params);

        return this;
    }

    /**
     * 设置提示文案
     *
     * @param tips
     * @return
     */
    public TipsDialog setTitleTips(String tips) {
        tv_title_msg.setText(tips);
        return this;
    }

    public TipsDialog setTitleTipsTextColor(int color) {
        tv_title_msg.setTextColor(color);
        return this;
    }

    public TipsDialog setTitleTipsTextSize(int size) {
        tv_title_msg.setTextSize(size);
        return this;
    }

    public void setLeftBtnColor(int color){
        bt_left.setTextColor(color);
    }

    /**
     * 设置单个按钮的文案
     *
     * @param text
     * @return
     */
    public TipsDialog setSingleButtonText(String text) {
        bt_right.setText(text);
        return this;
    }

    public TipsDialog setLeftButtonGone() {
        bt_left.setVisibility(View.GONE);
        return this;
    }


    private void init() {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_logout);

        bt_left = (Button) findViewById(R.id.bt_logout_dailog_cancle);
        bt_right = (Button) findViewById(R.id.bt_logout_dailog_exit);

        tv_title_msg = (TextView) findViewById(R.id.tv_title_msg);

        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        tv_logoutdialog_title = (TextView) findViewById(R.id.tv_logoutdialog_title);
        setCanceledOnTouchOutside(false);
        tv_logoutdialog_title.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                tv_logoutdialog_title.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//				int maxtheight = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight()*2/3;
                /*if(tv_logoutdialog_title.getHeight()>maxtheight){
                    ScrollView scrollview = (ScrollView)findViewById(R.id.scrollview);
					
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,maxtheight);
					scrollview.setLayoutParams(params);
				}*/
            }
        });


    }

    public TipsDialog hideTitle() {
        tv_title_msg.setVisibility(View.GONE);
        return this;
    }

    public TipsDialog setGravity(int gravity) {
        if (tv_logoutdialog_title != null) {
            tv_logoutdialog_title.setGravity(gravity);
        }
        return this;
    }


    // 点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_logout_dailog_cancle:
                if (onLeftClick != null) {
                    onLeftClick.leftClick();
                }
                break;
            case R.id.bt_logout_dailog_exit:
                if (onRightClick != null) {
                    onRightClick.rightClick();
                }
                break;
        }
        dismiss();
    }

    /**
     * 右边按钮的回调
     *
     * @author pc
     */
    public interface OnRightClick {
        void rightClick();
    }

    /**
     * 左边按钮的回调
     *
     * @author pc
     */
    public interface OnLeftClick {
        void leftClick();
    }
}
