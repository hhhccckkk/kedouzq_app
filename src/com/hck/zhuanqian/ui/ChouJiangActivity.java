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
    // 设置一个时间常量，此常量有两个作用，1.圆灯视图显示与隐藏中间的切换时间；2.指针转一圈所需要的时间，现设置为500毫秒
    private static final long ONE_WHEEL_TIME = 500;
    // 记录圆灯视图是否显示的布尔常量
    private boolean lightsOn = true;
    // 开始转动时候的角度，初始值为0
    private int startDegree = 0;

    private ImageView lightIv;
    private ImageView pointIv;
    // 指针转圈圈数数据源
    private int[] laps = { 10, 11, 13, 15, 17, 20, 22, 25, 30 };
    // 指针所指向的角度数据源，因为有6个选项，所有此处是6个值
    private int[] angles = { 0, 60, 120, 180, 240, 300 };
    // 转盘内容数组
    private String[] lotteryStr = { "恭喜获取金币500个", "恭喜获取金币50000个", "恭喜获取金币1000个", "恭喜获取金币100000个", "恭喜获取金币2000个", "恭喜获取金币100个", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoujiang);
        choujiangTextView = (TextView) findViewById(R.id.choujiangSize);
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
                    Toast.makeText(ChouJiangActivity.this, "没有抽奖次数 做任务提现可以获取次数", Toast.LENGTH_LONG).show();
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
            Toast.makeText(ChouJiangActivity.this, name, Toast.LENGTH_SHORT).show();
            addPoint();
            updateView();
        }
    };

    private void addPoint() {
        String content = null;
        if (postion2 == 0) {
            savePoint(500);
            content = "抽奖获取金币500个=0.5元";
            addCJInfo(content, 0);
        } else if (postion2 == 2) {
            savePoint(1000);
            content = "人品不错，抽奖获取金币1000个=1元";
            addCJInfo(content, 1000);
        } else if (postion2 == 4) {
            savePoint(2000);
            content = "人品爆发，抽奖获取金币2000个=2元";
            addCJInfo(content, 2000);
        } else if (postion2 == 5) {
            savePoint(100);
            content = "抽奖获取金币100个=0.1元";
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

    public void shareApp(View view) {
        startActivity(ChouJiangJiLuActivity.class);
    }

}
