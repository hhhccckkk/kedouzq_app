package com.hck.zhuanqian.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.ShareUtil;
import com.hck.zhuanqian.view.CustomAlertDialog;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

public class ChouJiangActivity extends BaseActivity {
    private TextView choujiangTextView;
    private CustomAlertDialog dialog;
    private int postion2;
    // ����һ��ʱ�䳣�����˳������������ã�1.Բ����ͼ��ʾ�������м���л�ʱ�䣻2.ָ��תһȦ����Ҫ��ʱ�䣬������Ϊ500����
    private static final long ONE_WHEEL_TIME = 500;
    // ��¼Բ����ͼ�Ƿ���ʾ�Ĳ�������
    private boolean lightsOn = true;
    // ��ʼת��ʱ��ĽǶȣ���ʼֵΪ0
    private int startDegree = 0;

    private ImageView lightIv;
    private ImageView pointIv;
    // ָ��תȦȦ������Դ
    private int[] laps = { 10, 11, 13, 15, 17, 20, 22, 25, 30 };
    // ָ����ָ��ĽǶ�����Դ����Ϊ��6��ѡ����д˴���6��ֵ
    private int[] angles = { 0, 60, 120, 180, 240, 300 };
    // ת����������
    private String[] lotteryStr = { "��ϲ��ȡ���500��", "��ϲ��ȡ���50000��", "��ϲ��ȡ���1000��", "��ϲ��ȡ���100000��", "��ϲ��ȡ���2000��", "��ϲ��ȡ���100��", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoujiang);
        choujiangTextView = (TextView) findViewById(R.id.choujiangSize);
        initTitle("�齱����Ʒ");
        setupViews();
        flashLights();

        pointIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UserBean userBean = MyData.getData().getUserBean();
                if (userBean.getChoujiang() > 0) {
                    type = 0;
                    reduceChouJiangSize();

                } else {
                    Toast.makeText(ChouJiangActivity.this, "û�г齱���� ���������ֿ��Ի�ȡ����", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void updateView() {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean != null) {
            choujiangTextView.setText("�齱����: " + userBean.getChoujiang() + "��");
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void startChouJiang() {
        int lap = laps[(int) (Math.random() * 8)];
        startDegree = 0;
        int postion = (int) (Math.random() * 100);
        int angle = 0;
        postion2 = 2;
        postion2 = getJiangPingSizeByciShu(postion);
        angle = angles[postion2];
        // ÿ��תȦ�Ƕ�����
        int increaseDegree = lap * 360 + angle;
        // ��ʼ����ת������������ĸ������������������Լ������ĵ�ΪԲ��תȦ
        RotateAnimation rotateAnimation = new RotateAnimation(startDegree, startDegree + increaseDegree, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        // �����ĽǶȸ�ֵ��startDegree��Ϊ�´�תȦ�ĳ�ʼ�Ƕ�
        startDegree += increaseDegree;
        // ���㶯��������ʱ��
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        // ���ö�������ʱ��
        rotateAnimation.setDuration(time);
        // ���ö����������ͣ�������һ֡������
        rotateAnimation.setFillAfter(true);
        // ���ö����ļ�����Ϊ�����ȼ��ٺ����
        rotateAnimation.setInterpolator(ChouJiangActivity.this, android.R.anim.accelerate_decelerate_interpolator);
        // ���ö����ļ�����
        rotateAnimation.setAnimationListener(al);
        // ��ʼ���Ŷ���
        pointIv.startAnimation(rotateAnimation);
    }

    // ���߳���UI�߳�ͨ�ŵ�handler����
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                if (lightsOn) {
                    // ����lightIv���ɼ�
                    lightIv.setVisibility(View.INVISIBLE);
                    lightsOn = false;
                } else {
                    // ����lightIv�ɼ�
                    lightIv.setVisibility(View.VISIBLE);
                    lightsOn = true;
                }
                break;

            default:
                break;
            }
        };

    };

    // ��������״̬�ļ�����
    private AnimationListener al = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            String name = lotteryStr[postion2];
            Toast.makeText(ChouJiangActivity.this, name, Toast.LENGTH_SHORT).show();
            addPoint();
            updateView();
        }
    };

    private void addPoint() {
        String content = null;
        if (postion2 == 0) {
            savePoint(500);
            content = "�齱��ȡ���500��=0.5Ԫ";
            addCJInfo(content, 0);
        } else if (postion2 == 2) {
            savePoint(1000);
            content = "��Ʒ�����齱��ȡ���1000��=1Ԫ";
            addCJInfo(content, 1000);
        } else if (postion2 == 4) {
            savePoint(2000);
            content = "��Ʒ�������齱��ȡ���2000��=2Ԫ";
            addCJInfo(content, 2000);
        } else if (postion2 == 5) {
            savePoint(100);
            content = "�齱��ȡ���100��=0.1Ԫ";
            addCJInfo(content, 0);
        }

    }

    private void addCJInfo(String content, int point) {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean == null) {
            return;
        }
        params = new RequestParams();
        params.put("touxiang", userBean.getTouxiang());
        params.put("username", userBean.getName());
        params.put("uid", userBean.getId() + "");
        params.put("content", content);
        params.put("point", point + "");
        Request.addCJInfo(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable content, String data) {
                LogUtil.D("onFailure: " + data + content);
            };

            public void onSuccess(int statusCode, JSONObject response) {
                LogUtil.D("onSuccess: " + response.toString());
            };
        });

    }

    private void setupViews() {
        lightIv = (ImageView) findViewById(R.id.light);
        pointIv = (ImageView) findViewById(R.id.point);
    }

    // ���Ƶ�Ȧ�����ķ���
    private void flashLights() {

        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                // ��UI�̷߳�����Ϣ
                mHandler.sendEmptyMessage(0);

            }
        };

        // ÿ��ONE_WHEEL_TIME��������tt�����run����
        timer.schedule(tt, 0, ONE_WHEEL_TIME);
    }

    private int getJiangPingSizeByciShu(int postion) {
        if (1 <= postion && postion < 40) {
            return 0;
        } else if (40 < postion && postion < 70) {
            return 2;
        } else if (70 < postion && postion < 90) {
            return 5;
        } else if (90 < postion && postion < 100) {
            return 4;
        } else {
            return 0;
        }
    }

    private void reduceChouJiangSize() {
        Pdialog.showDialog(this, "���Եȣ�������", false);
        UserBean userBean = MyData.getData().getUserBean();
        RequestParams params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        params.put("cjSize", -1 + "");
        Request.updateChouJiangSize(params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("onFailure: " + error + content);
                MyToast.showCustomerToast("�����쳣 ������");
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D("onSuccess: " + response.toString());
                updateChouJiangSize();
                startChouJiang();
            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                Pdialog.hiddenDialog();
            }

        });
    }

    private void updateChouJiangSize() {
        UserBean userBean = MyData.getData().getUserBean();
        int cjSize = userBean.getChoujiang() - 1;
        cjSize = cjSize > 0 ? cjSize : 0;
        MyData.getData().getUserBean().setChoujiang(cjSize);
        updateView();
    }

    public void shareApp(View view) {
        startActivity(ChouJiangJiLuActivity.class);
    }

}
