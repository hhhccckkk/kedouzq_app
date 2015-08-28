package com.hck.zhuanqian.data;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import android.text.TextUtils;

import com.hck.zhuanqian.bean.Config;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.MyPreferences;

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
        if (userBean == null) {
            String userString = MyPreferences.getString("user", null);
            try {
                if (!TextUtils.isEmpty(userString)) {
                    userBean = JsonUtils.parse(userString, UserBean.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
