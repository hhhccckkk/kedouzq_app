package com.hck.zhuanqian.util;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.hck.zhuanqian.bean.ShareBean;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Urls;
import com.hck.zhuanqian.view.MyToast;

public class ShareUtil {
    private static String downAppUrl;

    public static void share(final Context context, final String content, final Handler handler) {
        ShareSDK.initSDK(context);
        new Thread() {
            public void run() {
                OnekeyShare oks = new OnekeyShare();
               // oks.disableSSOWhenAuthorize();
                oks.setCallback(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        LogUtil.D("分享: " + arg0.toString() + arg1 + arg2);
                        handler.sendEmptyMessage(0);

                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        LogUtil.D("分享: onComplete");
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        handler.sendEmptyMessage(2);

                    }
                });
                ShareBean shareBean = MyData.getData().getShareBean();
                if (shareBean == null) {
                    MyToast.showCustomerToast("网络异常，分享失败");
                    return;
                }
                oks.setDialogMode();
                if (TextUtils.isEmpty(content)) {
                    oks.setText(shareBean.getContent());
                } else {
                    oks.setText(content);
                }
                oks.setTitle("3分钟赚一元，一元起提现");
                oks.setUrl(getDownAppUrl());
                oks.setTitleUrl(getDownAppUrl());
                oks.setImageUrl(shareBean.getImageUrl());
                // 启动分享GUI
                oks.show(context);

            };
      }.start();

    }

    private static String getDownAppUrl() {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean != null) {
            if (downAppUrl == null) {
                long id= Contans.DEFAULT_ID+userBean.getId();
                downAppUrl = Urls.MAIN_HOST_URL + "downApp?uid=" + id;
            }
        }
        return downAppUrl;

    }

}
