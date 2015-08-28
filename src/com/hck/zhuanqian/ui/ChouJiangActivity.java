package com.hck.zhuanqian.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.hck.zhuanqian.R;

public class ChouJiangActivity extends BaseActivity {
	// ����һ��ʱ�䳣�����˳������������ã�1.Բ����ͼ��ʾ�������м���л�ʱ�䣻2.ָ��תһȦ����Ҫ��ʱ�䣬������Ϊ500����
	private static final long ONE_WHEEL_TIME = 500;
	// ��¼Բ����ͼ�Ƿ���ʾ�Ĳ�������
	private boolean lightsOn = true;
	// ��ʼת��ʱ��ĽǶȣ���ʼֵΪ0
	private int startDegree = 0;

	private ImageView lightIv;
	private ImageView pointIv;
	private ImageView wheelIv;

	// ָ��תȦȦ������Դ
	private int[] laps = { 5, 7, 10, 15 };
	// ָ����ָ��ĽǶ�����Դ����Ϊ��6��ѡ����д˴���6��ֵ
	private int[] angles = { 0, 60, 120, 180, 240, 300 };
	// ת����������
	private String[] lotteryStr = { "����PSP", "10Ԫ���", "лл����", "DNFǮ��",
			"OPPO MP3", "5Ԫ���", };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoujiang);
		initTitle("�齱����Ʒ");
		setupViews();
		flashLights();

		pointIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int lap = laps[(int) (Math.random() * 4)];
				int angle = angles[(int) (Math.random() * 6)];
				// ÿ��תȦ�Ƕ�����
				int increaseDegree = lap * 360 + angle;
				// ��ʼ����ת������������ĸ������������������Լ������ĵ�ΪԲ��תȦ
				RotateAnimation rotateAnimation = new RotateAnimation(
						startDegree, startDegree + increaseDegree,
						RotateAnimation.RELATIVE_TO_SELF, 0.5f,
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
				rotateAnimation.setInterpolator(ChouJiangActivity.this,
						android.R.anim.accelerate_decelerate_interpolator);
				// ���ö����ļ�����
				rotateAnimation.setAnimationListener(al);
				// ��ʼ���Ŷ���
				pointIv.startAnimation(rotateAnimation);
			}
		});

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
			String name = lotteryStr[startDegree % 360 / 60];
			Toast.makeText(ChouJiangActivity.this, name, Toast.LENGTH_LONG)
					.show();
		}
	};

	private void setupViews() {
		lightIv = (ImageView) findViewById(R.id.light);
		pointIv = (ImageView) findViewById(R.id.point);
		wheelIv = (ImageView) findViewById(R.id.main_wheel);
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

}
