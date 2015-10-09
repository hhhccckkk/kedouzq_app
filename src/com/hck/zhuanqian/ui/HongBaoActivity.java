package com.hck.zhuanqian.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.adapter.HongBaoAdpter;
import com.hck.zhuanqian.adapter.HongBaoAdpter.CallBack;
import com.hck.zhuanqian.bean.Hongbao;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.HongBaoData;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.AlertDialogs;
import com.hck.zhuanqian.view.AlertDialogs.OneBtOnclick;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;

public class HongBaoActivity extends BaseActivity implements CallBack, OneBtOnclick {
    private PullToRefreshListView listView;
    private int page = 1;
    private View view;
    private List<Hongbao> hongbaos = new ArrayList<>();
    private HongBaoData hData = new HongBaoData();
    private View errorView;
    private HongBaoAdpter adpter;
    private boolean isUpdate;
    private TextView nTextView;
    private boolean isIng;
    private int post;
    private Hongbao hongbao;
    boolean isxt = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hongbao);
        initTitle("我的红包");
        type = Contans.TYPE_HONGBAO;
        maxNum = Contans.TYPE_HONGBAO_MAX_SIZE;
        initView();
        getHongBao();
        setListener();
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.hongbaoList);
        listView.setMode(Mode.BOTH);
        view = findViewById(R.id.loading);
        errorView = LayoutInflater.from(this).inflate(R.layout.error_view, null);
        nTextView = (TextView) errorView.findViewById(R.id.error_text);
        nTextView.setText("您还没有红包 推广用户可以无限获取红包");

    }

    private void getHongBao() {
        params = new RequestParams();
        params.put("page", page + "");
        Request.getHongBao(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                if (adpter == null) {
                    listView.setEmptyView(errorView);
                    nTextView.setText("您还没有红包 推广app而已获取大量红包");
                } else {
                    MyToast.showCustomerToast("网络异常获取数据失败");
                }
            };

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D(response.toString());
                try {
                    int size = response.getInt("size");
                    isOK = response.getBoolean("isok");
                    if (isOK) {
                        hData = JsonUtils.parse(response.toString(), HongBaoData.class);
                        hongbaos = hData.getHongbao();
                        initTitle("我的红包: " + size + "个");
                        updateUI();
                    } else {
                        if (adpter == null) {
                            listView.setEmptyView(errorView);
                            return;
                        }
                        if (adpter.hongbaos.size() == size) {
                            MyToast.showCustomerToast("没有数据了");
                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                view.setVisibility(View.GONE);
                listView.onRefreshComplete();
            }
        });
    }

    private void setListener() {
        listView.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                isUpdate = true;
                page = 1;
                getHongBao();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                page++;
                getHongBao();
            }
        });
    }

    private void updateUI() {
        if (isUpdate) {
            adpter = null;
        }
        if (adpter == null) {
            adpter = new HongBaoAdpter(hongbaos, this);
            listView.setAdapter(adpter);
        } else {
            adpter.update(hongbaos);
        }
        isUpdate = false;
    }

    @Override
    public void daKai(Hongbao hongbao, int post) {
        this.post = post;
        this.hongbao = hongbao;
        if (isIng) {
            MyToast.showCustomerToast("正在拆红包中");
            return;
        }
        Pdialog.showDialog(this, "正在拆红包中...", true);
        updateHongbaoState(hongbao.getId(), hongbao.getPoint());

    }

    private void updateHongbaoState(long id, final int point) {
        params = new RequestParams();
        params.put("id", id + "");
        Request.updateHongBao(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                LogUtil.D("失败: " + content + error);

            };

            public void onSuccess(int statusCode, JSONObject response) {
                savePointHongBao(point);
            };

            public void onFinish(String url) {
                Pdialog.hiddenDialog();
            };
        });
    }

    private void savePointHongBao(final int point) {
      
        if (hongbao!=null&&hongbao.getIsXiTong() == 1) {
            isxt = true;
        }
        LogUtil.D("savePointHongBao: "+hongbao.getIsXiTong() +": "+isxt);
        savePoint(Contans.HONG_BAO, point, isxt, new JsonHttpResponseHandler() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                Pdialog.hiddenDialog();
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                updateUserPoint(point,isxt);
                if(!isxt){
                    addTgInfo(hongbao);
                }
               
                adpter.update(post);
                post = 0;

            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                MyToast.showCustomerToast("获取金币失败 请检查您的网络");
                adpter.update(post);
                post = 0;
            }
        });
    }

    public void updateUserPoint(long point,boolean isXT) {
        try {
            UserBean userBean = MyData.getData().getUserBean();
            long nowPoint = userBean.getAllKeDouBi();
            nowPoint = nowPoint + point;
            long tgMoney = userBean.getTGMoney();
            userBean.setAllKeDouBi(nowPoint);
            if (!isXT) {
                userBean.setTGMoney(tgMoney + point);
            }
            MyData.getData().setUserBean(userBean);
            AlertDialogs.alert(this, "我知道了", "获取金币" + point + "个,1000金币即可提现，赚钱区 做任务可以无限获取金币", false);
        } catch (Exception e) {
            MyToast.showCustomerToast("网络异常增加金币失败");
        }
    }

    @Override
    public void callBack(int tag) {

    }

    private void addTgInfo(Hongbao hongbao) {
        params = new RequestParams();
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        params.put("name", hongbao.getuName());
        params.put("content", "用户" + hongbao.getuName() + "安装您的推广包，您获取金币: " + hongbao.getPoint() + "个");
        Request.addTGInfo(params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("addTgInfo onFailure: " + content + error);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D("addTgInfo onSuccess: " + response.toString());
            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                LogUtil.D("addTgInfo onFinish: " + url);
            }
        });
    }

}
