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
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.adapter.ZhuanQianJiLuAdapter;
import com.hck.zhuanqian.bean.OrderBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.data.ZhuanQianJiLu;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;

/**
 * ׬Ǯ��¼.
 * 
 * @author �Ƴɿ�
 * 
 */
public class ZhuanQianJiLuActivity extends BaseActivity {
	private PullToRefreshListView listView;
	private int page = 1;
	private View mLoadingView;
	private View errorView;
	private ZhuanQianJiLu zhuanQianJiLu = new ZhuanQianJiLu();
	private ZhuanQianJiLuAdapter adapter;
	private List<OrderBean> orderBeans =new ArrayList<>();
	private boolean isResh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuanqian_jilu);
		initTitle("׬Ǯ:"+MyData.getData().getUserBean().getAllMoney()+"Ԫ");
		initView();
		setListener();
		getDataFromServer();

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
				if (zhuanQianJiLu.getOrderBeans()!=null) {
					zhuanQianJiLu.getOrderBeans().clear();
				}
				orderBeans.clear();
				getDataFromServer();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				getDataFromServer();

			}
		});
	}

	/**
	 * �ӷ�������ȡ׬Ǯ��¼.
	 */
	private void getDataFromServer() {
		params = new RequestParams();
		params.put("uid", MyData.getData().getUserBean().getId() + "");
		params.put("page", page + "");
		Request.getZhuanQianJiLu(new JsonHttpResponseHandler()

		{
			public void onFinish(String url) {
				LogUtil.D("Url: " + url);
				mLoadingView.setVisibility(View.GONE);
				listView.onRefreshComplete();
			};

			public void onFailure(Throwable error, String content) {
				LogUtil.D("onFailure:" + content);
				listView.setEmptyView(errorView);
			};

			public void onSuccess(int statusCode, org.json.JSONObject response) {
				LogUtil.D("onSuccess:" + response.toString());
				try {
					isOK = response.getBoolean("isok");
					ZhuanQianJiLu data = JsonUtils.parse(response.toString(),
							ZhuanQianJiLu.class);
					orderBeans.addAll(data.getOrderBeans());
					zhuanQianJiLu.setOrderBeans(orderBeans);
					updateUI();
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.D("��������: " + e.toString());
					listView.setEmptyView(errorView);
				}
			};
		}, params);

	}

	/**
	 * �������ݵ�listView.
	 */
	private void updateUI() {
		if (zhuanQianJiLu == null || zhuanQianJiLu.getOrderBeans() == null
				|| zhuanQianJiLu.getOrderBeans().isEmpty()) {
			listView.setEmptyView(errorView);
		} else {
			if (isResh) {
				adapter = new ZhuanQianJiLuAdapter(zhuanQianJiLu, this);
				listView.setAdapter(adapter);
				return;
			}
			if (adapter == null) {
				adapter = new ZhuanQianJiLuAdapter(zhuanQianJiLu, this);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged(zhuanQianJiLu);
			}
		}
		isResh = false;
	}

}
