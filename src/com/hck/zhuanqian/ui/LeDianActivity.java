package com.hck.zhuanqian.ui;

import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;
import com.lerdian.startmanager.EarnPointSun;
import com.lerdian.startmanager.PointsManager;
import com.lerdian.startmanager.WallUser;

public class LeDianActivity extends BaseActivity {
    private static final int HUAFEI = 0;
    private static final int ZENGJIA = 1;
    private int money;
    private int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beiduo);
        initTitle("小点点赚钱专区");
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    public void startGetMoney(View view) {
        tag = 0;
        try {
            PointsManager.getInstance(this).openPoints(this);
        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPoint();

    }

    private void getPoint() {
        try {
            PointsManager.getInstance(this).queryPoints(this, mHandler);
        } catch (Exception e) {
        }

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
            case EarnPointSun.REQUEST_INTEGRAL_SUCCESS:
                WallUser mWallAppUser = (WallUser) msg.obj;
                if (mWallAppUser == null) {
                    return false;
                }
                int point = mWallAppUser.getBalance();
                money = 0;
                money = point;
                if (money > 0) {
                    huaFeiPoint(point);
                }
                break;
            }
            return false;
        }
    });

    private Handler huaFei = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tag++;
            switch (msg.what) {
            case EarnPointSun.REQUEST_INTEGRAL_SUCCESS:
                WallUser mWallAppUser = (WallUser) msg.obj;
                if (mWallAppUser == null) {
                    return false;
                }
                int point = mWallAppUser.getBalance();
                if (point > 0 && tag < 3) {
                    huaFeiPoint(point);
                    return false;
                }
                if (tag >= 3 && point > 0) {
                    MyToast.showCustomerToast("网络异常 获取金币失败");
                    tag = 0;
                    return false;
                }
                savePoint(Contans.AD_NAME_LEDIAN, money);
                break;
            }
            return false;
        }
    });

    private void huaFeiPoint(int point) {
        try {
            PointsManager.getInstance(this).consumptionPoints(this, point, huaFei);
        } catch (Exception e) {
        }

    }
}
