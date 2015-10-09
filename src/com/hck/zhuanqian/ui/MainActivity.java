package com.hck.zhuanqian.ui;

/**
 * 主界面.
 */
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyPreferences(this);
        setContentView(R.layout.activity_home);
        userBean = MyData.getData().getUserBean();
        if (userBean == null) {
            MyToast.showCustomerToast("用户过期请重新登录");
            finish();

        }
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        initView(view);
        setStartLabel();
        getHongBaoAndMsgSize();
        setListener();
        startBaiDuPushServices();
        new UpdateUtil().isUpdate(this);
    }

    private void startBaiDuPushServices() {
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, Contans.BAIDU_PUSH_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserData();
        updateView();
        if (bean != null) {
            if (isUpdate()) {
                showUpdateD(bean);
            }
        }
    }

    private void remind(RemindView remindView, int size) {
        if (remindView == null) {
            return;
        }
        remindView.setText(size + ""); // 需要显示的提醒类容
        remindView.setBadgePosition(RemindView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        remindView.setTextColor(Color.WHITE); // 文本颜色
        remindView.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
        remindView.setTextSize(12); // 文本大小
        remindView.setBadgeMargin(5); // 各边间隔
        remindView.show();// 只有显示

    }

    private void remindHB(int size) {
        badge1 = new RemindView(this, hongBaoSize);
        remind(badge1, size);
    }

    private void remindTgSize(int size) {
        tgBadge = new RemindView(this, tGSizeTextView);
        remind(tgBadge, size);

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
     * 设置下拉刷新布局
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
        view.findViewById(R.id.order).setOnClickListener(this);
        tGSizeTextView = (TextView) view.findViewById(R.id.tgSize);
        txImageView = (ImageView) view.findViewById(R.id.image_tx);
        chouJiangTextView = (TextView) view.findViewById(R.id.choujiang_TiShi);
        listView.setEmptyView(view);
        listView.setMode(Mode.PULL_FROM_START);
        hongBaoSize = (TextView) view.findViewById(R.id.hongbaoSize);
        msgTishiLayout = (LinearLayout) view.findViewById(R.id.msg_tishi);
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.tx).showImageForEmptyUri(R.drawable.tx) // url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.drawable.tx) // 加载图片出现问题，会显示该图片 //缓存用
                .displayer(new RoundedBitmapDisplayer(40)) // 图片圆角显示，值为整数
                .build();
        if (userBean != null) {
            ImageLoader.getInstance().displayImage(userBean.getTouxiang(), txImageView, options);
        }

    }

    private void updateView() {
        UserBean userBean = MyData.getData().getUserBean();
        if (remindViewCJ == null) {
            remindViewCJ = new RemindView(this, chouJiangTextView);
        }
        if (userBean == null || remindViewCJ == null) {
            return;
        }
        remindViewCJ.toggle();
        int choujiang = userBean.getChoujiang();
        if (choujiang > 0) {
            remind(remindViewCJ, choujiang);
        } else {
            if (remindViewCJ != null) {
                remindViewCJ.hide();
                remindViewCJ = null;
            }

        }

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
                MyToast.showCustomerToast("更新失败 请检查您的网络");
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
                        updateView();
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
            mUserJinbiTv.setText("我的金币: " + user.getAllKeDouBi() + "个");
            mTGSize.setText("一共赚钱: " + user.getAllMoney() + "元");
            mAllTgUserBtn.setText("推广赚钱" + getTgMoney(user.getTGMoney()) + "元");
            mUserNameTv.setText("昵称:" + user.getName());
            mAllMoneyBtn.setText("我的徒弟" + user.getTg() + "个");
        } catch (Exception e) {
            // TODO: handle exception
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
                MyToast.showCustomerToast("您还没有徒弟哦，赶快收徒哦");
                return;
            }
            startActivity(ShowTgUserActivity.class);
            break;
        case R.id.order:
            startActivity(ZhuanQianJiLuActivity.class);
            break;
        case R.id.homeChouJiang_rl:
            startActivity(ChouJiangActivity.class);
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
            if (badge1 != null) {
                badge1.hide();
            }
            break;
        case R.id.home_getMoney:
            startActivity(KindActivity.class);
            break;
        case R.id.home_Duihuan:
            startActivity(DuiHuanActivity.class);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 按返回键时候，提示用户是否退出
        if ((System.currentTimeMillis() - mExitTime) > CLICK_TIME_INTERVAL) {
            MyToast.showCustomerToast("再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit();
            MyToast.dissMissToast();
            finish();
            System.gc();
            super.onBackPressed();
        }
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
        dialog.setLeftText("退出");
        dialog.setRightText("重试");
        dialog.setTitle("提示");
        dialog.setMessage("获取手机imei失败，请允许手机获取imei号,手机唯一标识，谢谢");
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
        dialog.setLeftText("退出");
        dialog.setRightText("更新");
        dialog.setTitle("检测到新版本,请升级");
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

}
