package com.hck.zhuanqian.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.adapter.KindAdapter;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.KindData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;

public class KindActivity extends BaseActivity {
	private ListView listView;
	private View errorView;
	private KindData kBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kind);
		initView();
		getKindFromServer();
		setListener();
	}

	private void initView() {
		initTitle("×¬Ç®×¨Çø");
		listView = (ListView) findViewById(R.id.kind_listview);
		errorView = LayoutInflater.from(this)
				.inflate(R.layout.error_view, null);
	}

	private void getKindFromServer() {
		Request.getKind(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				listView.addHeaderView(errorView);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D("onSuccess: " + response.toString());
				try {
					kBean = JsonUtils.parse(response.toString(), KindData.class);
					updateUI();
				} catch (Exception e) {
					LogUtil.D("eee: " + e.toString());
					listView.addHeaderView(errorView);
				}
			}

			@Override
			public void onFinish(String url) {
				LogUtil.D("url: " + url);
				super.onFinish(url);
			}
		});
	}

	private void updateUI() {
		listView.setAdapter(new KindAdapter(kBean.getKindBeans(), this));
	}

	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					int type = kBean.getKindBeans().get(arg2).getAid();
					int maxSize = kBean.getKindBeans().get(arg2).getNum();
					startActivity(type, maxSize);
				} catch (Exception e) {
					MyToast.showCustomerToast("Î´Öª´íÎó ÇëÖØÊÔ");
				}

			}
			
		});
	}
	

	private void startActivity(int type, int maxSize) {
		Intent intent = new Intent();
		intent.putExtra("type", type);
		intent.putExtra("maxNum", maxSize);
		switch (type) {
		case Contans.AD_DIANLE:
			intent.setClass(this, DLActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_DIANCAI:
			intent.setClass(this, DianCaiActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_DUOMENG:
			intent.setClass(this, DuoMengActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_DATOUNIAO:
			intent.setClass(this, DaTouNiaoActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_YOUMI:
			intent.setClass(this, YouMiActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_YEGUO:
			intent.setClass(this, YeGuoActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_MIDI:
			intent.setClass(this, MiDiActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_GUOMENG:
			intent.setClass(this, GuoMengActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_QI_DIAN:
			intent.setClass(this, QiDianActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_ZHONGYI:
			intent.setClass(this, ZhongYiActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_DIAN_RU:
			intent.setClass(this, DianRuActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_BEIDUO:
			intent.setClass(this, BeiDuoActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_JIONG_YOU:
			intent.setClass(this, JiongYouActivity.class);
			startActivity(intent);
			break;
		case Contans.AD_KEKE:
			intent.setClass(this, KeKeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
