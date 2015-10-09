package com.hck.zhuanqian.downapp;

import javax.crypto.spec.PSource;

import org.json.JSONObject;

import android.content.Context;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HttpUtil;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.bean.AppInfoBean;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyTools;

public class UpdateUtil {
    private UpdateAppCallBack callBack;

    public interface UpdateAppCallBack {
        void backAppInfo(AppInfoBean bean);
    }

    public void isUpdate(Context context) {
        callBack = (UpdateAppCallBack) context;
        getInfo();
    }

    private void getInfo() {
        RequestParams params = new RequestParams();
        params.put("type", 1 + "");
        Request.getAppInfo(params, new JsonHttpResponseHandler() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D("更新: onSuccess:" + response.toString());

                try {
                    boolean isok = response.getBoolean("isok");
                    if (isok) {
                        AppInfoBean bAppInfoBean = JsonUtils.parse(response.getString("info"), AppInfoBean.class);
                        callBack.backAppInfo(bAppInfoBean);
                    }
                } catch (Exception e) {
                    LogUtil.D("更新222: onSuccess:" + e.toString());
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("更新: onFailure:" + content);
            }
        });
    }

}
