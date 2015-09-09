package com.hck.zhuanqian.ui;

import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyPreferences;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	public static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.isPrintLog=true;
		context=this;
		new MyPreferences(this);
	}

}
