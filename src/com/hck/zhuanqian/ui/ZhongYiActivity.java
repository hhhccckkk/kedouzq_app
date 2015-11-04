package com.hck.zhuanqian.ui;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.impl.Indenter;

import android.os.Bundle;
import android.view.View;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;
import com.zy.phone.SDKInit;
import com.zy.phone.net.Integral;

public class ZhongYiActivity extends BaseActivity implements Integral {
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zy);
        initTitle("小亿专区");
        initAd();
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
    }

    private void initAd() {
        try {
            SDKInit.initAd(this, Contans.ZHONG_YI_KEY, null);
        } catch (Exception e) {
            showErrorToast();
        }

    }

    public void showAd(View view) {
        try {
            isFirst = false;
            SDKInit.initAdList(this);
            SDKInit.checkIntegral(this);
        } catch (Exception e) {
            showErrorToast();
        }
    }

    @Override
    public void retAddIntegral(String arg0, String arg1) {
    }

    @Override
    public void retCheckIntegral(String arg0, String arg1) {
        try {

            if (arg0.equals("0")&& !arg1.equals("0")) {
                
                if (isFirst) {
                    SDKInit.minusIntegral(this, arg1);
                } else {
                    savePoint(Contans.AD_NMAE_ZHONYI, Integer.parseInt(arg1));
                    SDKInit.minusIntegral(this, arg1);
                }
            }
        } catch (Exception e) {
            MyToast.showCustomerToast("增加金币失败 请检查您的网络");
        }

    }

    @Override
    public void retMinusIntegral(String arg0, String arg1) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKInit.checkIntegral(this);
    }

}
