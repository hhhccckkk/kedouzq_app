package com.hck.zhuanqian.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.ShareBean;
import com.hck.zhuanqian.bean.TgAppBean;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.net.Urls;
import com.hck.zhuanqian.util.FileUtil;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.ShareUtil;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

public class TGActivity extends BaseActivity {
    private String downAppUrl;
    private WebView contentWebView;
    private TextView tgLianJieTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tg);
        initTitle("推广赚钱");
        initData();
        initView();
    }

    public void share(View view) {

        ShareUtil.share(this,null , handler);

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                MyToast.showCustomerToast("分享失败");
            } else if (msg.what == 1) {
                MyToast.showCustomerToast("分享成功");
            } else if (msg.what == 2) {
                MyToast.showCustomerToast("分享取消");
            }
        };
    };

    private void initData() {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean != null) {
            downAppUrl = Urls.MAIN_HOST_URL + "downApp?uid=" + userBean.getId();
        }

    }

    private void initView() {
        contentWebView = (WebView) findViewById(R.id.tgWebView);
        tgLianJieTextView = (TextView) findViewById(R.id.tuiguanglianjie);
        tgLianJieTextView.setText(downAppUrl);
        contentWebView.loadUrl("file:///android_asset/tuiguang.html");

    }

    public void copy(View view) {
        UserBean userBean = MyData.getData().getUserBean();
        String downUrl = Urls.MAIN_HOST_URL + "downapk.jsp?id=" + userBean.getId();
        FileUtil.copy(downUrl, this);
        MyToast.showCustomerToast("复制成功，输入框长按可以粘贴");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Pdialog.hiddenDialog();
    }

}
