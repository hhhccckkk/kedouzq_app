package com.hck.zhuanqian.ui;

import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class SendMsgActivity extends BaseActivity {
    private long uid;
    private String content;
    private EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        initTitle("给徒弟发信息");
        uid = getIntent().getLongExtra("uid", 0l);
        contentEditText = (EditText) findViewById(R.id.msg_content);
    }

    public void submit(View view) {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean == null) {
            return;
        }
        if (!isDataOk()) {
            MyToast.showCustomerToast("消息不能为空");
            return;
        }
        params = new RequestParams();
        params.put("uid", uid + "");
        params.put("userName", userBean.getName());
        params.put("content", content);
        Pdialog.showDialog(this, "消息发送中", true);
        Request.addMsg(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                MyToast.showCustomerToast("消息发送失败请检查您的网络");
            };

            public void onFinish(String url) {
                Pdialog.hiddenDialog();
            };

            public void onSuccess(int statusCode, org.json.JSONObject response) {
                try {
                    boolean isOK = response.getBoolean("isok");
                    if (isOK) {
                        contentEditText.setText("");
                        MyToast.showCustomerToast("消息发送成功");
                    } else {
                        MyToast.showCustomerToast("消息发送失败");
                    }
                } catch (Exception e) {
                }
            };
        });
    }

    private boolean isDataOk() {
        content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        return true;
    }

}
