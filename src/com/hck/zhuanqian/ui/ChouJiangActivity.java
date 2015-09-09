package com.hck.zhuanqian.ui;

import java.util.Timer;
import java.util.TimerTask;

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
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.LogUtil;
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
    private int[] laps = { 5, 7, 10, 15 };
    // ָ����ָ��ĽǶ�����Դ����Ϊ��6��ѡ����д˴���6��ֵ
    private int[] angles = { 0, 60, 120, 180, 240, 300 };
    // ת����������
    private String[] lotteryStr = { "��ϲ��ȡ���50��", "��ϲ��ȡ���150��", "лл����", "��ϲ��ȡ���200��", "��ϲ��ȡ1��Q��", "��ϲ��ȡ���100��", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoujiang);
        choujiangTextView = (TextView) findViewById(R.id.choujiangSize);
        updateView();
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
                    showDialog();
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

    public void showDialog() {
        if (isFinishing()) {
            return;
        }

        dialog = new CustomAlertDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("ȡ��");
        dialog.setRightText("ȷ��");
        dialog.setTitle("��ʾ");
        dialog.setMessage("û�г齱�����ˣ�������׬��Ҷһ�Q�һ���֧�����󣬷�����ѿ��Ի�ȡ�齱����");
        dialog.setOnRightListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getMoney();
            }
        });
        if (!isFinishing() && dialog != null) {
            dialog.show();
        }

    }

    private void getMoney() {
        Intent intent = new Intent();
        intent.setClass(ChouJiangActivity.this, KindActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void startChouJiang() {
        int lap = laps[(int) (Math.random() * 4)];
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
            Toast.makeText(ChouJiangActivity.this, name, Toast.LENGTH_LONG).show();
            addPoint();
            updateView();
        }
    };

    private void addPoint() {
        if (postion2 == 0) {
            savePoint(50);
        } else if (postion2 == 1) {
            savePoint(150);
        } else if (postion2 == 3) {
            savePoint(200);
        } else if (postion2 == 5) {
            savePoint(100);
        }
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

    private int getJiangPingSize(int postion) {
        if (0 <= postion && postion <= 5) {
            return 4;
        } else if (10 < postion && postion < 20) {
            return 1;
        } else if (50 < postion && postion <= 60) {
            return 3;
        } else if (60 < postion && postion <= 80) {
            return 5;
        } else if (80 < postion && postion <= 100) {
            return 0;
        } else {
            return 2;
        }
    }

    private int getJiangPingSizeByciShu(int postion) {
        if (0 <= postion && postion <= 5) {
            return 3;
        } else if (10 < postion && postion < 20) {
            return 2;
        } else if (50 < postion && postion <= 60) {
            return 0;
        } else if (60 < postion && postion <= 80) {
            return 5;
        } else if (80 < postion && postion <= 100) {
            return 0;
        } else {
            return 2;
        }
    }

    private void reducePoint() {

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

    public void getMoney(View view) {
        getMoney();
    }

}
