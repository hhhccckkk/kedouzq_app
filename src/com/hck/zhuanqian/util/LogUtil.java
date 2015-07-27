package com.hck.zhuanqian.util;

import android.util.Log;

public class LogUtil {
	private static final String TAG = "hck";
	public static boolean isPrintLog;

	public static void D(String value) {
		if (isPrintLog) {
			Log.d(TAG, value);
		}
		
	}

}
