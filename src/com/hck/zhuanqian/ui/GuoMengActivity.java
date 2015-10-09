package com.hck.zhuanqian.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cn.guomob.android.intwal.GMScoreCallBack;
import cn.guomob.android.intwal.GMUtils;
import cn.guomob.android.intwal.OpenIntegralWall;
import cn.guomob.android.intwal.ReturnAdMsg;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;

public class GuoMengActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guomeng);
        initTitle("小果专区");
        initAD();
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    public void startGetMoney(View view) {
        OpenIntegralWall.getInstance().show();
    }

    private void initAD() {
        OpenIntegralWall.setGbKeyCode(this, Contans.GUOMENG_KEY);
        OpenIntegralWall.initInterfaceType(this, new GMScoreCallBack() {

            @Override
            public void onSuccess(Context arg0, String arg1) {
                try {
                    ArrayList<ReturnAdMsg> adMsgList = GMUtils.GetAdList(arg1);
                    for (int i = 0; i < adMsgList.size(); i++) {
                        ReturnAdMsg returnScoreMsg = adMsgList.get(i);
                        int point=Integer.parseInt(returnScoreMsg.getOrder());
                        savePoint(Contans.AD_NMAE_GUOMENG, point);
                    }
                } catch (Exception e) {
                    MyToast.showCustomerToast("获取金币失败 请检查您的网络");
                }
               
                
            }

        });
    }
}
