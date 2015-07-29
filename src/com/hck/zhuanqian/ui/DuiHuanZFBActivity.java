package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.List;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.util.LogUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_duihuan_zfb);
		initTitle("�һ�Q��");
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
		errorTextView =(TextView) findViewById(R.id.duihuan_error_text);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.duihuan_1:
			postion = 0;
			break;
		case R.id.duihuan_5:
			postion = 1;
			break;
		case R.id.duihuan_10:
			postion = 2;
			break;
		case R.id.duihuan_20:
			postion = 3;
			break;
		case R.id.duihuan_50:
			postion = 4;
			break;
		case R.id.duihuan_100:
			postion = 5;
			break;
		case R.id.duihuan_submit:

			break;
		default:
			break;
		}
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
