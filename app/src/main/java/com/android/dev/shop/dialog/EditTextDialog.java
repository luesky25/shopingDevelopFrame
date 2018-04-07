package com.android.dev.shop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.common.utils.EdiTextLengthFilter;
import com.android.dev.shop.R;


public class EditTextDialog extends Dialog {

    protected TextView mTitleBar = null;

    protected EditText mContentText = null;
    
    private TextView cancelBtn;
    
    private TextView sureBtn;
    

    public EditTextDialog(Activity activity) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.edittext_dialog);
        mTitleBar = (TextView) findViewById(R.id.title);
        mContentText = (EditText) findViewById(R.id.edit_text);
        cancelBtn = (TextView) findViewById(R.id.cancelTxt);
        sureBtn = (TextView) findViewById(R.id.sureTxt);

        mContentText.setSaveEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mContentText.setSaveFromParentEnabled(true);
        }
        
        setCanceledOnTouchOutside(true);
        mContentText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				if(arg0.toString().equals(" ")||(arg0.length()==1 && arg0.charAt(0)==((char)10))){
					mContentText.setText("");
				}
			}
		});
        
        cancelBtn.setOnClickListener(mCancelClickListener);
        
    }
    
    public void setPositiveButtonClickListener(View.OnClickListener mPositiveButtonClickListener){
        sureBtn.setOnClickListener(mPositiveButtonClickListener);
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
    
    public void setEditTextString(String s){
    	mContentText.setText(s);
    	if(s !=null && s.length()>0){
    		mContentText.setSelection(s.length());
    	}
    }
     public void setEditFilters(int size){
         mContentText.setFilters(new EdiTextLengthFilter[]{new EdiTextLengthFilter(size)});

     }


    public void setHintText(String hintText){
        mContentText.setHint(hintText);
    }
    
    @Override
	public void setTitle(CharSequence text){
    	mTitleBar.setText(text);
	}
}
