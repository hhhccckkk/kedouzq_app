package com.hck.zhuanqian.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.view.AlertDialogs;
import com.hck.zhuanqian.view.MyToast;
import com.lostip.sdk.offerwalllibrary.LostipOfferWall;
import com.lostip.sdk.offerwalllibrary.LostipOfferWallListener;
import com.lostip.sdk.offerwalllibrary.entity.Point;

public class KeKeActivity extends BaseActivity {
	private int point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAd();
		setContentView(R.layout.activity_keke);
		initTitle("С��ר��");
		getAdInitData();
		initDownSize(findViewById(R.id.down_size));
		initView();
	}

	private void initView() {
		initTitle("С��׬Ǯר��");
		webView = (WebView) findViewById(R.id.dianle_webview);
		initDate();
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

	private void initDate() {
		webView.loadUrl("file:///android_asset/keke.html");
	}

	private void initAd() {
	    try {
	        LostipOfferWall.setPlatformId(Contans.KE_KE_KEY);
	        LostipOfferWall.init(this);
        } catch (Exception e) {
            MyToast.showCustomerToast("��ʼ������ʧ��");
        }
		

	}

	public void startGetMoney(View view) {
		if (!getAirplaneMode()) {
			nt2();
			return;
		}
		LostipOfferWall.setOnCloseListener(new LostipOfferWallListener<Void>() {
			@Override
			public void onSucceed(Void result) {

			}

			@Override
			public void onError(int errorCode, String errorMsg) {
			}
		});
		LostipOfferWall
				.setOnActivatedListener(new LostipOfferWallListener<Point>() {
					@Override
					public void onSucceed(Point result) {

						try {
							if (result != null) {
								point = 0;
								point = result.balance;
								if (point > 1000) {
									point = 100;
								}
								if (point > 0) {
									savePoint(Contans.AD_NAME_KEKE, point);
								}
								handler.sendEmptyMessage(point);

							}
						} catch (Exception e) {
						}

					}

					@Override
					public void onError(int errorCode, String errorMsg) {
					}
				});

		LostipOfferWall.open(this, new LostipOfferWallListener<Void>() {
			@Override
			public void onSucceed(Void result) {
			}

			@Override
			public void onError(int errorCode, String errorMsg) {
			}
		});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			huafei(msg.what);
		};
	};

	private void huafei(int point) {
		LostipOfferWall.usePoint(point, "����",
				new LostipOfferWallListener<Point>() {
					@Override
					public void onSucceed(Point result) {
					}

					@Override
					public void onError(int errorCode, String errorMsg) {
						finish();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LostipOfferWall.destroy(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			LostipOfferWall.getPoint(new LostipOfferWallListener<Point>() {

				@Override
				public void onError(int arg0, String arg1) {

				}

				@Override
				public void onSucceed(Point arg0) {
					handler.sendEmptyMessage(arg0.balance);

				}

			});
		} catch (Exception e) {
		}

	}

}
