package com.hck.zhuanqian.util;

import android.graphics.Point;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Urls;

public class RequestUtils {
	private static final int TIME_OUT = 15 * 1000;
	private static HCKHttpClient client = new HCKHttpClient();
	static {
		client.setTimeout(TIME_OUT);
	}

	private static void post(String method, HCKHttpResponseHandler handler) {
		client.get(Urls.MAIN_HOST_URL + method, handler);
	}

	public static void post(String method,
			RequestParams params, HCKHttpResponseHandler handler) {
		client.get(Urls.MAIN_HOST_URL + method, params, handler);
	}

	/**
	 * 获取配置信息
	 * 
	 * @param handler
	 */
	public static void getConfig(HCKHttpResponseHandler handler) {
		post(Urls.GET_CONFIG, handler);
	}

	/**
	 * 增加用户
	 * 
	 * @param handler
	 * @param params
	 */
	public static void addUser(HCKHttpResponseHandler handler,
			RequestParams params) {
		params.put("password", MyData.key);
		post(Urls.ADD_USER,params,handler);
	}
}
