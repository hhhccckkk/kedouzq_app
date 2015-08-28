package com.hck.zhuanqian.ui;

/**
 * 主界面.
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
        mUserJinbiTv.setText("我的金币:" + userBean.getAllKeDouBi() + "个");
        mAllMoneyBtn.setText("一共赚钱" + userBean.getAllMoney() + "元");
        mAllTgUserBtn.setText("推广赚钱" + userBean.getTGMoney() + "元");
        mUserNameTv.setText(userBean.getName());
        mTGSize.setText("推广用户:" + userBean.getTg() + "个");

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
            MyToast.showCustomerToast("开发中");
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
     * 注册用户到服务器.
     */
    private void updateUser() {
        if (TextUtils.isEmpty(MyTools.getImei(this))) {
            MyToast.showCustomerToast("获取手机imei失败");
            finish();
            return;
        } else if (MyTools.getImei(this).equals("000000000000000")) {
            MyToast.showCustomerToast("模拟器，不能使用本软件");
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
                MyToast.showCustomerToast("刷新失败 请检查您的网络");

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
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 按返回键时候，提示用户是否退出
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
        dialog.setLeftText("退出");
        dialog.setRightText("好评");
        dialog.setTitle("提示");
        dialog.setMessage("这么好的软件，必需给个好评");
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
