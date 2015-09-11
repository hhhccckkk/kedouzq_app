package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.ShareUtil;
import com.hck.zhuanqian.view.AlertDialogs;
import com.hck.zhuanqian.view.AlertDialogs.OneBtOnclick;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

public class DuiHuanQQActivity extends BaseActivity implements OnClickListener {
    private static final int DUIHUAN_1QBI = 0;
    private static final int DUIHUAN_5QBI = 1;
    private static final int DUIHUAN_10QBI = 2;
    private static final int DUIHUAN_20QBI = 3;
    private static final int DUIHUAN_50QBI = 4;
    private static final int DUIHUAN_100QBI = 5;
    private List<Button> buttons;
    private int postion;
    private TextView myJinBiTextView, needPoinTextView;
    private int money;
    private int needPoint;
    private int userPoint;
    private Button subMitBtn;
    private TextView errorTextView;
    private EditText qqEditText;
    private int qBiSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duihuan_qq);
        initTitle("兑换Q币");
        initView();
        initData();
        qBiSize = 1;
        updateUi();
    }

    private void initData() {
        userPoint = (int) (MyData.getData().getUserBean().getAllKeDouBi());
        money = userPoint / 1000;
        myJinBiTextView.setText(MyData.getData().getUserBean().getAllKeDouBi() + "个" + " 至少可兑换Q币" + money + "个");
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
        qqEditText = (EditText) findViewById(R.id.duihuan_input_qq);
        qqEditText.setText(MyData.getData().getUserBean().getQq());
        initData();
        subMitBtn.setEnabled(false);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.duihuan_1:
            postion = 0;
            qBiSize = 1;
            updateUi();
            break;
        case R.id.duihuan_5:
            postion = 1;
            qBiSize = 5;
            updateUi();
            break;
        case R.id.duihuan_10:
            postion = 2;
            qBiSize = 10;
            updateUi();
            break;
        case R.id.duihuan_20:
            postion = 3;
            qBiSize = 20;
            updateUi();
            break;
        case R.id.duihuan_50:
            postion = 4;
            qBiSize = 50;
            updateUi();
            break;
        case R.id.duihuan_100:
            postion = 5;
            qBiSize = 100;
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
        String qqString = qqEditText.getText().toString();
        if (TextUtils.isEmpty(qqString)) {
            MyToast.showCustomerToast("qq号码不能为空");
            return;
        }
        sendDataToServer(qqString);
    }

    private void sendDataToServer(String qq) {
        UserBean userBean = MyData.getData().getUserBean();
        if (qBiSize <= 0) {
            MyToast.showCustomerToast("兑换数量不能为0个");
            return;
        }
        Pdialog.showDialog(this, "正在下单 请稍等..", false);
        params = new RequestParams();
        params.put("id", userBean.getId() + "");
        params.put("money", needPoint + "");
        params.put("content", "qq号码：" + qq + "h兑换" + qBiSize + "个Q币");
        params.put("shangjia1", userBean.getShangjia1() + "");
        params.put("shangjia2", userBean.getShangjia2() + "");
        params.put("shangjia3", userBean.getShangjia3() + "");
        params.put("shangjia4", userBean.getShangjia4() + "");
        params.put("shangjia5", userBean.getShangjia5() + "");
        params.put("userName", userBean.getName());
        params.put("size", qBiSize + "");
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
                            AlertDialogs.alert(DuiHuanQQActivity.this, "失败", "金币不足，请到首页下拉更新用户信息", true,

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
        MyData.getData().getUserBean().setAllMoney(zhuanqian + qBiSize);
        initData();
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

    private void showSuccessDialog() {
        final String shareContent = "这个手机赚钱软件，很不错。刚兑换了" + qBiSize + "个Q币，希望大家一起来赚钱,安装就送红包";
        AlertDialogs.alert(this, "分享好友", "下单成功." + "分享好友可以获取抽奖次数,还可以发展下线" + "   (注意：分享到qq好友，分享后，请点击留在qq，然后再返回本app，不然获取不到抽奖次数,QQbug)", true,

        new OneBtOnclick() {

            @Override
            public void callBack(int tag) {
                ShareUtil.share(DuiHuanQQActivity.this, shareContent, handler);
            }
        }, 1);
    }

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
        case DUIHUAN_1QBI:
            needPoint = 1000;
            break;
        case DUIHUAN_5QBI:
            needPoint = (int) (5000 * 0.98);
            break;
        case DUIHUAN_10QBI:
            needPoint = (int) (10000 * 0.95);
            break;
        case DUIHUAN_20QBI:
            needPoint = (int) (20000 * 0.9);
            break;
        case DUIHUAN_50QBI:
            needPoint = (int) (50000 * 0.85);
            break;
        case DUIHUAN_100QBI:
            needPoint = (int) (100000 * 0.80);
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

}
