package com.hck.zhuanqian.net;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Urls;

public class Request {
	private static final int TIME_OUT = 15 * 1000;
	private static HCKHttpClient client = new HCKHttpClient();
	static {
		client.setTimeout(TIME_OUT);
	}

	private static void post(String method, HCKHttpResponseHandler handler) {
		RequestParams params =new RequestParams();
		post(method,params,handler);
	}

	public static void post(String method, RequestParams params,
			HCKHttpResponseHandler handler) {
		params.put("password", MyData.key);
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
		post(Urls.ADD_USER, params, handler);
	}

	/**
	 * 获取赚钱记录
	 * 
	 * @param handler
	 * @param params
	 * @param page
	 */
	public static void getZhuanQianJiLu(HCKHttpResponseHandler handler,
			RequestParams params) {
		post(Urls.GET_ZHUANQIAN_JILU, params, handler);

	}

	/**
	 * 获取推广信息.
	 */
	public static void getTG(HCKHttpResponseHandler handler,
			RequestParams params) {
		post(Urls.GET_TuiGuang_Data, params, handler);

	}

	public static void getDuiHuanJiLu(HCKHttpResponseHandler handler) {
		post(Urls.GET_DUIHUAN_JILU, handler);

	}
}
