package com.hck.zhuanqian.ui;

import android.os.Bundle;
import android.view.View;

import com.chuannuo.tangguo.TangGuoWall;
import com.chuannuo.tangguo.listener.TangGuoWallListener;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;

public class TangGuoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beiduo);
        initTitle("小糖赚钱专区");
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    public void startGetMoney(View view) {
        try {
            TangGuoWall.initWall(this, "");
            TangGuoWall.setTangGuoWallListener(new TangGuoWallListener() {

                @Override
                public void onSign(int arg0, String arg1, int arg2) {
                    LogUtil.D("onSign: "+arg0+":"+arg1+":"+arg2);
                    if (arg0==1 && arg2>0) {
                        savePoint(Contans.AD_NMAE_GUOMENG, arg2);
                    }
                }
                @Override
                public void onAddPoint(int arg0, String arg1, int arg2) {
                    LogUtil.D("onAddPoint: "+arg0+":"+arg1+":"+arg2);
                    if (arg0==1 && arg2>0) {
                        savePoint(Contans.AD_NMAE_GUOMENG, arg2);
                    }
                }
            });
            TangGuoWall.show();
        } catch (Exception e) {
            MyToast.showCustomerToast("打开广告失败");
        }

    }

}
