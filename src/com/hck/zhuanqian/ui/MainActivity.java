package com.hck.zhuanqian.ui;

/**
 * ������.
 */
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyPreferences;
import com.hck.zhuanqian.util.MyTools;
import com.hck.zhuanqian.view.CustomAlertDialog;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.RemindView;

public class MainActivity extends Activity implements OnClickListener {
    private TextView mUserIdTV, mUserJinbiTv, mUserNameTv, mTGSize;
    private Button mAllMoneyBtn, mAllTgUserBtn;
    private PullToRefreshListView listView;
    private TextView hongBaoSize;
    private RemindView badge1;
    private LinearLayout msgTishiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyPreferences(this);
        setContentView(R.layout.activity_home);
        if (MyData.myData == null || MyData.myData.getUserBean() == null) {
            finish();

        }
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        initView(view);
        setStartLabel();
        getHongBaoAndMsgSize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserData();
    }

    private void remind(RemindView remindView, int size) {
        remindView.setText(size + ""); // ��Ҫ��ʾ����������
        remindView.setBadgePosition(RemindView.POSITION_TOP_RIGHT);// ��ʾ��λ��.���Ͻ�,BadgeView.POSITION_BOTTOM_LEFT,���󣬻���������������
        remindView.setTextColor(Color.WHITE); // �ı���ɫ
        remindView.setBadgeBackgroundColor(Color.RED); // ������Ϣ�ı�����ɫ���Լ�����
        remindView.setTextSize(12); // �ı���С
        remindView.setBadgeMargin(5); // ���߼��
        remindView.show();// ֻ����ʾ
    }

    private void remindHB(int size) {
        badge1 = new RemindView(this, hongBaoSize);
        remind(badge1, size);
    }

    private void remindMsg(int isShow) {
        msgTishiLayout.setVisibility(isShow);
    }

    private void getHongBaoAndMsgSize() {
        Request.getSize(new JsonHttpResponseHandler() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                try {
                    long msgId = response.getLong("msgId");
                    long oldMsgId = MyPreferences.getLong("msgId", 0l);
                    if (oldMsgId < msgId) {
                        remindMsg(View.VISIBLE);
                        MyPreferences.saveBoolean("isShowMsg", true);
                    } else {
                        MyPreferences.saveBoolean("isShowMsg", false);
                    }
                    MyPreferences.saveLong("msgId", msgId);
                    int hongbaoSize = response.getInt("hongbaoSize");
                    remindHB(hongbaoSize);

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("onSuccess:  " + error + content);
            }
        });
    }

    /**
     * ��������ˢ�²���
     */
    private void setStartLabel() {
        ILoadingLayout startLabel = listView.getLoadingLayoutProxy(true, false);
        startLabel.setPullLabel(getString(R.string.pull_to_refresh));
        startLabel.setReleaseLabel(getString(R.string.release_to_refresh));
        startLabel.setRefreshingLabel(getString(R.string.is_refreshing));
    }

    private void initView(View view) {
        listView = (PullToRefreshListView) findViewById(R.id.mainList);
        mUserIdTV = (TextView) view.findViewById(R.id.userId);
        mUserJinbiTv = (TextView) view.findViewById(R.id.userJinbi);
        mAllMoneyBtn = (Button) view.findViewById(R.id.userMoney);
        mAllTgUserBtn = (Button) view.findViewById(R.id.userTuiguang);
        mUserNameTv = (TextView) view.findViewById(R.id.userName);
        mTGSize = (TextView) view.findViewById(R.id.tuiguang_user);
        mAllMoneyBtn.setOnClickListener(this);
        mAllTgUserBtn.setOnClickListener(this);
        view.findViewById(R.id.homeChouJiang).setOnClickListener(this);
        view.findViewById(R.id.homeHelp).setOnClickListener(this);
        view.findViewById(R.id.homeMingxi).setOnClickListener(this);
        view.findViewById(R.id.homeMore).setOnClickListener(this);
        view.findViewById(R.id.home_hongbao).setOnClickListener(this);
        view.findViewById(R.id.home_getMoney).setOnClickListener(this);
        view.findViewById(R.id.home_Duihuan).setOnClickListener(this);
        view.findViewById(R.id.home_Tuiguang).setOnClickListener(this);
        listView.setEmptyView(view);
        listView.setMode(Mode.PULL_FROM_START);
        hongBaoSize = (TextView) view.findViewById(R.id.hongbaoSize);
        msgTishiLayout = (LinearLayout) view.findViewById(R.id.msg_tishi);

    }

    private void setListener() {
        listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                updateUser();
            }
        });
    }

    private void initUserData() {
        UserBean userBean = MyData.getData().getUserBean();
        long userId = Contans.DEFAULT_ID + userBean.getId();
        mUserIdTV.setText("ID: " + userId);
        mUserJinbiTv.setText("�ҵĽ��:" + userBean.getAllKeDouBi() + "��");
        mAllMoneyBtn.setText("һ��׬Ǯ" + userBean.getAllMoney() + "Ԫ");
        mAllTgUserBtn.setText("�ƹ�׬Ǯ" + userBean.getTGMoney() + "Ԫ");
        mUserNameTv.setText(userBean.getName());
        mTGSize.setText("�ƹ��û�:" + userBean.getTg() + "��");

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.userTuiguang:
            startActivity(TuiGuangActivity.class);
            break;
        case R.id.userMoney:
            startActivity(ZhuanQianJiLuActivity.class);
            break;
        case R.id.homeChouJiang:
            MyToast.showCustomerToast("������");
            // startActivity(ChouJiangActivity.class);
            break;
        case R.id.homeMingxi:
            startActivity(DuiHuanActivity.class);
            break;
        case R.id.homeHelp:
            startActivity(HelpActivity.class);
            break;
        case R.id.homeMore:
            startActivity(MoreActivity.class);
            remindMsg(View.GONE);
            break;
        case R.id.home_hongbao:
            startActivity(HongBaoActivity.class);
            badge1.hide();
            break;
        case R.id.home_getMoney:
            startActivity(KindActivity.class);
            break;
        case R.id.home_Duihuan:
            startActivity(DuiHuanActivity.class);
            break;
        case R.id.home_Tuiguang:
            startActivity(TGActivity.class);
            break;

        default:
            break;
        }

    }

    private void startActivity(Class<?> class1) {
        startActivity(new Intent(this, class1));
    }

    /**
     * ע���û���������.
     */
    private void updateUser() {
        if (TextUtils.isEmpty(MyTools.getImei(this))) {
            MyToast.showCustomerToast("��ȡ�ֻ�imeiʧ��");
            finish();
            return;
        } else if (MyTools.getImei(this).equals("000000000000000")) {
            MyToast.showCustomerToast("ģ����������ʹ�ñ����");
            this.finish();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("mac", MyTools.getImei(this));
        params.put("phone", MyTools.getTel(this));
        params.put("point", 0 + "");
        params.put("xh", MyTools.getModel());
        params.put("sdk", MyTools.getSDK());
        params.put("ips", "");
        Request.addUser(new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D(content + error);
                MyToast.showCustomerToast("ˢ��ʧ�� ������������");

            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D(response.toString());
                boolean isok = false;
                try {
                    isok = response.getBoolean("isok");
                    if (isok) {
                        UserBean userBean = JsonUtils.parse(response.getString("user"), UserBean.class);
                        MyData.getData().setUserBean(userBean);
                    }
                    initUserData();
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                LogUtil.D(url);
                listView.onRefreshComplete();
            }

        }, params);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // �����ؼ�ʱ����ʾ�û��Ƿ��˳�
        showExitDialog();
        return false;
    }

    public void showExitDialog() {
        if (isFinishing()) {
            return;
        }

        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("�˳�");
        dialog.setRightText("����");
        dialog.setTitle("��ʾ");
        dialog.setMessage("��ô�õ�����������������");
        dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppManager.getAppManager().AppExit();
                finish();
                System.gc();
            }
        });

        dialog.setOnRightListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startPinLunActivity();
            }
        });
        if (!isFinishing() && dialog != null) {
            dialog.show();
        }

    }

    public void startPinLunActivity() {
        try {
            Uri uri = Uri.parse("market://details?id=" + "com.hck.zhuanqian");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
        }

    }

}
