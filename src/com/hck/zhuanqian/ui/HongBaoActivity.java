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
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.adapter.HongBaoAdpter;
import com.hck.zhuanqian.adapter.HongBaoAdpter.CallBack;
import com.hck.zhuanqian.bean.Hongbao;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hongbao);
        initTitle("�ҵĺ��");
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
        nTextView.setText("����û�к�� �ƹ��û��������޻�ȡ���");
        nTextView.setTextColor(getResources().getColor(R.color.red));
    }

    private void getHongBao() {
        params = new RequestParams();
        params.put("page", page + "");
        Request.getHongBao(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                MyToast.showCustomerToast("��·�쳣 ��ȡ����ʧ��");
                listView.setEmptyView(errorView);
                nTextView.setText("����û�к�� �ƹ�app���ѻ�ȡ�������");
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
                        updateUI();
                    } else {
                        if (adpter == null) {
                            listView.setEmptyView(errorView);
                            return;
                        }
                        if (adpter.hongbaos.size() == size) {
                            MyToast.showCustomerToast("û��������");
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
            MyToast.showCustomerToast("���ڲ�����");
            return;
        }
        Pdialog.showDialog(this, "���ڲ�����...", true);
        updateHongbaoState(hongbao.getId(), hongbao.getPoint());

    }

    private void updateHongbaoState(long id, final int point) {
        params = new RequestParams();
        params.put("id", id + "");
        Request.updateHongBao(params, new JsonHttpResponseHandler() {
            public void onFailure(Throwable error, String content) {
                LogUtil.D("ʧ��: " + content + error);
                Pdialog.hiddenDialog();
            };

            public void onSuccess(int statusCode, JSONObject response) {
                savePoint(point);
            };

            public void onFinish(String url) {

            };
        });
    }

    private void savePoint(final int point) {
        LogUtil.D("isxi111111111111�� ");
        int isXiTong = hongbao.getIsXiTong();
        LogUtil.D("isxi�� "+isXiTong);
        boolean isTG = true;
        if (isXiTong == 1) {
            isTG = false;
        }
        savePoint(Contans.HONG_BAO, point, isTG, new JsonHttpResponseHandler() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                Pdialog.hiddenDialog();
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                updateUserPoint(point);
                addTgInfo(hongbao);
                adpter.update(post);
                post = 0;

            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                MyToast.showCustomerToast("��ȡ���ʧ�� ������������");
                adpter.update(post);
                post = 0;
            }
        });
    }

    public void updateUserPoint(long point) {
        try {
            long nowPoint = MyData.getData().getUserBean().getAllKeDouBi();
            nowPoint = nowPoint + point;
            MyData.getData().getUserBean().setAllKeDouBi(nowPoint);
            AlertDialogs.alert(this, "��ϲ", "�𿪺����ȡ��� " + point + "��", false);
        } catch (Exception e) {
            MyToast.showCustomerToast("�����쳣���ӽ��ʧ��");
        }
    }

    @Override
    public void callBack(int tag) {

    }

    private void addTgInfo(Hongbao hongbao) {
        params = new RequestParams();
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        params.put("name", hongbao.getuName());
        params.put("content", "�𿪺����ȡ�ƹ���: " + hongbao.getPoint() + "��");
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