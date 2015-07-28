package com.hck.zhuanqian.ui;

/**
 * 主界面.
 */
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.Contans;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyTools;
import com.hck.zhuanqian.view.MyToast;

public class MainActivity extends Activity implements OnClickListener {
	private TextView mUserIdTV, mUserJinbiTv, mUserNameTv;
	private Button mAllMoneyBtn, mAllTgUserBtn;
	private PullToRefreshListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		View view = LayoutInflater.from(this).inflate(R.layout.activity_main,
				null);
		initView(view);
		setStartLabel();
		initUserData();
		setListener();
	}

	/**
	 * 设置下拉刷新布局
	 */
	private void setStartLabel() {
		ILoadingLayout startLabel = listView.getLoadingLayoutProxy(true, false);
		startLabel.setPullLabel(getString(R.string.pull_to_refresh));
		startLabel.setReleaseLabel(getString(R.string.release_to_refresh));
		startLabel.setRefreshingLabel(getString(R.string.is_refreshing));
	}

	private void initView(View view) {
		listView = (PullToRefreshListView) findViewById(R.id.mainList);
		mUserIdTV = (TextView) view.findViewById(R.id.userId);
		mUserJinbiTv = (TextView) view.findViewById(R.id.userJinbi);
		mAllMoneyBtn = (Button) view.findViewById(R.id.userMoney);
		mAllTgUserBtn = (Button) view.findViewById(R.id.userTuiguang);
		mUserNameTv = (TextView) view.findViewById(R.id.userName);
		mAllMoneyBtn.setOnClickListener(this);
		mAllTgUserBtn.setOnClickListener(this);
		view.findViewById(R.id.homeChouJiang).setOnClickListener(this);
		view.findViewById(R.id.homeHelp).setOnClickListener(this);
		view.findViewById(R.id.homeMingxi).setOnClickListener(this);
		view.findViewById(R.id.homeMore).setOnClickListener(this);
		view.findViewById(R.id.home_Hongbao).setOnClickListener(this);
		view.findViewById(R.id.home_getMoney).setOnClickListener(this);
		view.findViewById(R.id.home_Duihuan).setOnClickListener(this);
		view.findViewById(R.id.home_Tuiguang).setOnClickListener(this);
		listView.setEmptyView(view);
		listView.setMode(Mode.PULL_FROM_START);

	}

	private void setListener() {
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				updateUser();
			}
		});
	}

	private void initUserData() {
		UserBean userBean = MyData.getData().getUserBean();
		long userId = Contans.DEFAULT_ID + userBean.getId();
		mUserIdTV.setText("ID: " + userId);
		mUserJinbiTv.setText(userBean.getAllKeDouBi() + "个");
		mAllMoneyBtn.setText("一共赚钱" + userBean.getAllMoney() + "元");
		mAllTgUserBtn.setText("推广用户" + userBean.getTg() + "人");
		mUserNameTv.setText(userBean.getName());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.userTuiguang:
			 startActivity(TuiGuangActivity.class);
			break;
		case R.id.userMoney:
			 startActivity(ZhuanQianJiLuActivity.class);
			break;
		case R.id.homeChouJiang:

			break;
		case R.id.homeMingxi:
            startActivity(DuiHuanJiLuActivity.class);
			break;
		case R.id.homeHelp:

			break;
		case R.id.homeMore:

			break;
		case R.id.home_Hongbao:

			break;
		case R.id.home_getMoney:

			break;
		case R.id.home_Duihuan:

			break;
		case R.id.home_Tuiguang:

			break;

		default:
			break;
		}

	}

	private void startActivity(Class<?> class1) {
		startActivity(new Intent(this, class1));
	}

	/**
	 * 注册用户到服务器.
	 */
	private void updateUser() {
		if (TextUtils.isEmpty(MyTools.getImei(this))) {
			MyToast.showCustomerToast("获取手机imei失败");
			finish();
			return;
		} else if (MyTools.getImei(this).equals("000000000000000")) {
			MyToast.showCustomerToast("模拟器，不能使用本软件");
			this.finish();
			return;
		}
		RequestParams params = new RequestParams();
		params.put("mac", MyTools.getImei(this));
		params.put("phone", MyTools.getTel(this));
		params.put("point", 0 + "");
		params.put("xh", MyTools.getModel());
		params.put("sdk", MyTools.getSDK());
		params.put("ips", "");
		Request.addUser(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D(content + error);
				MyToast.showCustomerToast("刷新失败 请检查您的网络");

			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D(response.toString());
				boolean isok = false;
				try {
					isok = response.getBoolean("isok");
					if (isok) {
						UserBean userBean = JsonUtils.parse(
								response.getString("user"), UserBean.class);
						MyData.getData().setUserBean(userBean);
					}
					initUserData();
				} catch (Exception e) {
					e.printStackTrace();

				}

			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				LogUtil.D(url);
				listView.onRefreshComplete();
			}

		}, params);

	}

}
