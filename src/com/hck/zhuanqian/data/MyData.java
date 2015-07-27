package com.hck.zhuanqian.data;

import com.hck.zhuanqian.bean.Config;
import com.hck.zhuanqian.bean.UserBean;

public class MyData {
	public static MyData myData;
	public static final String key = "HCK123hck";
    private UserBean userBean;
    
	public static MyData getMyData() {
		return myData;
	}

	public static void setMyData(MyData myData) {
		MyData.myData = myData;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public static MyData getData() {
		if (myData == null) {
			myData = new MyData();
		}
		return myData;
	}

	private Config config;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
