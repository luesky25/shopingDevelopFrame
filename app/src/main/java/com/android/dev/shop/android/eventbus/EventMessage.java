package com.android.dev.shop.android.eventbus;

public class EventMessage {
	
	public int what;
	public Object obj1;
	public Object obj2;
	public int arg1;
	public int arg2;
	
	public EventMessage(int what) {
		this.what = what;
	}
	
}
