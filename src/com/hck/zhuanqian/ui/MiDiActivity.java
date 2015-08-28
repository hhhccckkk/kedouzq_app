package com.hck.zhuanqian.ui;

import net.midi.wall.sdk.AdWall;
import net.midi.wall.sdk.IAdWallEarnPointsNotifier;
import android.os.Bundle;
import android.view.View;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;

public class MiDiActivity extends BaseActivity implements
		IAdWallEarnPointsNotifier {
	private int point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_midi);
		AdWall.init(this, Contans.MI_DI_ID, Contans.MI_DI_KEY);
	}

	public void showAd(View view) {
		AdWall.showAppOffers(null);
		AdWall.setEarnPointsListener(this);
	}

	@Override
	public void onEarnPoints(String arg0, String arg1, Integer arg2) {
		LogUtil.D("Ã×µÏ: " + arg0 + ":" + arg1 + ":" + arg2);
	}

}
