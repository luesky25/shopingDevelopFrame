package com.android.dev.shop.android.base.temple;

import java.io.Serializable;

/**
 * 逻辑层回调到UI层的载体,字段可随意添加,随意使用
 */
public class UIGeter implements Serializable{

	private static final long serialVersionUID = 2L;

	private boolean isSuccess;
	
	private int returnCode;
	
	private String message;
	
	private Object returnObject;
	
	private int arg1;
	
	private int arg2;

	private String str1;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	
	public int getArg1() {
		return arg1;
	}

	public void setArg1(int arg1) {
		this.arg1 = arg1;
	}

	public int getArg2() {
		return arg2;
	}

	public void setArg2(int arg2) {
		this.arg2 = arg2;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	@Override
	public String toString() {
		return "UIGeter [isSuccess=" + isSuccess + ", returnCode=" + returnCode
				+ ", message=" + message + ", returnObject=" + returnObject
				+ ", arg1=" + arg1 + ", arg2=" + arg2 + "]";
	}

}
