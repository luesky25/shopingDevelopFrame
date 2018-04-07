package com.android.dev.shop.android.eventbus;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public interface EventBusCallback {

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(EventMessage msg);
	
}
