package com.hck.zhuanqian.ui;

import org.json.JSONObject;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.adapter.DHListAdpter;
import com.hck.zhuanqian.data.ZhuanQianJiLu;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class DuiHuanJiLuActivity extends BaseActivity {
	private ListView listView;
	private View errorView;
	private View mLoadingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_duihuan_jilu);
		initTitle("最新兑换记录");
		initView();
		getDuiHuanJiLuData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.dh_list);
		errorView = LayoutInflater.from(this)
				.inflate(R.layout.error_view, null);
		mLoadingView = findViewById(R.id.loading);
	}

	private void getDuiHuanJiLuData() {
		Request.getDuiHuanJiLu(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				listView.addHeaderView(errorView);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D(response.toString());
				try {
					if (isOK) {
						ZhuanQianJiLu zhuanQianJiLu = new ZhuanQianJiLu();
						zhuanQianJiLu = JsonUtils.parse(response.toString(),
								ZhuanQianJiLu.class);
						listView.setAdapter(new DHListAdpter(
								DuiHuanJiLuActivity.this, zhuanQianJiLu
										.getOrderBeans()));
						LogUtil.D("获取兑换okokoko");
					} else {
						listView.addHeaderView(errorView);
					}
				} catch (Exception e) {
					listView.addHeaderView(errorView);
				}

			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				mLoadingView.setVisibility(View.GONE);
			}
		});
	}

}
