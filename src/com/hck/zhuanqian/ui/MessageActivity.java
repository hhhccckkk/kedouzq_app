package com.hck.zhuanqian.ui;import org.json.JSONObject;import android.content.Intent;import android.os.Bundle;import android.view.KeyEvent;import android.view.LayoutInflater;import android.view.View;import android.view.View.OnClickListener;import android.widget.AdapterView;import android.widget.AdapterView.OnItemClickListener;import com.handmark.pulltorefresh.library.PullToRefreshListView;import com.hck.httpserver.JsonHttpResponseHandler;import com.hck.httpserver.RequestParams;import com.hck.kedouzq.R;import com.hck.zhuanqian.adapter.MessageAdpter;import com.hck.zhuanqian.bean.MessageBean;import com.hck.zhuanqian.data.Contans;import com.hck.zhuanqian.data.MessageData;import com.hck.zhuanqian.data.MyData;import com.hck.zhuanqian.net.Request;import com.hck.zhuanqian.util.JsonUtils;import com.hck.zhuanqian.util.LogUtil;import com.hck.zhuanqian.util.MyPreferences;import com.hck.zhuanqian.view.MyToast;import com.hck.zhuanqian.view.Pdialog;public class MessageActivity extends BaseActivity {    private PullToRefreshListView listView;    private MessageAdpter adpter;    private View errorView;    private MessageData msgData;    boolean hasMsg;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_message);        hasMsg = MyPreferences.getBoolean(Contans.HAS_MSG, false);        initTitle("我的消息");        initView();        getMsg();        setListener();    }    private void initView() {        listView = (PullToRefreshListView) findViewById(R.id.msgList);        errorView = LayoutInflater.from(this).inflate(R.layout.error_view, null);    }    private void getMsg() {        Pdialog.showDialog(this, "获取消息中", true);        params = new RequestParams();        params.put("id", MyData.getData().getUserBean().getId() + "");        Request.getMsg(params, new JsonHttpResponseHandler() {            @Override            public void onFailure(Throwable error, String content) {                super.onFailure(error, content);                MyToast.showCustomerToast("获取消息失败");                listView.setEmptyView(errorView);                LogUtil.D(content);            }            @Override            public void onSuccess(int statusCode, JSONObject response) {                super.onSuccess(statusCode, response);                LogUtil.D("onSuccess:" + response.toString());                try {                    isOK = response.getBoolean("isok");                    if (isOK) {                        msgData = JsonUtils.parse(response.toString(), MessageData.class);                        updateMsg();                    } else {                        if (adpter == null) {                            listView.setEmptyView(errorView);                        }                    }                } catch (Exception e) {                    MyToast.showCustomerToast("获取消息失败");                }            }            @Override            public void onFinish(String url) {                super.onFinish(url);                Pdialog.hiddenDialog();            }        });    }    private void updateMsg() {        if (msgData != null && msgData.getMsgBeans() != null) {            adpter = new MessageAdpter(this, msgData.getMsgBeans());            listView.setAdapter(adpter);        }    }    private void setListener() {        listView.setOnItemClickListener(new OnItemClickListener() {            @Override            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {                Intent intent = new Intent();                MessageBean messageBean = msgData.getMsgBeans().get(--arg2);                intent.putExtra("msg", messageBean);                intent.putExtra("pos", arg2);                intent.setClass(MessageActivity.this, ShowMsgXiangXiActivity.class);                startActivityForResult(intent, 1);            }        });        mTitleBar.getLeftBtn().setOnClickListener(new OnClickListener() {            @Override            public void onClick(View arg0) {                if (hasMsg) {                    startMainActivity();                }                finish();            }        });           }    @Override    protected void onActivityResult(int arg0, int arg1, Intent arg2) {        super.onActivityResult(arg0, arg1, arg2);        if (arg2 != null) {            int pos = arg2.getIntExtra("pos", 0);            adpter.updateMsg(pos);        }    }    @Override    public boolean onKeyDown(int keyCode, KeyEvent event) {        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复            if (hasMsg) {                startMainActivity();                finish();                return true;            }        }        return super.onKeyDown(keyCode, event);    }    private void startMainActivity() {        startActivity(new Intent(this, MainActivity.class));        finish();    }}