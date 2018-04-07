package com.android.common.permissiions;

import android.app.Activity;
import android.app.Dialog;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.android.common.R;


/**
 * Created by anckyxia on 2016/10/24.
 *  主要用于安卓6.0以上机型，需要动态权限而用户没有授权的地方，会弹窗提醒用户
 */

public class PermissionTipsDialog extends Dialog {

    protected TextView mTitleBar = null;

    protected TextView mContentText = null;

    private TextView cancelBtn;

    private TextView sureBtn;

    public PermissionTipsDialog(Activity activity) {
        super(activity);
        setContentView(R.layout.permission_tips_dialog_layout);
        mTitleBar = (TextView) findViewById(R.id.title);
        mContentText = (TextView) findViewById(R.id.content);
        cancelBtn = (TextView) findViewById(R.id.cancelTxt);
        sureBtn = (TextView) findViewById(R.id.sureTxt);

        setCanceledOnTouchOutside(true);


        cancelBtn.setOnClickListener(mCancelClickListener);
    }

    public void setPositiveButtonClickListener(View.OnClickListener mPositiveButtonClickListener){
        sureBtn.setOnClickListener(mPositiveButtonClickListener);
    }

    public void setNegativeButtonClickListener(View.OnClickListener mNegativeButtonClickListener){
        cancelBtn.setOnClickListener(mNegativeButtonClickListener);
    }

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    /**
     * 获取edittext里的内容
     * @return
     */
    public String getEditTextString(){
        return mContentText.getText().toString();
    }

    public void setContentString(String s){
        mContentText.setText(s);
    }

    public void setContentSpanString(Spanned s){
        mContentText.setText(s);
    }

    public void setOKButtonText(String s){
        sureBtn.setText(s);
    }

    @Override
    public void setTitle(CharSequence text){
        mTitleBar.setText(text);
    }


}
