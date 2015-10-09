package com.hck.zhuanqian.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;
import com.jd.callback.JdAddScoreListener;
import com.jd.integral.JdAdManager;

public class JinDaiActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jindai);
        initTitle("小金专区");
        initAD();
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    public void startGetMoney(View view) {
        try {
            JdAdManager.showAdOffers(this);
        } catch (Exception e) {
        }

    }

    private void initAD() {
        try {
            JdAdManager.setAddScoreListener(this, new JdAddScoreListener() {

                @Override
                public void jdAddScoreSucceed(int arg0, int arg1, String arg2, String arg3, String arg4) {
                    LogUtil.D("金袋 增加金币成功: " + arg0 + ":" + arg1 + ":" + arg2 + ":" + arg3 + ":" + arg4);
                    if (arg1 > 0) {
                        handler.sendEmptyMessage(arg1);
                    }

                }

                @Override
                public void jdAddScoreFaild(String arg0, String arg1, String arg2) {

                }
            });
        } catch (Exception e) {

        }

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            savePoint(Contans.AD_NAME_JINGDAI, msg.what);
            MyToast.showCustomerToast("任务完成，获取金币: " + msg.what + "个");
        };
    };
}
