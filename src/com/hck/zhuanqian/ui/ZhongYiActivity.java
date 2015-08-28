package com.hck.zhuanqian.ui;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.impl.Indenter;

import android.os.Bundle;
import android.view.View;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.data.Contans;
import com.zy.phone.SDKInit;
import com.zy.phone.net.Integral;

public class ZhongYiActivity extends BaseActivity implements Integral{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_midi);
		initAd();
	}

	private void initAd() {
		try {
			SDKInit.initAd(this, Contans.ZHONG_YI_KEY, null);
		} catch (Exception e) {
			showErrorToast();
		}

	}

	public void showAd(View view) {
		  try {
			  SDKInit.initAdList(this);
			  SDKInit.checkIntegral(this);
			} catch (Exception e) {
				showErrorToast();
			}
	}

	@Override
	public void retAddIntegral(String arg0, String arg1) {
		
	}

	@Override
	public void retCheckIntegral(String arg0, String arg1) {
		
	}

	@Override
	public void retMinusIntegral(String arg0, String arg1) {
		
	}

	

}
