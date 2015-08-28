package com.hck.zhuanqian.ui;

import org.json.JSONObject;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.Config;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.FileUtil;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyPreferences;
import com.hck.zhuanqian.util.MyTools;
import com.hck.zhuanqian.view.CustomAlertDialog;
import com.hck.zhuanqian.view.MyToast;

public class SplashActivity extends Activity {
	private final long mAnimationTime = 1500L;
	private ImageView mImageView;
	private String shangjiaString;
	String[] ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initData();
		setContentView(R.layout.activity_splash);
		initView();
		startAnim();
		getConfig();
	}

	private void initData() {
		shangjiaString = FileUtil.readFile(this);
		if (!TextUtils.isEmpty(shangjiaString)) {
			ids = shangjiaString.split(",");
		}

	}

	private void initView() {
		mImageView = (ImageView) findViewById(R.id.loding_img);
	}

	/**
	 * 开始动画.
	 */
	private void startAnim() {
		Animation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(mAnimationTime);
		mImageView.setAnimation(animation);
	}

	/**
	 * 获取配置.
	 */
	private void getConfig() {

		Request.getConfig(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D("content: " + content + error);
				showNetErrorDialog();
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.D(response.toString());
				boolean isOK = false;
				try {
					isOK = response.getBoolean("isok");
				} catch (Exception e) {
				}

				if (isOK) {
					try {
						Config config = JsonUtils.parse(
								response.getString("data"), Config.class);
						MyData.getData().setConfig(config);
						MyPreferences.saveString("user", response.toString());
					} catch (Exception e) {
						LogUtil.D("getConfig: " + e.toString());
						MyToast.showCustomerToast("服务器正在维护中");
						finish();
					}
					addUser();

				} else {
					MyToast.showCustomerToast("服务器正在维护中");
					finish();
				}
			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
			}

		});
	}

	/**
	 * 注册用户到服务器.
	 */
	private void addUser() {
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
		if (MyTools.getTel(this) != null) {
			params.put("phone", MyTools.getTel(this));
		}

		params.put("point", 0 + "");
		params.put("xh", MyTools.getModel());
		params.put("sdk", MyTools.getSDK());
		if (ids != null) {
			if (ids.length == 1) {
				params.put("shangjia1", ids[0].trim());
			} else if (ids.length == 2) {
				params.put("shangjia1", ids[0].trim());
				params.put("shangjia2", ids[1].trim());
			} else if (ids.length == 3) {
				params.put("shangjia1", ids[0].trim());
				params.put("shangjia2", ids[1].trim());
				params.put("shangjia3", ids[2].trim());
			} else if (ids.length == 4) {
				params.put("shangjia1", ids[0].trim());
				params.put("shangjia2", ids[1].trim());
				params.put("shangjia3", ids[2].trim());
				params.put("shangjia4", ids[3].trim());
			}

			else if (ids.length == 5) {
				params.put("shangjia1", ids[0].trim());
				params.put("shangjia2", ids[1].trim());
				params.put("shangjia3", ids[2].trim());
				params.put("shangjia4", ids[3].trim());
				params.put("shangjia5", ids[4].trim());
			}

		}
		Request.addUser(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				LogUtil.D(content + error + content);
				showNetErrorDialog();

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
						startMainActivity();
					}
				} catch (Exception e) {
					e.printStackTrace();
					showNetErrorDialog();
				}

			}

			@Override
			public void onFinish(String url) {
				super.onFinish(url);
				LogUtil.D(url);
			}

		}, params);

	}

	private void startMainActivity() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	public void showNetErrorDialog() {
		if (isFinishing()) {
			return;
		}

		CustomAlertDialog dialog = new CustomAlertDialog(this);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setLeftText("退出");
		dialog.setRightText("重试");
		dialog.setTitle("提示");
		dialog.setMessage("您的网络不稳定");
		dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppManager.getAppManager().AppExit();
				finish();
				System.gc();
			}
		});

		dialog.setOnRightListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getConfig();
			}
		});
		if (!isFinishing() && dialog != null) {
			dialog.show();
		}

	}

}
