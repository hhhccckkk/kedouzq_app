package com.hck.zhuanqian.ui;

import org.json.JSONObject;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.adapter.DHListAdpter;
import com.hck.zhuanqian.data.ZhuanQianJiLu;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class DuiHuanActivity extends BaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_duihuan);
		initTitle("×îÐÂ¶Ò»»¼ÇÂ¼");
		initView();
		getDuiHuanJiLuData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.dh_list);

	}

	private void getDuiHuanJiLuData() {
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
						ZhuanQianJiLu zhuanQianJiLu = new ZhuanQianJiLu();
						zhuanQianJiLu = JsonUtils.parse(response.toString(),
								ZhuanQianJiLu.class);
						listView.setAdapter(new DHListAdpter(
								DuiHuanActivity.this, zhuanQianJiLu
										.getOrderBeans()));

					} else {
					}
				} catch (Exception e) {
				}

			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
			}
		});
	}

	public void duihuanQQ(View view) {
		startActivity(new Intent(this, DuiHuanQQActivity.class));
	}
	public void duihuanZFB(View view) {
		startActivity(new Intent(this, DuiHuanZFBActivity.class));
	}

}
