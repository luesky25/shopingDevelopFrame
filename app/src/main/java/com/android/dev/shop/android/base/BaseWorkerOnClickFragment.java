package com.android.dev.shop.android.base;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.dev.framework.component.base.BaseWorkerFragment;


/**
 * 提供onClickListener事件优化的fragment类
 * @author haitaoliu
 */
public abstract class BaseWorkerOnClickFragment extends BaseWorkerFragment implements OnClickListener {

	private SparseArray<Long> mClickMap;
	private int mIgnoreTime = 1000;		//点击事件响应忽略时间段
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mClickMap = new SparseArray<Long>();
	}
	
	@Override
	public final void onClick(View v) {
		long clickTime = System.currentTimeMillis();
		if (mClickMap.get(v.getId()) != null) {
			if (Math.abs(clickTime - mClickMap.get(v.getId())) < mIgnoreTime)
				return;
			OnClickSingle(v);
			mClickMap.put(v.getId(), clickTime);
		}
		else {
			OnClicked(v);
		}
	}
	
	public void setOnClickListenerSingle(View v) {
		v.setOnClickListener(this);
		mClickMap.put(v.getId(), (long) 0);
	}
	
	public void setOnClickListener(View v) {
		v.setOnClickListener(this);
	}
	
	//点击事件不会在短时间内相同ID多次响应
	public void OnClickSingle(View v) {}
	//点击事件方法会实时响应
	public void OnClicked(View v) {}

	public int getmIgnoreTime() {
		return mIgnoreTime;
	}

	public void setmIgnoreTime(int mIgnoreTime) {
		this.mIgnoreTime = mIgnoreTime;
	}

}
