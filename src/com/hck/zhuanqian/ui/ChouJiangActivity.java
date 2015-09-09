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
    // 设置一个时间常量，此常量有两个作用，1.圆灯视图显示与隐藏中间的切换时间；2.指针转一圈所需要的时间，现设置为500毫秒
    private static final long ONE_WHEEL_TIME = 500;
    // 记录圆灯视图是否显示的布尔常量
    private boolean lightsOn = true;
    // 开始转动时候的角度，初始值为0
    private int startDegree = 0;

    private ImageView lightIv;
    private ImageView pointIv;
    // 指针转圈圈数数据源
    private int[] laps = { 5, 7, 10, 15 };
    // 指针所指向的角度数据源，因为有6个选项，所有此处是6个值
    private int[] angles = { 0, 60, 120, 180, 240, 300 };
    // 转盘内容数组
    private String[] lotteryStr = { "恭喜获取金币50个", "恭喜获取金币150个", "谢谢参与", "恭喜获取金币200个", "恭喜获取1个Q币", "恭喜获取金币100个", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoujiang);
        choujiangTextView = (TextView) findViewById(R.id.choujiangSize);
        updateView();
        initTitle("抽奖看人品");
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
            choujiangTextView.setText("抽奖次数: " + userBean.getChoujiang() + "次");
        }
    }

    public void showDialog() {
        if (isFinishing()) {
            return;
        }

        dialog = new CustomAlertDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("取消");
        dialog.setRightText("确定");
        dialog.setTitle("提示");
        dialog.setMessage("没有抽奖次数了，做任务赚金币兑换Q币或者支付宝后，分享好友可以获取抽奖次数");
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
        // 每次转圈角度增量
        int increaseDegree = lap * 360 + angle;
        // 初始化旋转动画，后面的四个参数是用来设置以自己的中心点为圆心转圈
        RotateAnimation rotateAnimation = new RotateAnimation(startDegree, startDegree + increaseDegree, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        // 将最后的角度赋值给startDegree作为下次转圈的初始角度
        startDegree += increaseDegree;
        // 计算动画播放总时间
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        // 设置动画播放时间
        rotateAnimation.setDuration(time);
        // 设置动画播放完后，停留在最后一帧画面上
        rotateAnimation.setFillAfter(true);
        // 设置动画的加速行为，是先加速后减速
        rotateAnimation.setInterpolator(ChouJiangActivity.this, android.R.anim.accelerate_decelerate_interpolator);
        // 设置动画的监听器
        rotateAnimation.setAnimationListener(al);
        // 开始播放动画
        pointIv.startAnimation(rotateAnimation);
    }

    // 子线程与UI线程通信的handler对象
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                if (lightsOn) {
                    // 设置lightIv不可见
                    lightIv.setVisibility(View.INVISIBLE);
                    lightsOn = false;
                } else {
                    // 设置lightIv可见
                    lightIv.setVisibility(View.VISIBLE);
                    lightsOn = true;
                }
                break;

            default:
                break;
            }
        };

    };

    // 监听动画状态的监听器
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

    // 控制灯圈动画的方法
    private void flashLights() {

        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                // 向UI线程发送消息
                mHandler.sendEmptyMessage(0);

            }
        };

        // 每隔ONE_WHEEL_TIME毫秒运行tt对象的run方法
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
        Pdialog.showDialog(this, "请稍等，处理中", false);
        UserBean userBean = MyData.getData().getUserBean();
        RequestParams params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        params.put("cjSize", -1 + "");
        Request.updateChouJiangSize(params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("onFailure: " + error + content);
                MyToast.showCustomerToast("网络异常 请重试");
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
