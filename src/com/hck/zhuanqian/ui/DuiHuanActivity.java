package com.hck.zhuanqian.ui;

import java.util.Stack;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.adapter.DHListAdpter;
import com.hck.zhuanqian.data.ZhuanQianJiLu;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.Pdialog;

public class DuiHuanActivity extends BaseActivity {
    private ListView listView;
    private WebView mWebView;
    private String url;
    private static Stack<DuiHuanActivity> activityStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duihuan);
        initData();
        initTitle("�һ�����");
        initView();
        initWebView();
        getDuiHuanJiLuData();
    }

    private void initData() {
        url = getIntent().getStringExtra("url");
        if (url == null) {
            throw new RuntimeException("url can't be blank");
        }
        // ����������ջ������ģ��ԭ��Ӧ�õ�ҳ����ת��
        if (activityStack == null) {
            activityStack = new Stack<DuiHuanActivity>();
        }
        activityStack.push(this);
    }

    private void initView() {
       // listView = (ListView) findViewById(R.id.dh_list);
        initWebView();

    }

    private void getDuiHuanJiLuData() {
        Pdialog.showDialog(this, "���ڻ�ȡ����...", true);
        Request.getDuiHuanJiLu(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D(response.toString());
                try {
                    isOK = response.getBoolean("isok");
                    if (isOK) {
                        showDuiHuan(response.toString());
                    } else {
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                Pdialog.hiddenDialog();
            }
        });
    }

    private void showDuiHuan(String data) {
        try {
            ZhuanQianJiLu zhuanQianJiLu = new ZhuanQianJiLu();
            zhuanQianJiLu = JsonUtils.parse(data, ZhuanQianJiLu.class);
            listView.setAdapter(new DHListAdpter(DuiHuanActivity.this, zhuanQianJiLu.getOrderBeans()));
            listView.addHeaderView(mWebView);
        } catch (Exception e) {
        }

    }

    private void initWebView() {
        mWebView = new WebView(this);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        WebSettings settings = mWebView.getSettings();

        // User settings
        settings.setJavaScriptEnabled(true); // ����webview֧��javascript
        settings.setLoadsImagesAutomatically(true); // ֧���Զ�����ͼƬ
        settings.setUseWideViewPort(true); // ����webview�Ƽ�ʹ�õĴ��ڣ�ʹhtml��������Ӧ��Ļ
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true); // ����webview���������
        settings.setSavePassword(true); // ����webview��������
        settings.setDefaultZoom(ZoomDensity.MEDIUM); // �����е������ܶȣ�medium=160dpi
        settings.setSupportZoom(true); // ֧������

        CookieManager.getInstance().setAcceptCookie(true);

        if (Build.VERSION.SDK_INT > 8) {
            settings.setPluginState(PluginState.ON_DEMAND);
        }

        // Technical settings
        settings.setSupportMultipleWindows(true);
        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);
        mWebView.getChildCount();

        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        loadUrl();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                return false;
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void loadUrl() {
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }

}
