package com.android.dev.utils;

import com.android.common.utils.KGLog;

/**
 * 异常抛出工具类，debug模式会崩溃，及早发现问题
 * 上线版本仅仅log打印错误
 * @author haitaoliu
 */
public class ExceptionUtil {
	
	public static void throwsException(String msg) {
		if (KGLog.isDebug()) {
			throw new IllegalStateException(msg);
		}
		else {
			KGLog.e("error", msg);
		}
	}
	
}
