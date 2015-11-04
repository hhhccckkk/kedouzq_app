package com.hck.zhuanqian.ui;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyPreferences;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MoreActivity extends BaseActivity implements OnClickListener {
    ImageView msgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initTitle("¸ü¶à");
        initView();
    }

    private void initView() {
        findViewById(R.id.more_about).setOnClickListener(this);
        findViewById(R.id.more_user).setOnClickListener(this);
        findViewById(R.id.more_fankui).setOnClickListener(this);
        findViewById(R.id.more_msg).setOnClickListener(this);
        findViewById(R.id.more_haoping).setOnClickListener(this);
        findViewById(R.id.more_about_ra).setOnClickListener(this);

        msgView = (ImageView) findViewById(R.id.showMsg);
        boolean isShowMsg = MyPreferences.getBoolean("isShowMsg", false);
        if (isShowMsg) {
            showMsgTiShi(View.VISIBLE);
        }
        boolean hasMsg = MyPreferences.getBoolean(Contans.HAS_MSG, false);
        if (hasMsg) {
            showMsgTiShi(View.VISIBLE);
            MyPreferences.saveBoolean(Contans.HAS_MSG, false);

        } else {
            showMsgTiShi(View.GONE);
        }
    }

    private void showMsgTiShi(int isShow) {
        msgView.setVisibility(isShow);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.more_user:
            startActivity(UpdateUserInfoActivity.class);
            break;
        case R.id.more_msg:
            showMsgTiShi(View.GONE);
            startActivity(MessageActivity.class);
            break;
        case R.id.more_about:
            startActivity(HelpActivity.class);
            break;
        case R.id.more_fankui:
            startActivity(FanKuiActivity.class);
            break;
        case R.id.more_haoping:
            startPinLunActivity();
            break;
        case R.id.more_about_ra:
            startActivity(HelpActivity.class);
        default:
            break;
        }
    }

    public void startPinLunActivity() {
        try {
            Uri uri = Uri.parse("market://details?id=" + "com.hck.kedouzhuanqian");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
        }

    }
}
