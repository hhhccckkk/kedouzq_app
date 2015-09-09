package com.hck.zhuanqian.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
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
import com.hck.zhuanqian.bean.ShareBean;
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
    private List<Long> ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        setContentView(R.layout.activity_splash);
        initView();
        startAnim();
        getShareInfo();
    }

    private void initData() {
        shangjiaString = FileUtil.readFile(this);
        String id2[] = null;
        if (!TextUtils.isEmpty(shangjiaString)) {
            id2 = shangjiaString.split(",");
        }
        for (int i = 0; i < id2.length; i++) {
            try {
                long id = Long.parseLong(id2[i].trim());
                if (id > 0) {
                    ids.add(id);
                }
            } catch (Exception e) {
            }

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

    private void getShareInfo() {
        Request.getShareInfo(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                LogUtil.D("content: " + content + error);
                showNetErrorDialog();
            };

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D("shareinfo: " + response.toString());
                boolean isOK = false;
                try {
                    isOK = response.getBoolean("isok");
                } catch (Exception e) {
                }
                if (isOK) {
                    try {
                        String shareString = response.getString("info");
                        ShareBean shareBean = JsonUtils.parse(shareString, ShareBean.class);
                        MyPreferences.saveString("share", shareString);
                        MyData.getData().setShareBean(shareBean);
                        addUser();
                    } catch (Exception e) {
                        MyToast.showCustomerToast("服务器正在维护中");
                        e.printStackTrace();
                    }
                } else {
                    MyToast.showCustomerToast("服务器正在维护中");
                }

            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
            }
        }, 1);
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
            MyToast.showCustomerToast("获取手机imei失败");
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
            if (ids.size() == 1) {
                params.put("shangjia1", ids.get(0) + "");
            } else if (ids.size() == 2) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
            } else if (ids.size() == 3) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
            } else if (ids.size() == 4) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
                params.put("shangjia4", ids.get(3) + "");
            }

            else if (ids.size() == 5) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
                params.put("shangjia4", ids.get(3) + "");
                params.put("shangjia5", ids.get(4) + "");
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
                        UserBean userBean = JsonUtils.parse(response.getString("user"), UserBean.class);
                        MyPreferences.saveString("user", response.getString("user"));
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
                getShareInfo();
            }
        });
        if (!isFinishing() && dialog != null) {
            dialog.show();
        }

    }

}
