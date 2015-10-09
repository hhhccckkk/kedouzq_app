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
import android.widget.Toast;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
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
    private UserBean userBean;
    private int choujaingSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duihuan_qq);
        userBean = MyData.getData().getUserBean();
        initTitle("�һ�Q��");
        initView();
        initData();
        qBiSize = 1;
        updateUi();
    }

    private void initData() {
        userPoint = (int) (userBean.getAllKeDouBi());
        money = userPoint / 1000;
        myJinBiTextView.setText(userBean.getAllKeDouBi() + "��" + " �ɶһ�Q��" + money + "��");
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
        qqEditText.setText(userBean.getQq());
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
            MyToast.showCustomerToast("qq���벻��Ϊ��");
            return;
        }
        sendDataToServer(qqString);
    }

    private void sendDataToServer(String qq) {
        UserBean userBean = MyData.getData().getUserBean();
        if (qBiSize <= 0) {
            MyToast.showCustomerToast("�һ���������Ϊ0��");
            return;
        }
        Pdialog.showDialog(this, "�����µ� ���Ե�..", false);
        params = new RequestParams();
        params.put("id", userBean.getId() + "");
        params.put("money", needPoint + "");
        params.put("content", "�ɹ��һ�" + qBiSize + "��Q��");
        params.put("size", qBiSize + "");
        params.put("info","qq����: "+qq+"�һ� "+qBiSize+"��Q��");
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
                            AlertDialogs.alert(DuiHuanQQActivity.this, "ʧ��", "��Ҳ��㣬�뵽��ҳ���������û���Ϣ", true,

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
                            }

                            choujaingSize = 0;
                            remindNeedPoint();
                            showSuccessDialog();
                        }

                    } else {
                        MyToast.showCustomerToast("�����쳣 ����ʧ��");
                    }
                } catch (Exception e) {
                    MyToast.showCustomerToast("�����쳣 ����ʧ��");
                }

            };

            public void onFailure(Throwable error, String content) {
                LogUtil.D(content + error);
                MyToast.showCustomerToast("�����쳣 ����ʧ��");
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
                MyToast.showCustomerToast("����ʧ��");
            } else if (msg.what == 1) {
                MyToast.showCustomerToast("����ɹ�");

            } else if (msg.what == 2) {
                MyToast.showCustomerToast("����ȡ��");
            }
        };
    };

    private void showSuccessDialog() {
        final String shareContent = "����ֻ�׬Ǯ������ܲ����նһ���" + qBiSize + "��Q�ң�ϣ�����һ����׬Ǯ,��װ���ͺ��";
        AlertDialogs.alert(this, "�������", "�µ��ɹ�." + "����������������һ������ɡ����ĺ���ͨ�������������ر�app��װ�򿪼��������0.3-0.8Ԫ�ĺ������һֱ����8������׬Ǯ10%����", true,

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
            choujaingSize = 0;
            break;
        case DUIHUAN_5QBI:
            needPoint = 5000;
            choujaingSize = 0;
            break;
        case DUIHUAN_10QBI:
            needPoint = 10000;
            choujaingSize = 1;
            break;
        case DUIHUAN_20QBI:
            needPoint = 20000;
            choujaingSize = 3;
            break;
        case DUIHUAN_50QBI:
            needPoint = 50000;
            choujaingSize = 7;
            break;
        case DUIHUAN_100QBI:
            needPoint = 100000;
            choujaingSize = 15;
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

    private void addChouJiang(final int size) {

        RequestParams params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        params.put("cjSize", size + "");
        Request.updateChouJiangSize(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                MyToast.showCustomerToast("�����쳣�����ӳ齱����ʧ��");
            };

            public void onFinish(String url) {
            };

            public void onSuccess(int statusCode, JSONObject response) {
                try {
                    boolean isok = response.getBoolean("isok");
                    if (isok) {
                        Toast.makeText(DuiHuanQQActivity.this, "��ϲ����������" + size + "�γ齱����", Toast.LENGTH_SHORT).show();
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
