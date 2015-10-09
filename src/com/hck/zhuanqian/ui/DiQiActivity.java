package com.hck.zhuanqian.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.dyk.hfsdk.dao.util.DevListener;
import com.dyk.hfsdk.ui.Access;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;

public class DiQiActivity extends BaseActivity implements DevListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beiduo);
        initTitle("小七广告平台");
        initAD();
        getAdInitData();
        initDownSize(findViewById(R.id.down_size));
        setListener();
    }

    private void initAD() {
        try {
            Access.getInstance().init(this, Contans.KEY_DIQI_ID, "qq");
            Access.getInstance().setdefaultSCORE(this, 0);
        } catch (Exception e) {
        }

    }

    public void startGetMoney(View view) {
        try {
            Access.getInstance().openWALL(this);

        } catch (Exception e) {
        }

    }

    private void setListener() {
        Access.getInstance().setAppListener(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClose(Context arg0) {

    }

    @Override
    public void onDevFailed(String arg0) {
    }

    @Override
    public void onDevSucceed(int arg0) {
        if (arg0 > 0) {
            savePoint(Contans.AD_NAME_DIQI, arg0);
        }
    }
}
