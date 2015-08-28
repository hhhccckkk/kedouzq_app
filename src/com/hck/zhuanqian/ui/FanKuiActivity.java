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
		initTitle("��д���������Ϣ");
		initView();

	}

	private void initView() {
		yijianEditText = (EditText) findViewById(R.id.yijian_content);
	}

	public void submit(View view) {
		String content = yijianEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			MyToast.showCustomerToast("�������Ϊ��");
		} else if (content.length() > 140) {
			MyToast.showCustomerToast("�������ܳ���140��");
		} else if (content.length() <5) {
			MyToast.showCustomerToast("��������С��5��");
		} else {
			addYiJian(content);
		}
	}

	private void addYiJian(String content) {
		Pdialog.showDialog(this, "������...", true);
		params = new RequestParams();
		params.put("content", content);
		params.put("uid", MyData.getData().getUserBean().getId() + "");
		Request.addYiJian(params, new JsonHttpResponseHandler() {
			public void onFailure(Throwable error, String content) {
				LogUtil.D(error+content);
				MyToast.showCustomerToast("�������ʧ�� ������������");
			};

			public void onSuccess(int statusCode, org.json.JSONObject response) {
			 try {
				 isOK =response.getBoolean("isok");
				if (isOK) {
					MyToast.showCustomerToast("����������յ� лл֧��");
					yijianEditText.setText("");
				}
				else {
					MyToast.showCustomerToast("�������ʧ��");
				}
			} catch (Exception e) {
				MyToast.showCustomerToast("�������ʧ��");
			}
			};

			public void onFinish(String url) {
				Pdialog.hiddenDialog();
			};
		});
	}
}
