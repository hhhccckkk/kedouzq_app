package com.hck.zhuanqian.ui;

import net.youmi.android.AdManager;
import net.youmi.android.offers.EarnPointsOrderList;
import net.youmi.android.offers.OffersBrowserConfig;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsEarnNotify;
import net.youmi.android.offers.PointsManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.AlertDialogs;
import com.hck.zhuanqian.view.MyToast;

public class YouMiActivity extends BaseActivity implements PointsEarnNotify {

	private int point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAd();
		setContentView(R.layout.activity_youmi);
		getAdInitData();
		initDownSize(findViewById(R.id.down_size));
		initView();
		
	}

	public void nt2() {
		try {
			if (!getAirplaneMode()) {
				AlertDialogs.alert(this, "��֪����",
						"������ʾ���������迪���ֻ�����ģʽ, �������Է�ֹ���Լ����������������������Ҫ�ķ���", true);
			}
		} catch (Exception e) {
		}

	}

	private void initView() {
		initTitle("����׬Ǯר��");
		webView = (WebView) findViewById(R.id.dianle_webview);
		initDate();
	}

	private void initDate() {
		webView.loadUrl("file:///android_asset/youmi.html");
	}

	private void initAd() {
		try {
			AdManager.getInstance(this).init(Contans.YOUMI_ID,
					Contans.YOUMI_KEY);
			OffersManager.getInstance(this).onAppLaunch();
			PointsManager.getInstance(this).registerPointsEarnNotify(this);
			OffersBrowserConfig.setPointsLayoutVisibility(false);

		} catch (Exception e) {
			MyToast.showCustomerToast("��ʼ��ʧ�� ������");
			finish();
		}

	}

	public void startGetMoney(View view) {
		if (!getAirplaneMode()) {
			nt2();
			return;
		}
		try {
			OffersManager.getInstance(this).showOffersWall();

		} catch (Exception e) {
			MyToast.showCustomerToast("������ʧ�� ������");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			OffersManager.getInstance(this).onAppExit();
			PointsManager.getInstance(this).unRegisterPointsEarnNotify(this);
		} catch (Exception e) {
		}

	}

	@Override
	public void onPointEarn(Context arg0, EarnPointsOrderList arg1) {
		LogUtil.D("��ȡ��� ����: ");
		point = 0;
		point = arg1.get(0).getPoints();
		if (point > 0) {
			savePoint(Contans.AD_NAME_YOUMI, point);
		}
	}

}
