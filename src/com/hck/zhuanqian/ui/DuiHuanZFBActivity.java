package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.ShareUtil;
import com.hck.zhuanqian.view.AlertDialogs;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;
import com.hck.zhuanqian.view.AlertDialogs.OneBtOnclick;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DuiHuanZFBActivity extends BaseActivity implements OnClickListener {
	private static final int DUIHUAN_1ZFB = 0;
	private static final int DUIHUAN_5ZFB = 1;
	private static final int DUIHUAN_10ZFB = 2;
	private static final int DUIHUAN_20ZFB = 3;
	private static final int DUIHUAN_50ZFB = 4;
	private static final int DUIHUAN_100ZFB = 5;
	private List<Button> buttons;
	private int postion;
	private TextView myJinBiTextView, needPoinTextView;
	private int money;
	private int needPoint;
	private int userPoint;
	private Button subMitBtn;
	private TextView errorTextView;
	private EditText zfbEditText, userEditText;
	private int zhifubaoSize;
    private int moneySize;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_duihuan_zfb);
		initTitle("�һ�֧����");
		initData();
		initView();
	}

	private void initData() {
		userPoint = (int) (MyData.getData().getUserBean().getAllKeDouBi());
		money = userPoint / 1000;
	}

	private void initView() {
		buttons = new ArrayList<>();
		buttons.add((Button) findViewById(R.id.duihuan_1));
		buttons.add((Button) findViewById(R.id.duihuan_5));
		buttons.add((Button) findViewById(R.id.duihuan_10));
		buttons.add((Button) findViewById(R.id.duihuan_20));
		buttons.add((Button) findViewById(R.id.duihuan_50));
		buttons.add((Button) findViewById(R.id.duihuan_100));
		findViewById(R.id.duihuan_1).setOnClickListener(this);
		findViewById(R.id.duihuan_5).setOnClickListener(this);
		findViewById(R.id.duihuan_10).setOnClickListener(this);
		findViewById(R.id.duihuan_20).setOnClickListener(this);
		findViewById(R.id.duihuan_50).setOnClickListener(this);
		findViewById(R.id.duihuan_100).setOnClickListener(this);
		myJinBiTextView = (TextView) findViewById(R.id.duihuan_my_jinbi);
		needPoinTextView = (TextView) findViewById(R.id.duihuan_need_point);
		myJinBiTextView.setText(MyData.getData().getUserBean().getAllKeDouBi()
				+ "��" + " ���ٿɶһ�֧����" + money + "Ԫ");
		subMitBtn = (Button) findViewById(R.id.duihuan_submit);
		subMitBtn.setOnClickListener(this);
		errorTextView = (TextView) findViewById(R.id.duihuan_error_text);
		zfbEditText = (EditText) findViewById(R.id.duihuan_zfb_name);
		zfbEditText.setText(MyData.getData().getUserBean().getZhifubao());
		userEditText = (EditText) findViewById(R.id.duihuan_zfb_user_name);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.duihuan_1:
			postion = 0;
			zhifubaoSize = 1;
			moneySize=1;
			updateUi();
			break;
		case R.id.duihuan_5:
			postion = 1;
			zhifubaoSize = 5;
			moneySize=5;
			updateUi();
			break;
		case R.id.duihuan_10:
			zhifubaoSize = 10;
			postion = 2;
			moneySize=10;
			updateUi();
			break;
		case R.id.duihuan_20:
			zhifubaoSize = 20;
			postion = 3;
			updateUi();
			moneySize=20;
			break;
		case R.id.duihuan_50:
			zhifubaoSize = 50;
			postion = 4;
			updateUi();
			break;
		case R.id.duihuan_100:
			zhifubaoSize = 100;
			moneySize=100;
			postion = 5;
			updateUi();
			break;
		case R.id.duihuan_submit:
			checkoutData();
			break;
		default:
			break;
		}

	}

	private void checkoutData() {
		String qqString = zfbEditText.getText().toString();
		String userName = userEditText.getText().toString();
		if (TextUtils.isEmpty(qqString)) {
			MyToast.showCustomerToast("qq���벻��Ϊ��");
			return;
		}
		sendDataToServer(qqString, userName);
	}

	private void sendDataToServer(String zfb, String userName) {
		UserBean userBean = MyData.getData().getUserBean();
		if (zhifubaoSize <= 0) {
			MyToast.showCustomerToast("�һ���������Ϊ0��");
			return;
		}
		Pdialog.showDialog(this, "�����µ� ���Ե�..", false);
		params = new RequestParams();
		params.put("id", userBean.getId() + "");
		params.put("money", needPoint + "");
		params.put("content", "֧�����˺ţ�" + zfb + "h�һ������" + zhifubaoSize + "Ԫ");
		params.put("shangjia1", userBean.getShangjia1() + "");
		params.put("shangjia2", userBean.getShangjia2() + "");
		params.put("shangjia3", userBean.getShangjia3() + "");
		params.put("userName", userBean.getName());
		params.put("size", zhifubaoSize + "");
		Request.addOrder(params, new JsonHttpResponseHandler() {
			public void onFinish(String url) {
				Pdialog.hiddenDialog();
			};

			public void onSuccess(int statusCode, org.json.JSONObject response) {
				LogUtil.D(response.toString());
				try {
					isOK = response.getBoolean("isok");
					if (isOK) {
						int jinbi = response.getInt("jinbi");
						if (jinbi == -1) {
							AlertDialogs.alert(DuiHuanZFBActivity.this, "ʧ��",
									"��Ҳ��㣬�뵽��ҳ���������û���Ϣ", true,

									new OneBtOnclick() {

										@Override
										public void callBack(int tag) {
											finish();
										}
									}, 1);
						} else {
							updateUser();
							remindNeedPoint();
							showSuccessDialog();
						}

					} else {
						MyToast.showCustomerToast("�����쳣 �µ�ʧ��");
					}
				} catch (Exception e) {
					MyToast.showCustomerToast("�����쳣 �µ�ʧ��");
				}

			};

			public void onFailure(Throwable error, String content) {
				LogUtil.D(content + error);
				MyToast.showCustomerToast("�����쳣 �µ�ʧ��");
			};
		});
	}

	private void updateUser() {
		long jinbi = MyData.getData().getUserBean().getAllKeDouBi();
		long zhuanqian = MyData.getData().getUserBean().getAllMoney();
		MyData.getData().getUserBean().setAllKeDouBi(jinbi - needPoint);
		MyData.getData().getUserBean().setAllMoney(zhuanqian + zhifubaoSize);
		initData();
	}

	 private void showSuccessDialog() {
	        final String shareContent = "����ֻ�׬Ǯ������ܲ����նһ���" + moneySize + "Ԫ֧������ϣ�����һ����׬Ǯ,��װ���ͺ��";
	        AlertDialogs.alert(this, "�������", "�µ��ɹ�." + "������ѿ��Ի�ȡ�齱����,�����Է�չ����" + "   (ע�⣺����qq���ѣ��������������qq��Ȼ���ٷ��ر�app����Ȼ��ȡ�����齱����,QQbug)", true,

	        new OneBtOnclick() {

	            @Override
	            public void callBack(int tag) {
	                ShareUtil.share(DuiHuanZFBActivity.this, shareContent, handler);
	            }
	        }, 1);
	    }
	 Handler handler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            if (msg.what == 0) {
	                MyToast.showCustomerToast("����ʧ��");
	            } else if (msg.what == 1) {
	                MyToast.showCustomerToast("����ɹ�");
	                addChouJiangSize();

	            } else if (msg.what == 2) {
	                MyToast.showCustomerToast("����ȡ��");
	            }
	        };
	    };
	    private void addChouJiangSize() {

	        Pdialog.showDialog(this, "���Եȣ��������ӳ齱����", false);
	        UserBean userBean = MyData.getData().getUserBean();
	        RequestParams params = new RequestParams();
	        params.put("uid", userBean.getId() + "");
	        params.put("cjSize", 1 + "");
	        Request.updateChouJiangSize(params, new JsonHttpResponseHandler() {
	            @Override
	            public void onFailure(Throwable error, String content) {
	                super.onFailure(error, content);
	                LogUtil.D("onFailure: " + error + content);
	                MyToast.showCustomerToast("�����쳣 ���ӳ齱����ʧ��");
	            }

	            @Override
	            public void onSuccess(int statusCode, JSONObject response) {
	                super.onSuccess(statusCode, response);
	                LogUtil.D("onSuccess: " + response.toString());
	                MyToast.showCustomerToast("��ϲ����������һ�γ齱����");
	                updateUserChouJiang();
	            }

	            @Override
	            public void onFinish(String url) {
	                super.onFinish(url);
	                Pdialog.hiddenDialog();
	            }

	        });
	    }
	    private void updateUserChouJiang() {
	        UserBean userBean = MyData.getData().getUserBean();
	        int choujiang = userBean.getChoujiang();
	        userBean.setChoujiang(choujiang + 1);
	        MyData.getData().setUserBean(userBean);
	    }
	private void updateUi() {
		changeBtnbg();
		remindNeedPoint();
	}

	private void changeBtnbg() {
		for (int i = 0; i < buttons.size(); i++) {
			if (i == postion) {
				buttons.get(postion).setBackgroundResource(
						R.drawable.duihuan_btn_bg_press);

			} else {
				buttons.get(i).setBackgroundResource(
						R.drawable.duihuan_btn_normol);
			}
		}
	}

	private void remindNeedPoint() {
		switch (postion) {
		case DUIHUAN_1ZFB:
			needPoint = 1000;
			break;
		case DUIHUAN_5ZFB:
			needPoint = (int) (5000 * 0.98);
			break;
		case DUIHUAN_10ZFB:
			needPoint = (int) (10000 * 0.95);
			break;
		case DUIHUAN_20ZFB:
			needPoint = (int) (20000 * 0.9);
			break;
		case DUIHUAN_50ZFB:
			needPoint = (int) (50000 * 0.85);
			break;
		case DUIHUAN_100ZFB:
			needPoint = (int) (100000 * 0.80);
			break;

		default:
			break;
		}
		if (userPoint < needPoint) {
			needPoinTextView.setText("��Ҫ���: " + needPoint);
			subMitBtn.setEnabled(false);
			errorTextView.setVisibility(View.VISIBLE);

		} else {
			needPoinTextView.setText("��Ҫ���: " + needPoint + "��");
			subMitBtn.setEnabled(true);
			errorTextView.setVisibility(View.GONE);
		}

	}

}
