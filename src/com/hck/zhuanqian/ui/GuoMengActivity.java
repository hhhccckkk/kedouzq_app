package com.hck.zhuanqian.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cn.guomob.android.intwal.GMScoreCallBack;
import cn.guomob.android.intwal.OpenIntegralWall;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.data.Contans;

public class GuoMengActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_midi);
		initAD();
	}

	public void showAd(View view) {
		OpenIntegralWall.getInstance().show();
	}

	private void initAD() {
		OpenIntegralWall.setGbKeyCode(this, Contans.GUOMENG_KEY);
		OpenIntegralWall.initInterfaceType(this, new GMScoreCallBack() {

			@Override
			public void onSuccess(Context arg0, String arg1) {

			}
		});
	}
}
