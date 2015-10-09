package com.hck.zhuanqian.ui;

import java.util.List;

import org.json.JSONObject;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.adapter.CJAdpter;
import com.hck.zhuanqian.bean.ChouJiangBean;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.ChouJiangData;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ChouJiangJiLuActivity extends BaseActivity {
    private ListView listView;
    private TextView titleTextView;
    UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choujiang_jilu);
        initTitle("最新抽奖记录");
        initView();
        getCJInfo();
    }

    private void initView() {
        userBean = MyData.getData().getUserBean();
        listView = (ListView) findViewById(R.id.cj_list);
        titleTextView = (TextView) findViewById(R.id.cj_title);
        titleTextView.setText("我有抽奖次数: " + userBean.getChoujiang() + "次");
    }

    private void getCJInfo() {
        Pdialog.showDialog(this, "获取数据中...", false);

        if (userBean == null) {
            return;
        }
        params = new RequestParams();
        Request.getChouJiangInfo(params, new JsonHttpResponseHandler() {
            public void onFinish(String url) {
                Pdialog.hiddenDialog();
            };

            public void onFailure(Throwable error, String content) {
                LogUtil.D("onFailure: " + content + error);
                MyToast.showCustomerToast("网络异常获取数据失败");
            };

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D("onSuccess: " + response.toString());
                try {
                    isOK = response.getBoolean("isok");
                    if (isOK) {
                        ChouJiangData data = JsonUtils.parse(response.toString(), ChouJiangData.class);
                        updateUI(data.getBeans());
                    }
                    else {
                        MyToast.showCustomerToast("没有获取到数据"); 
                    }
                } catch (Exception e) {
                    LogUtil.D("cjcjccj: " + e.toString());
                }

            }
        });
    }

    private void updateUI(List<ChouJiangBean> beans) {
        CJAdpter adpter = new CJAdpter(beans, this);
        listView.setAdapter(adpter);
    }
}
