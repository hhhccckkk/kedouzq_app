package com.hck.zhuanqian.ui;

import android.os.Bundle;
import android.view.View;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.util.LogUtil;
import com.yql.sdk.DRScoreInterface;
import com.yql.sdk.DRSdk;

public class DianRuActivity extends BaseActivity {
	private int point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_midi);
		initAD();
	}

	private void initAD() {
		DRSdk.initialize(this, false, null);
	}

	public void showAd(View view) {
		DRSdk.showAdWall(this, DRSdk.DR_OFFER);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPoint();
	}

	private void getPoint() {
		DRSdk.getScore(this, new DRScoreInterface() {

			@Override
			public void spendScoreCallback(boolean arg0) {
				LogUtil.D("spendScoreCallback: " + arg0);
			}

			@Override
			public void scoreResultCallback(int arg0) {
				LogUtil.D("scoreResultCallback: " + arg0);

			}
		});
	}

	private void huafei() {
		DRSdk.spendScore(point, this, new DRScoreInterface() {

			@Override
			public void spendScoreCallback(boolean arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void scoreResultCallback(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
}
