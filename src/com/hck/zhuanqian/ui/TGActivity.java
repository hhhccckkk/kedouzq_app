package com.hck.zhuanqian.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.TgAppBean;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.net.Urls;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

public class TGActivity extends BaseActivity {
	private Button getUrlButton;
	private WebView contentWebView;
	private TextView downAppurlTextView;
	private LinearLayout showMyTGUrLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tg);
		initTitle("推广赚钱(必看)");
		initView();
		getDownUrl();
		initData();
	}

	public void share(View view) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize();
		oks.setCallback(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				LogUtil.D("分享: " + arg0.toString() + arg1 + arg2);
				MyToast.showCustomerToast("分享失败");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				LogUtil.D("分享: onComplete");
				MyToast.showCustomerToast("分享成功");
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				MyToast.showCustomerToast("分享取消");
			}
		});
		oks.setDialogMode();
		oks.setText("我是分享文本");
		oks.setTitle("最好的手机赚钱软件");
		oks.setUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		oks.setTitleUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		// 启动分享GUI
		oks.show(this);

	}

	private void initData() {
		contentWebView.loadUrl("file:///android_asset/tg.html");
	}

	private void initView() {
		getUrlButton = (Button) findViewById(R.id.getUrlBtn);
		contentWebView = (WebView) findViewById(R.id.tgWebView);
		downAppurlTextView = (TextView) findViewById(R.id.app_down_url);
		showMyTGUrLayout = (LinearLayout) findViewById(R.id.showAppUrl);
		getUrlButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				daBao();
			}
		});

	}

	private void getDownUrl() {
		Pdialog.showDialog(this, "获取数据中", true);
		Request.getMyTgAppUrl(new JsonHttpResponseHandler() {
			public void onFailure(Throwable error, String content) {
				LogUtil.D("TGActivity getDownUrl: " + content + error);
			};

			public void onFinish(String url) {
				Pdialog.hiddenDialog();
				LogUtil.D(url);
			};

			public void onSuccess(int statusCode, JSONObject response) {
				LogUtil.D("TGActivity onSuccess: " + response.toString());
				try {
					isOK = response.getBoolean("isok");
					if (isOK) {
						getUrlButton.setVisibility(View.GONE);
						showMyTGUrLayout.setVisibility(View.VISIBLE);
						TgAppBean appBean = JsonUtils.parse(
								response.getString("tgapp"), TgAppBean.class);
						updateView(appBean);
					} else {
						getUrlButton.setVisibility(View.VISIBLE);
						showMyTGUrLayout.setVisibility(View.GONE);
					}
				} catch (Exception e) {

				}
			};
		});
	}

	private void updateView(TgAppBean appBean) {
		downAppurlTextView.setText(Urls.MAIN_HOST_URL + appBean.getDownUrl());
	}

	public void daBao() {
		Pdialog.showDialog(this, "处理中", true);
		UserBean userBean = MyData.getData().getUserBean();
		params = new RequestParams();
		params.put("uid", userBean.getId() + "");
		params.put("uid1", userBean.getShangjia1() + "");
		params.put("uid2", userBean.getShangjia2() + "");
		params.put("uid3", userBean.getShangjia3() + "");
		params.put("uid4", userBean.getShangjia4() + "");
		Request.daBao(new JsonHttpResponseHandler() {
			@Override
			public void onFinish(String url) {
				LogUtil.D("geturl: " + url);
				Pdialog.hiddenDialog();
				super.onFinish(url);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("onFailure: " + content + error);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					isOK = response.getBoolean("isok");
					LogUtil.D("isok; " + isOK);
					if (isOK) {
						getUrlButton.setVisibility(View.GONE);
						showMyTGUrLayout.setVisibility(View.VISIBLE);
						TgAppBean appBean = JsonUtils.parse(
								response.getString("tgapp"), TgAppBean.class);
						updateView(appBean);
					} else {
						MyToast.showCustomerToast("获取失败 请重试");
					}
				} catch (Exception e) {
					LogUtil.D(e.toString());
				}
			}
		}, params);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Pdialog.hiddenDialog();
	}

}
