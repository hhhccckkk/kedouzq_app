package com.hck.zhuanqian.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.bb.dd.BeiduoPlatform;
import com.bb.dd.listener.IActiveListener;
import com.bb.dd.listener.IGetListener;
import com.bb.dd.listener.IReduceListener;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;

/**
 * 
 * @author
 * 
 */
public class BeiDuoActivity extends BaseActivity {
    private int point;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAd();
        setContentView(R.layout.activity_beiduo);
        initTitle("Ð¡±´×¨Çø");
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    private void initAd() {
        try {
            BeiduoPlatform.setAppId(this, Contans.BEI_ZUO_ID, Contans.BEI_ZUO_KEY);
            BeiduoPlatform.setActiveAppListener(new IActiveListener() {

                @Override
                public void activeSuccess(int arg0, int arg1, String arg2) {
                    point = 0;
                    point = arg1;
                    if (point > 0) {
                        savePoint(Contans.AD_NAME_BEIDUO, point);
                    }
                    handler.sendEmptyMessage(point);

                }

                @Override
                public void activeFailed(int arg0, int arg1, String arg2) {
                }
            });

        } catch (Exception e) {
        }

    }

    public void startGetMoney(View view) {
        isFirst = false;
        try {
            BeiduoPlatform.showOfferWall(this);
        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isFirst) {
                return;
            }
            BeiduoPlatform.getMoney(new IGetListener() {

                @Override
                public void getSuccess(int arg0, String arg1) {
                    if (arg0 > 0) {
                        handler.sendEmptyMessage(arg0);
                    }
                }

                @Override
                public void getFailed(int arg0) {
                }
            });
        } catch (Exception e) {
            showErrorToast();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            huafei(msg.what);
        };
    };

    private void huafei(final int point) {
        try {
            BeiduoPlatform.reduceMoney(point, new IReduceListener() {

                @Override
                public void reduceSuccess(int arg0) {
                }

                @Override
                public void reduceFailed(int arg0) {
                }
            });
        } catch (Exception e) {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
