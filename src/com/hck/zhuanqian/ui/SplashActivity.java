package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
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
import com.hck.zhuanqian.view.Pdialog;

public class SplashActivity extends Activity {
    private static final int LOGIN_ERROR = 0;
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_CANCEL = 2;
    private final long mAnimationTime = 1500L;
    private ImageView mImageView;
    private String shangjiaString;
    private List<Long> ids = new ArrayList<>();
    private Button loginBtn;
    private View pBar;
    CustomAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_splash);
        initView();
        startAnim();
        getShareInfo();
        setListener();
    }

    private void initData() {
        try {
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
        } catch (Exception e) {
            ids=null;
        }
       

    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.loding_img);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        pBar = findViewById(R.id.pb);
    }

    private void setListener() {
        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                loginBtn.setFocusable(false);
                loginQQ();
            }
        });
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
                pBar.setVisibility(View.GONE);
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
                        userLogin();
                        pBar.setVisibility(View.GONE);
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
                LogUtil.D("url: " + url);
               
            }
        }, 2);
    }

    private void alertLoginD(String content) {
        if (isFinishing()) {
            return;
        }

        dialog= new CustomAlertDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("我知道了");
        dialog.setRightText("登录");
        dialog.setTitle("提示");
        dialog.setMessage(content);
        dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               
            }
        });

        dialog.hideRightBtn();
        if (!isFinishing() && dialog != null) {
            dialog.show();
        }
    }

    private void userLogin() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        if (qq != null && qq.isAuthValid()) {
            loginQQ();
        } else {
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private void loginQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        if (qq == null) {
            MyToast.showCustomerToast("登录失败");
            return;
        }
        if (qq != null && !qq.isAuthValid()) {
            MyToast.showCustomerToast("正在启动qq...");
        }
        qq.SSOSetting(false);
        qq.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Message message = new Message();
                message.what = LOGIN_ERROR;
                handler.sendMessage(message);

            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Message message = new Message();
                message.what = LOGIN_SUCCESS;
                message.obj = arg0;
                handler.sendMessage(message);

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                Message message = new Message();
                message.what = LOGIN_CANCEL;
                handler.sendMessage(message);

            }
        });
        qq.showUser(null);

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            loginBtn.setFocusable(true);
            if (msg.what == LOGIN_ERROR || msg.what == LOGIN_CANCEL) {
                MyToast.showCustomerToast("登录失败 您可以重新登录");
            } else if (msg.what == LOGIN_SUCCESS) {
                if (isFinishing()) {
                    return;
                }
                loginBtn.setVisibility(View.GONE);
                pBar.setVisibility(View.VISIBLE);
                Platform platform = (Platform) msg.obj;
                PlatformDb platDB = platform.getDb();
                if (platDB != null) {
                    addUser(platDB.getUserId(), platDB.getUserName(), platDB.getUserIcon());
                } else {
                    MyToast.showCustomerToast("登录失败");
                }
            }

        };
    };

    /**
     * 注册用户到服务器.
     */
    private void addUser(String userId, String userName, String userTX) {
        LogUtil.D("id: "+userId);
        if (TextUtils.isEmpty(MyTools.getImei(this))) {
            MyToast.showCustomerToast("获取手机imei失败");
            showGetImeiErrorDialog();
            return;
        } else if (MyTools.getImei(this).equals("000000000000000")) {
            MyToast.showCustomerToast("获取手机imei失败");
            showGetImeiErrorDialog();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("mac", MyTools.getImei(this) + "");
        if (MyTools.getTel(this) != null) {
            params.put("phone", MyTools.getTel(this) + "");
        }
        params.put("point", 0 + "");
        params.put("xh", MyTools.getModel() + "");
        params.put("sdk", MyTools.getSDK() + "");
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("userTX", userTX);

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
            } else if (ids.size() == 5) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
                params.put("shangjia4", ids.get(3) + "");
                params.put("shangjia5", ids.get(4) + "");
            } else if (ids.size() == 6) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
                params.put("shangjia4", ids.get(3) + "");
                params.put("shangjia5", ids.get(4) + "");
                params.put("shangjia6", ids.get(5) + "");

            } else if (ids.size() == 7) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
                params.put("shangjia4", ids.get(3) + "");
                params.put("shangjia5", ids.get(4) + "");
                params.put("shangjia6", ids.get(5) + "");
                params.put("shangjia7", ids.get(6) + "");
            } else if (ids.size() == 8) {
                params.put("shangjia1", ids.get(0) + "");
                params.put("shangjia2", ids.get(1) + "");
                params.put("shangjia3", ids.get(2) + "");
                params.put("shangjia4", ids.get(3) + "");
                params.put("shangjia5", ids.get(4) + "");
                params.put("shangjia6", ids.get(5) + "");
                params.put("shangjia7", ids.get(6) + "");
                params.put("shangjia8", ids.get(7) + "");
            }

        }
        Request.addUser(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D(content + error + content);
                MyToast.showCustomerToast("登录失败 请检查您的网络");
                loginBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                int type = 0;
                UserBean userBean = null;
                try {
                    type = response.getInt("type");
                    if (type == Request.REQUEST_SUCCESS) {
                        userBean = JsonUtils.parse(response.getString("user"), UserBean.class);
                          if(userBean!=null && userBean.getIsok()==0){
                              alertLoginD("您违反了相关规定，账号已被禁用"); 
                              clearnUser();
                              return;
                        }
                        MyPreferences.saveString("user", response.getString("user"));
                        MyData.getData().setUserBean(userBean);
                        startMainActivity();
                    } else if (type == 0) {
                        MyToast.showCustomerToast("登录失败 请检查您的网络");
                    } else if (type == 2 || type==3) {
                        String errorMsg=response.getString("msg");
                        alertLoginD(errorMsg);
                        clearnUser();
                        loginBtn.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyToast.showCustomerToast("您的QQ账号不对,登录失败");
                }

            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                LogUtil.D(url);
                pBar.setVisibility(View.GONE);
            }

        }, params);

    }

    private void clearnUser() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        if (qq != null) {
            qq.removeAccount();
        }
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
        dialog.setMessage("您的网络不稳定,或者服务器维护中");
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

    public void showGetImeiErrorDialog() {
        if (isFinishing()) {
            return;
        }

        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.hideRightBtn();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("退出");
        dialog.setRightText("重试");
        dialog.setTitle("提示");
        dialog.setMessage("获取手机imei失败，请允许手机获取imei号,手机唯一标识，谢谢");
        dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppManager.getAppManager().AppExit();
                finish();
                System.gc();
            }
        });

        if (!isFinishing() && dialog != null) {
            dialog.show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
        if (dialog!=null) {
            dialog.dismiss();
            dialog=null;
        }
    }

}
