package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
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
import android.widget.Toast;

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
    private UserBean userBean;
    private int choujaingSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duihuan_zfb);
        userBean = MyData.getData().getUserBean();
        initTitle("兑换支付宝");
        initView();
        initData();
    }

    private void initData() {
        userPoint = (int) (userBean.getAllKeDouBi());
        money = userPoint / 1000;
        myJinBiTextView.setText(userBean.getAllKeDouBi() + "个" + " 至少可兑换支付宝" + money + "元");
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
        subMitBtn = (Button) findViewById(R.id.duihuan_submit);
        subMitBtn.setOnClickListener(this);
        errorTextView = (TextView) findViewById(R.id.duihuan_error_text);
        zfbEditText = (EditText) findViewById(R.id.duihuan_zfb_name);
        zfbEditText.setText(userBean.getZhifubao());
        userEditText = (EditText) findViewById(R.id.duihuan_zfb_user_name);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.duihuan_1:
            postion = 0;
            zhifubaoSize = 1;
            moneySize = 1;
            updateUi();
            break;
        case R.id.duihuan_5:
            postion = 1;
            zhifubaoSize = 5;
            moneySize = 5;
            updateUi();
            break;
        case R.id.duihuan_10:
            zhifubaoSize = 10;
            postion = 2;
            moneySize = 10;
            updateUi();
            break;
        case R.id.duihuan_20:
            zhifubaoSize = 20;
            postion = 3;
            updateUi();
            moneySize = 20;
            break;
        case R.id.duihuan_50:
            zhifubaoSize = 50;
            postion = 4;
            updateUi();
            break;
        case R.id.duihuan_100:
            zhifubaoSize = 100;
            moneySize = 100;
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
            MyToast.showCustomerToast("支付宝账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            MyToast.showCustomerToast("支付宝用户名不能为空");
            return;
        }
        sendDataToServer(qqString, userName);
    }

    private void sendDataToServer(String zfb, String userName) {
        UserBean userBean = MyData.getData().getUserBean();
        if (zhifubaoSize <= 0) {
            MyToast.showCustomerToast("请选择兑换数量");
            return;
        }
        Pdialog.showDialog(this, "正在下单 请稍等..", false);
        params = new RequestParams();
        params.put("id", userBean.getId() + "");
        params.put("money", needPoint + "");
        params.put("content", "支付宝成功提现" + zhifubaoSize + "元");
        params.put("userName", userBean.getName());
        params.put("size", zhifubaoSize + "");
        params.put("info", "支付宝账号:" + zfb + "用户:" + userName + "提现 " + zhifubaoSize + "元");
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
                            AlertDialogs.alert(DuiHuanZFBActivity.this, "失败", "金币不足，请到首页下拉更新用户信息", true,

                            new OneBtOnclick() {

                                @Override
                                public void callBack(int tag) {
                                    finish();
                                }
                            }, 1);
                        } else {
                            updateUser();
                            if (choujaingSize > 0) {
                                addChouJiang(choujaingSize);
                                choujaingSize = 0;
                            }
                            remindNeedPoint();
                            showSuccessDialog();
                        }

                    } else {
                        MyToast.showCustomerToast("网络异常 下单失败");
                    }
                } catch (Exception e) {
                    MyToast.showCustomerToast("网络异常 下单失败");
                }

            };

            public void onFailure(Throwable error, String content) {
                LogUtil.D(content + error);
                MyToast.showCustomerToast("网络异常 下单失败");
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
        final String shareContent = "这个手机赚钱软件，很不错。刚兑换了" + moneySize + "元支付宝，希望大家一起来赚钱,安装就送红包";
        AlertDialogs.alert(this, "分享好友", "下单成功." + "软件不错，分享给好友一起来玩吧。您的好友通过分享链接下载本app安装打开即送您0.3-0.8元红包，并一直享有8级下线赚钱10%奖励", true,

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
                MyToast.showCustomerToast("分享失败");
            } else if (msg.what == 1) {
                MyToast.showCustomerToast("分享成功,坐等收钱");

            } else if (msg.what == 2) {
                MyToast.showCustomerToast("分享取消");
            }
        };
    };

    private void updateUi() {
        changeBtnbg();
        remindNeedPoint();
    }

    private void changeBtnbg() {
        for (int i = 0; i < buttons.size(); i++) {
            if (i == postion) {
                buttons.get(postion).setBackgroundResource(R.drawable.duihuan_btn_bg_press);

            } else {
                buttons.get(i).setBackgroundResource(R.drawable.duihuan_btn_normol);
            }
        }
    }

    private void remindNeedPoint() {
        switch (postion) {
        case DUIHUAN_1ZFB:
            needPoint = 1000;
            choujaingSize = 0;
            break;
        case DUIHUAN_5ZFB:
            needPoint = 5000;
            choujaingSize = 0;
            break;
        case DUIHUAN_10ZFB:
            needPoint = 10000;
            choujaingSize = 1;
            break;
        case DUIHUAN_20ZFB:
            needPoint = 20000;
            choujaingSize = 3;
            break;
        case DUIHUAN_50ZFB:
            needPoint = 50000;
            choujaingSize = 7;
            break;
        case DUIHUAN_100ZFB:
            needPoint = 100000;
            choujaingSize = 15;
            break;

        default:
            break;
        }
        if (userPoint < needPoint) {
            needPoinTextView.setText("需要金币: " + needPoint);
            subMitBtn.setEnabled(false);
            errorTextView.setVisibility(View.VISIBLE);

        } else {
            needPoinTextView.setText("需要金币: " + needPoint + "个");
            subMitBtn.setEnabled(true);
            errorTextView.setVisibility(View.GONE);
        }

    }

    private void addChouJiang(final int size) {

        RequestParams params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        params.put("cjSize", size + "");
        Request.updateChouJiangSize(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                MyToast.showCustomerToast("网络异常，增加抽奖次数失败");
            };

            public void onFinish(String url) {
            };

            public void onSuccess(int statusCode, JSONObject response) {
                try {
                    boolean isok = response.getBoolean("isok");
                    if (isok) {
                        Toast.makeText(DuiHuanZFBActivity.this, "恭喜您，增加了" + size + "次抽奖次数", Toast.LENGTH_SHORT).show();
                        updateUserChouJiang(size);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        });

    }

    private void updateUserChouJiang(int size) {
        UserBean userBean = MyData.getData().getUserBean();
        int cjSize = userBean.getChoujiang() + size;
        MyData.getData().getUserBean().setChoujiang(cjSize);
    }

}
