package com.android.dev.shop.android.eventbus;


import org.greenrobot.eventbus.EventBus;


public class KGEventBus {
	
	public final static void register(Object obj) {
		if (!EventBus.getDefault().isRegistered(obj)) {
			EventBus.getDefault().register(obj);
		}
	}
	
	public final static void unregister(Object obj) {
		EventBus.getDefault().unregister(obj);
	}
	
	public final static void post(EventMessage msg) {
		EventBus.getDefault().post(msg);
	}
	
	public final static void post(int what) {
		EventMessage msg = new EventMessage(what);
		EventBus.getDefault().post(msg);
	}
	
}
