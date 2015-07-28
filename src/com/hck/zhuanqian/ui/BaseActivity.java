package com.hck.zhuanqian.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.view.TitleBar;

/**
 * 基础界面.
 */
public class BaseActivity extends FragmentActivity {
	protected TitleBar mTitleBar;
	public RequestParams params;
	public boolean isOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layout) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initTitleBar();
		ViewGroup root = getRootView();
		View paramView = getLayoutInflater().inflate(layout, null);
		root.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		root.addView(paramView, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		super.setContentView(root);
	}



	private ViewGroup getRootView() {
		LinearLayout localLinearLayout = new LinearLayout(this);
		localLinearLayout.setOrientation(LinearLayout.VERTICAL);
		return localLinearLayout;
	}


	private void initTitleBar() {
		mTitleBar = new TitleBar(this);
	}

	public TitleBar getTitleBar() {
		return mTitleBar;
	}

	public String getStringData(int id) {
		return getResources().getString(id);
	}
	public void initTitle(String content) {
		mTitleBar.setTitleContent(content);

	}

}
