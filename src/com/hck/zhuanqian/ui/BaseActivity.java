package com.hck.zhuanqian.ui;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.data.Contans;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.CustomAlertDialog;
import com.hck.zhuanqian.view.MyToast;
import com.hck.zhuanqian.view.TitleBar;

/**
 * 基础界面.
 */
public class BaseActivity extends FragmentActivity {
    private static final int ADD_MONEY_SUCCESS = 2;
    private static final int ADD_MONEY_OUT_SIZE = 1;
    protected TitleBar mTitleBar;
    public RequestParams params;
    public boolean isOK;
    public int type, maxNum;
    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean getAirplaneMode() {
        int isAirplaneMode = Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        return (isAirplaneMode == 1) ? true : false;
    }

    @Override
    public void setContentView(int layout) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initTitleBar();
        ViewGroup root = getRootView();
        View paramView = getLayoutInflater().inflate(layout, null);
        root.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        root.addView(paramView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(root);
    }

    private ViewGroup getRootView() {
        LinearLayout localLinearLayout = new LinearLayout(this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        return localLinearLayout;
    }

    private void initTitleBar() {
        mTitleBar = new TitleBar(this);
    }

    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    public String getStringData(int id) {
        return getResources().getString(id);
    }

    public void initTitle(String content) {
        mTitleBar.setTitleContent(content);

    }

    public void startActivity(Class<?> class1) {
        startActivity(new Intent(this, class1));
    }

    public void getAdInitData() {
        type = getIntent().getIntExtra("type", 0);
        maxNum = getIntent().getIntExtra("maxNum", 5);
    }

    public void initDownSize(View textView) {

        ((TextView) textView).setText("该区每天限制下载app " + maxNum + "个");
    }

    public void showErrorToast() {
        MyToast.showCustomerToast("" + "初始化失败 请重试");
    }

    public void showGetMoneyErrorToast() {
        MyToast.showCustomerToast("" + "获取金币失败 请检查网络");
    }

    public void showNetErrorToast() {
        MyToast.showCustomerToast("" + "获取数据失败 请检查网络");
    }

    public void updateUserPoint(long point) {
        try {
            long nowPoint = MyData.getData().getUserBean().getAllKeDouBi();
            nowPoint = nowPoint + point;
            MyData.getData().getUserBean().setAllKeDouBi(nowPoint);
            MyToast.showCustomerToast("获得金币: " + point + "个");
        } catch (Exception e) {
            MyToast.showCustomerToast("网络异常增加金币失败");
        }
    }

    class MyRequestCallBack extends JsonHttpResponseHandler {
        int point = 0;

        public MyRequestCallBack(int point) {
            this.point = point;
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            LogUtil.D("增加金币失败: " + error + content);
            MyToast.showCustomerToast("网络异常增加金币失败");
        }

        @Override
        public void onSuccess(int statusCode, JSONObject response) {
            super.onSuccess(statusCode, response);
            LogUtil.D("增加金币成功: " + response.toString());
            dealResult(response, point);

        }

    }

    private void dealResult(JSONObject response, int point) {
        try {
            isOK = response.getBoolean("isok");
            if (isOK) {
                int type = response.getInt("type");
                if (type == ADD_MONEY_OUT_SIZE) {
                    MyToast.showCustomerToast("增加金币失败 本区每天限制做任务" + maxNum + "个");
                } else if (type == ADD_MONEY_SUCCESS) {
                    updateUserPoint(point);
                }

            } else {
                MyToast.showCustomerToast("网络异常增加金币失败");
            }
        } catch (Exception e) {
            MyToast.showCustomerToast("网络异常增加金币失败");
        }
    }

    public void savePoint(String adName, int point) {
        Request.addPoint(type, maxNum, adName, point, false, new MyRequestCallBack(point));
    }

    public void savePoint(String adName, int point, boolean isTG, HCKHttpResponseHandler handler) {
        Request.addPoint(type, maxNum, adName, point, isTG, handler);
    }

    public void savePoint(int point) {
        Request.addPoint(Contans.CHOUJIANG, 1000, "抽奖", point, false, new MyRequestCallBack(point));
    }

}
