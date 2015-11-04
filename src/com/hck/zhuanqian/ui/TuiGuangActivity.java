package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.adapter.TuiGuangAdapter;
import com.hck.zhuanqian.bean.TuiGBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.data.TuiGuangData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;

public class TuiGuangActivity extends BaseActivity {
	private PullToRefreshListView listView;
	private int page = 1;
	private View mLoadingView;
	private View errorView;
	private TuiGuangData tData = new TuiGuangData();
	private List<TuiGBean> tuiGBeans = new ArrayList<>();
	private TuiGuangAdapter adapter;
	private boolean isResh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuanqian_jilu);
		initTitle("推广赚钱记录");
		initView();
		getTuiGuangData();
		setListener();
	}

	private void initView() {
		listView = (PullToRefreshListView) findViewById(R.id.zhuanqianList);
		listView.setMode(Mode.BOTH);
		mLoadingView = findViewById(R.id.loading);
		errorView = LayoutInflater.from(this)
				.inflate(R.layout.error_view, null);

	}

	@SuppressWarnings("unchecked")
	private void setListener() {
		listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				isResh = true;
				page = 1;
				getTuiGuangData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				getTuiGuangData();

			}
		});
	}

	/**
	 * 获取推广记录信息.
	 */
	private void getTuiGuangData() {
		params = new RequestParams();
		params.put("uid", MyData.getData().getUserBean().getId() + "");
		params.put("page", page + "");
		Request.getTG(new JsonHttpResponseHandler() {
			public void onFailure(Throwable error, String content) {
				LogUtil.D("onFailure: " + content);
				listView.setEmptyView(errorView);
			};

			public void onFinish(String url) {
				mLoadingView.setVisibility(View.GONE);
				listView.onRefreshComplete();
			};

			public void onSuccess(int statusCode, org.json.JSONObject response) {
				try {
					isOK = response.getBoolean("isok");
					if (isOK) {
						if (tData.getTuiGBeans() != null && isResh) {
							tData.getTuiGBeans().clear();
							tuiGBeans.clear();
						}
						TuiGuangData data = JsonUtils.parse(
								response.toString(), TuiGuangData.class);
						tuiGBeans.addAll(data.getTuiGBeans());
						tData.setTuiGBeans(tuiGBeans);
						updateUI();
					} else {
						listView.setEmptyView(errorView);
					}

				} catch (Exception e) {
					listView.setEmptyView(errorView);
				}
			};
		}

		, params);

	}

	private void updateUI() {
		if (tData.getTuiGBeans() != null && !tData.getTuiGBeans().isEmpty()) {
			if (isResh) {
				adapter = null;
				adapter = new TuiGuangAdapter(tData, this);
				listView.setAdapter(adapter);
				return;
			}
			if (adapter == null) {
				adapter = new TuiGuangAdapter(tData, this);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged(tData);
			}
		}
		isResh = false;
	}

}
