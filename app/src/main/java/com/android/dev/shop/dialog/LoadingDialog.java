package com.android.dev.shop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.dev.shop.R;
import com.android.dev.utils.ToolUtils;


public class LoadingDialog extends Dialog {
	private ProgressBar mProgressBar;

	public LoadingDialog(Context context) {
		super(context, R.style.dialogForLoadingStyle);
		init();
	}

	private void init() {
		View view = View.inflate(getContext(), R.layout.loading_layout,null);
		setContentView(view, new ViewGroup.LayoutParams(ToolUtils.getWidth(getContext()),ToolUtils.getHeight(getContext())));
		mProgressBar = (ProgressBar) findViewById(R.id.footer_progressBar);
		setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}
	public void show(){
	    super.show();
	    //((TextView)findViewById(R.id.footer_Message)).setText(res);
	}

	public void show(String res){
		super.show();
		((TextView)findViewById(R.id.footer_Message)).setText(res);
	}
}
