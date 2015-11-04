package com.hck.zhuanqian.ui;

/**
 * ������.
 */
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.waps.AppConnect;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.AppInfoBean;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.downapp.UpdateManager;
import com.hck.zhuanqian.downapp.UpdateUtil;
import com.hck.zhuanqian.downapp.UpdateUtil.UpdateAppCallBack;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.JsonUtils;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.util.MyPreferences;
import com.hck.zhuanqian.util.MyTools;
import com.hck.zhuanqian.view.CustomAlertDialog;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.Pdialog;
import com.hck.zhuanqian.view.RemindView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MainActivity extends Activity implements OnClickListener, UpdateAppCallBack {
    private TextView mUserIdTV, mUserJinbiTv, mUserNameTv, mTGSize;
    private Button mAllMoneyBtn, mAllTgUserBtn;
    private PullToRefreshListView listView;
    private TextView hongBaoSize, tGSizeTextView;
    private RemindView badge1, tgBadge;
    private LinearLayout msgTishiLayout;
    private TextView chouJiangTextView;
    private RemindView remindViewCJ;
    private ImageView txImageView;
    private UserBean userBean;
    private long mExitTime;
    private static final int CLICK_TIME_INTERVAL = 2000;
    AppInfoBean bean;
    private boolean isXinShou;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyPreferences(this);
        setContentView(R.layout.activity_home);
        userBean = MyData.getData().getUserBean();
        if (userBean == null) {
            MyToast.showCustomerToast("�û����������µ�¼");
            finish();

        }
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        initView(view);
        setStartLabel();
        getHongBaoAndMsgSize();
        setListener();
        startBaiDuPushServices();
        initHuoDongView();
        new UpdateUtil().isUpdate(this);
        isXinShou = MyPreferences.getBoolean("xinshou", true);
        if (isXinShou) {
            startActivity(XinShouActivity.class);
            MyPreferences.saveBoolean("xinshou", false);
        }
        try {
            AppConnect.getInstance(Contans.KEY_WANPU, "qq", this);
            AppConnect.getInstance(this).initPopAd( this);
        } catch (Exception e) {
        }
      
       

    }
  
    private void startBaiDuPushServices() {
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, Contans.BAIDU_PUSH_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean hasMsg = MyPreferences.getBoolean(Contans.HAS_MSG, false);
        LogUtil.D("hasMsghasMsg: "+hasMsg);
        if (hasMsg) {
            remindMsg(View.VISIBLE);
        } else {
            remindMsg(View.GONE);
        }
        updateUser();
        initUserData();
        if (bean != null) {
            if (isUpdate()) {
                showUpdateD(bean);
            }
        }
    }

    private void remind(RemindView remindView, String size) {
        if (remindView == null) {
            return;
        }
        remindView.setText(size); // ��Ҫ��ʾ����������
        remindView.setBadgePosition(RemindView.POSITION_TOP_RIGHT);// ��ʾ��λ��.���Ͻ�,BadgeView.POSITION_BOTTOM_LEFT,���󣬻���������������
        remindView.setTextColor(Color.WHITE); // �ı���ɫ
        remindView.setBadgeBackgroundColor(Color.RED); // ������Ϣ�ı�����ɫ���Լ�����
        remindView.setTextSize(12); // �ı���С
        remindView.setBadgeMargin(1); // ���߼��
        remindView.show();// ֻ����ʾ

    }

    private void remindHB(int size) {
        badge1 = new RemindView(this, hongBaoSize);
        remind(badge1, size + "");
    }

    private void remindTgSize(int size) {

        tgBadge = new RemindView(this, tGSizeTextView);
        if (size > 100) {
            remind(tgBadge, "99+");
        } else {
            remind(tgBadge, size + "");
        }

    }

    private void remindMsg(int isShow) {
        if (msgTishiLayout != null) {
            msgTishiLayout.setVisibility(isShow);
        }

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

                    long tgSize = response.getLong("tgSize");
                    long oldTgSize = MyPreferences.getLong("tgSize", 0l);
                    if (tgSize > oldTgSize && tgSize != 0) {
                        remindTgSize((int) (tgSize - oldTgSize));
                    }
                    MyPreferences.saveLong("tgSize", tgSize);

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
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
        mUserIdTV = (TextView) view.findViewById(R.id.userid);
        mUserJinbiTv = (TextView) view.findViewById(R.id.userJinbi);
        mAllMoneyBtn = (Button) view.findViewById(R.id.userMoney);
        mAllTgUserBtn = (Button) view.findViewById(R.id.userTuiguang);
        mUserNameTv = (TextView) view.findViewById(R.id.userName);
        mTGSize = (TextView) view.findViewById(R.id.tuiguang_user);
        mAllMoneyBtn.setOnClickListener(this);
        mAllTgUserBtn.setOnClickListener(this);
        view.findViewById(R.id.homeChouJiang_rl).setOnClickListener(this);
        view.findViewById(R.id.homeHelp).setOnClickListener(this);
        view.findViewById(R.id.homeMingxi).setOnClickListener(this);
        view.findViewById(R.id.homeMore).setOnClickListener(this);
        view.findViewById(R.id.home_hongbao).setOnClickListener(this);
        view.findViewById(R.id.home_getMoney).setOnClickListener(this);
        view.findViewById(R.id.home_Duihuan).setOnClickListener(this);
        view.findViewById(R.id.home_Tuiguang).setOnClickListener(this);
        view.findViewById(R.id.paihang).setOnClickListener(this);
        tGSizeTextView = (TextView) view.findViewById(R.id.tgSize);
        txImageView = (ImageView) view.findViewById(R.id.image_tx);
        chouJiangTextView = (TextView) view.findViewById(R.id.choujiang_TiShi);
        listView.setEmptyView(view);
        listView.setMode(Mode.PULL_FROM_START);
        hongBaoSize = (TextView) view.findViewById(R.id.hongbaoSize);
        msgTishiLayout = (LinearLayout) view.findViewById(R.id.msg_tishi);
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.tx).showImageForEmptyUri(R.drawable.tx) // url���Օ���ʾ��ͼƬ���Լ�����drawable�����
                .showImageOnFail(R.drawable.tx) // ����ͼƬ�������⣬����ʾ��ͼƬ //������
                .displayer(new RoundedBitmapDisplayer(40)) // ͼƬԲ����ʾ��ֵΪ����
                .build();
        if (userBean != null) {
            ImageLoader.getInstance().displayImage(userBean.getTouxiang(), txImageView, options);
        }

    }

    private void initHuoDongView() {
        remindViewCJ = new RemindView(this, chouJiangTextView);
        remind(remindViewCJ, "��");

    }

    private void setListener() {
        listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                updateUser();
                getHongBaoAndMsgSize();
            }
        });
    }

    private void updateUser() {
        if (userBean == null) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("uid", userBean.getId() + "");
        Request.getUser(params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                MyToast.showCustomerToast("����ʧ�� ������������");
            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                listView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                boolean isOk = false;
                try {
                    isOk = response.getBoolean("isok");
                    if (isOk) {
                        String userString = response.getString("user");
                        UserBean userBean = JsonUtils.parse(userString, UserBean.class);
                        MyPreferences.saveString("user", userString);
                        MyData.getData().setUserBean(userBean);
                        initUserData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUserData() {
        UserBean user = MyData.getData().getUserBean();
        if (user == null) {
            return;
        }
        long userId = Contans.DEFAULT_ID + user.getId();
        try {
            mUserIdTV.setText("ID: " + userId);
            mUserJinbiTv.setText("���: " + user.getAllKeDouBi() + "��");
            mTGSize.setText("׬Ǯ: " + user.getAllMoney() + "Ԫ");
            mAllTgUserBtn.setText("�ƹ�׬Ǯ" + getTgMoney(user.getTGMoney()) + "Ԫ");
            mUserNameTv.setText("�ǳ�:" + user.getName());
            mAllMoneyBtn.setText("�ҵ�ͽ��" + user.getTg() + "��");
        } catch (Exception e) {
        }

    }

    private String getTgMoney(long money) {
        double tgMoney = (double) money / 1000;
        String TG = new String(tgMoney + "");
        return TG;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.userTuiguang:
            startActivity(TuiGuangActivity.class);
            break;
        case R.id.userMoney:
            if (userBean.getTg() <= 0) {
                MyToast.showCustomerToast("����û��ͽ��Ŷ���Ͽ���ͽŶ");
                return;
            }
            startActivity(ShowTgUserActivity.class);
            break;
        case R.id.paihang:
            startActivity(PaiHangActivity.class);
            break;
        case R.id.homeChouJiang_rl:
            startActivity(HuoDongActivity.class);
            break;
        case R.id.homeMingxi:
            startActivity(DuiHuanActivity.class);
            break;
        case R.id.homeHelp:
            startActivity(XinShouActivity.class);
            break;
        case R.id.homeMore:
            startActivity(MoreActivity.class);
            remindMsg(View.GONE);
            break;
        case R.id.home_hongbao:
            startActivity(HongBaoActivity.class);
            if (badge1 != null) {
                badge1.hide();
            }
            break;
        case R.id.home_getMoney:
            startActivity(KindActivity.class);
            break;
        case R.id.home_Duihuan:
            getLoginUrl();
            break;
        case R.id.home_Tuiguang:
            if (tgBadge != null) {
                tgBadge.hide();
            }
            startActivity(TGActivity.class);
            break;

        default:
            break;
        }

    }

    public void startActivity(Class<?> class1) {
        startActivity(new Intent(this, class1));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // �����ؼ�ʱ����ʾ�û��Ƿ��˳�
        // if ((System.currentTimeMillis() - mExitTime) > CLICK_TIME_INTERVAL) {
        // MyToast.showCustomerToast("�ٰ�һ���˳�");
        // mExitTime = System.currentTimeMillis();
        // } else {
        // AppManager.getAppManager().AppExit();
        // MyToast.dissMissToast();
        // finish();
        // System.gc();
        // super.onBackPressed();
        // }
        showExitDialog();

        return false;
    }

    public void showGetImeiErrorDialog() {
        if (isFinishing()) {
            return;
        }

        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.hideRightBtn();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("�˳�");
        dialog.setRightText("����");
        dialog.setTitle("��ʾ");
        dialog.setMessage("��ȡ�ֻ�imeiʧ�ܣ��������ֻ���ȡimei��,�ֻ�Ψһ��ʶ��лл");
        dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyToast.dissMissToast();
                AppManager.getAppManager().AppExit();
                finish();
                System.gc();

            }
        });
        try {
            if (!isFinishing() && dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void backAppInfo(AppInfoBean bean) {
        this.bean = bean;
        try {
            if (isUpdate()) {
                showUpdateD(bean);
            }
        } catch (Exception e) {
        }

    }

    private boolean isUpdate() {
        if (bean != null) {
            if (bean.getVersionCode() > MyTools.getVerCode(this)) {
                return true;
            }
        }
        return false;
    }

    private void showUpdateD(final AppInfoBean bean) {
        if (isFinishing()) {
            return;
        }

        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("�˳�");
        dialog.setRightText("����");
        dialog.setTitle("��⵽�°汾,������");
        dialog.setMessage(bean.getContent());
        dialog.setOnLeftListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyToast.dissMissToast();
                AppManager.getAppManager().AppExit();
                finish();
                System.gc();

            }
        });
        dialog.setOnRightListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDownApp(bean.getDownUrl());
            }
        });
        try {
            if (!isFinishing() && dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {
        }
    }

    private void startDownApp(String downUrl) {
        UpdateManager manager = new UpdateManager(this);
        manager.downloadApk(downUrl);
    }

    private void getLoginUrl() {
        Pdialog.showDialog(this, "���Ե�", true);
        RequestParams params = new RequestParams();
        UserBean userBean = MyData.getData().getUserBean();
        params.put("uid", userBean.getId() + "");
        params.put("point", userBean.getAllKeDouBi() + "");
        Request.getLoginUrl(params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                LogUtil.D("onFailure: " + content + error);
                MyToast.showCustomerToast("�����쳣 ������");
            }

            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                Pdialog.hiddenDialog();
                LogUtil.D("onFinish: " + url);
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                LogUtil.D("getLoginUrl: " + response.toString());
                try {
                    boolean isok = response.getBoolean("isok");
                    if (isok) {
                        String urlString = response.getString("url");
                        startDuiHuangActivity(urlString);
                    }
                } catch (Exception e) {
                }

            }
        });
    }

    private void startDuiHuangActivity(String url) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, CreditActivity.class);
        intent.putExtra("navColor", "#e55107"); // ���õ������ı�����ɫ������#ffffff����ʽ��
        intent.putExtra("titleColor", "#ffffff"); // ���õ������������ɫ������#ffffff����ʽ��
        intent.putExtra("url", url); // �����Զ���½��ַ��ÿ�������˶�̬���ɡ�
        intent.putExtra("type", 1);
        intent.putExtra("title", "�һ�����");
        startActivity(intent);
    }

    public void showExitDialog() {
        if (isFinishing()) {
            return;
        }

        CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLeftText("�˳�");
        dialog.setRightText("������");
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
            Uri uri = Uri.parse("market://details?id=" + "com.hck.kedouzq");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
        }

    }

}
