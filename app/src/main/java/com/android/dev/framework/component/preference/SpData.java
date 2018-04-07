package com.android.dev.framework.component.preference;

import android.content.Context;

import com.android.dev.basics.DevApplication;

public class SpData extends PreferenceOpenHelper {

	private static SpData mSpData;

	public SpData(Context context, String prefname) {
		super(context, prefname);
	}

	public synchronized static SpData getInstance() {
		if (mSpData == null) {
			Context context = DevApplication.getInstance().getApplicationContext();
			String name = context.getPackageName() + "migunew";
			mSpData = new SpData(context, name);
		}
		return mSpData;
	}
}
