package com.hck.zhuanqian.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.view.MyToast;

public class WanPuActivity extends BaseActivity {
    int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanpu);
        initTitle("小万赚钱区");
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    public void startGetMoney(View view) {
        try {
            AppConnect.getInstance(Contans.KEY_WANPU, "qq", this);
            AppConnect.getInstance(this).showOffers(this);
        } catch (Exception e) {
            MyToast.showCustomerToast("初始化数据失败");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getPoint();
        } catch (Exception e) {
        }

    }

    private void getPoint() {
        AppConnect.getInstance(this).getPoints(new UpdatePointsNotifier() {

            @Override
            public void getUpdatePointsFailed(String arg0) {

            }

            @Override
            public void getUpdatePoints(String arg0, int arg1) {
                if (arg1 > 0) {
                    point = arg1;
                    Message message = new Message();
                    message.arg1 = arg1;
                    message.what = HUAFEI;
                    handler.sendMessage(message);
                }
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == HUAFEI) {
                huafei(msg.arg1);
            } else if (msg.what == HUAFEI_SUCCESS) {
                if (point > 0) {
                    savePoint(Contans.AD_NMAE_WANPU, point);
                    point = 0;
                }

            }
        };
    };

    private void huafei(int point) {
        AppConnect.getInstance(this).spendPoints(point, new UpdatePointsNotifier() {

            @Override
            public void getUpdatePointsFailed(String arg0) {

            }

            @Override
            public void getUpdatePoints(String arg0, int arg1) {
                Message message =new Message();
                message.what=HUAFEI_SUCCESS;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AppConnect.getInstance(this).close();
        } catch (Exception e) {
        }
       
    }

}
