package com.hck.zhuanqian.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.adapter.TGUserAdpter;
import com.hck.zhuanqian.adapter.TGUserAdpter.SendMsgCallBack;
import com.hck.zhuanqian.bean.TGUserBean;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.data.TGUserData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;

public class ShowTgUserActivity extends BaseActivity implements SendMsgCallBack {
    private PullToRefreshListView listView;
    private int page = 1;
    private View view;
    private TextView titleTextView;
    private TGUserAdpter adpter;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tg_user);
        initTitle("我的徒弟");
        initView();
        getTgSizeData();
        getTgUserData();
        setListener();

    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.tg_listview);
        listView.setMode(Mode.BOTH);
        view = findViewById(R.id.pb);
        titleTextView = (TextView) findViewById(R.id.tg_user_title);

    }

    private void getTgSizeData() {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean == null) {
            return;
        }
        params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        Request.getTgUserSize(params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("onFailure: " + content + error);
            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                try {
                    boolean isOK = response.getBoolean("isok");
                    if (isOK) {
                        JSONObject jsonObject = response.getJSONObject("size");
                        int allSize = jsonObject.getInt("totalSize");
                        int size = jsonObject.getInt("size1");
                        updateView(size, allSize);
                    }
                } catch (Exception e) {
                }

            }
        });
    }

    private void getTgUserData() {
        UserBean userBean = MyData.getData().getUserBean();
        if (userBean == null) {
            return;
        }
        params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        params.put("page", page + "");
        Request.getTgUser(params, new JsonHttpResponseHandler() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                if (listView != null) {
                    listView.onRefreshComplete();
                }

            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);

                try {
                    boolean isOK = response.getBoolean("isok");
                    if (isOK) {
                        TGUserData userBeans = new TGUserData();
                        JsonUtils.getTgUser(response.getString("users"), userBeans);
                        updateUI(userBeans);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }

    private void setListener() {
        listView.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                isUpdate = true;
                page = 1;
                getTgUserData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                page++;
                getTgUserData();
            }
        });
    }

    private void updateView(int size1, int allSize) {
        titleTextView.setText("一级徒弟: " + size1 + "个   全部8级徒弟: " + allSize + "个");
    }

    private void updateUI(TGUserData tgUserData) {
        if (isUpdate) {
            adpter = null;
        }
        if (adpter == null) {
            adpter = new TGUserAdpter(tgUserData, this);
            listView.setAdapter(adpter);
        } else {
            adpter.updateData(tgUserData.getTgUserDatas());
        }
        isUpdate = false;

    }

    @Override
    public void sendMsg(TGUserBean userBean) {
        Intent intent = new Intent();
        intent.putExtra("uid", userBean.getUserId());
        intent.setClass(this, SendMsgActivity.class);
        intent.putExtra("userName", userBean.getUserName());
        startActivity(intent);
    }
   
}
