package com.android.dev.shop.android.base.temple;


import org.json.JSONObject;

public interface JsonCallback {
	public void onResponseJson(JSONObject response, int reqCode);

	public void onErrorResponse(VolleyError error, int reqCode);
}
