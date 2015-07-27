package com.hck.zhuanqian.ui;

/**
 * 主界面.
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.Contans;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;

public class MainActivity extends Activity implements OnClickListener {
	private TextView mUserIdTV, mUserJinbiTv, mUserNameTv;
	private Button mAllMoneyBtn, mAllTgUserBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initUserData();
	}

	private void initView() {
		mUserIdTV = (TextView) findViewById(R.id.userId);
		mUserJinbiTv = (TextView) findViewById(R.id.userJinbi);
		mAllMoneyBtn = (Button) findViewById(R.id.userMoney);
		mAllTgUserBtn = (Button) findViewById(R.id.userTuiguang);
		mUserNameTv = (TextView) findViewById(R.id.userName);
		mAllMoneyBtn.setOnClickListener(this);
		mAllTgUserBtn.setOnClickListener(this);
		findViewById(R.id.homeChouJiang).setOnClickListener(this);
		findViewById(R.id.homeHelp).setOnClickListener(this);
		findViewById(R.id.homeMingxi).setOnClickListener(this);
		findViewById(R.id.homeMore).setOnClickListener(this);
		findViewById(R.id.home_Hongbao).setOnClickListener(this);
		findViewById(R.id.home_getMoney).setOnClickListener(this);
		findViewById(R.id.home_Duihuan).setOnClickListener(this);
		findViewById(R.id.home_Tuiguang).setOnClickListener(this);
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
			//startActivity(MainActivity.class);
			break;
		case R.id.userMoney:

			break;
		case R.id.homeChouJiang:

			break;
		case R.id.homeMingxi:

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
	private void startActivity(Class<?> class1){
		startActivity(new Intent(this,class1));
	}

}
