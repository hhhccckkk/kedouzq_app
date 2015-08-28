package com.hck.zhuanqian.ui;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class FanKuiActivity extends BaseActivity {
	private EditText yijianEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fankui);
		initTitle("填写意见反馈信息");
		initView();

	}

	private void initView() {
		yijianEditText = (EditText) findViewById(R.id.yijian_content);
	}

	public void submit(View view) {
		String content = yijianEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			MyToast.showCustomerToast("意见不能为空");
		} else if (content.length() > 140) {
			MyToast.showCustomerToast("字数不能超过140个");
		} else if (content.length() <5) {
			MyToast.showCustomerToast("字数不能小于5个");
		} else {
			addYiJian(content);
		}
	}

	private void addYiJian(String content) {
		Pdialog.showDialog(this, "处理中...", true);
		params = new RequestParams();
		params.put("content", content);
		params.put("uid", MyData.getData().getUserBean().getId() + "");
		Request.addYiJian(params, new JsonHttpResponseHandler() {
			public void onFailure(Throwable error, String content) {
				LogUtil.D(error+content);
				MyToast.showCustomerToast("增加意见失败 请检查您的网络");
			};

			public void onSuccess(int statusCode, org.json.JSONObject response) {
			 try {
				 isOK =response.getBoolean("isok");
				if (isOK) {
					MyToast.showCustomerToast("您的意见已收到 谢谢支持");
					yijianEditText.setText("");
				}
				else {
					MyToast.showCustomerToast("增加意见失败");
				}
			} catch (Exception e) {
				MyToast.showCustomerToast("增加意见失败");
			}
			};

			public void onFinish(String url) {
				Pdialog.hiddenDialog();
			};
		});
	}
}
