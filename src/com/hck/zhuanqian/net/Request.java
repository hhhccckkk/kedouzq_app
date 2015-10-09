package com.hck.zhuanqian.net;

import java.net.URL;

import android.content.pm.PackageInfo;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Urls;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;

public class Request {
    private static final int TIME_OUT = 15 * 1000;
    public static final int REQUEST_SUCCESS = 1;
    private static HCKHttpClient client = new HCKHttpClient();
    static {
        client.setTimeout(TIME_OUT);
    }

    private static void post(String method, HCKHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        post(method, params, handler);
    }

    public static void post(String method, RequestParams params, HCKHttpResponseHandler handler) {
        params.put("password", MyData.key);
        client.post(Urls.MAIN_HOST_URL + method, params, handler);
    }

    private static void get(String method, HCKHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        get(method, params, handler);
    }

    public static void get(String method, RequestParams params, HCKHttpResponseHandler handler) {
        params.put("password", MyData.key);
        client.get(Urls.MAIN_HOST_URL + method, params, handler);
    }

    /**
     * 获取配置信息
     * 
     * @param handler
     */
    public static void getConfig(HCKHttpResponseHandler handler) {
        get(Urls.GET_CONFIG, handler);
    }

    /**
     * 获取分享信息.
     * 
     * @param handler
     */
    public static void getShareInfo(HCKHttpResponseHandler handler, int id) {
        RequestParams params = new RequestParams();
        params.put("id", id + "");
        get(Urls.GET_SHARE, params, handler);
    }

    /**
     * 获取红包数量信息数量.
     * 
     * @param handler
     */
    public static void getSize(HCKHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        get(Urls.GET_SIZE, params, handler);
    }

    /**
     * 增加用户
     * 
     * @param handler
     * @param params
     */
    public static void addUser(HCKHttpResponseHandler handler, RequestParams params) {
        params.put("password", MyData.key);
        post(Urls.ADD_USER, params, handler);
    }

    /**
     * 获取赚钱记录
     * 
     * @param handler
     * @param params
     * @param page
     */
    public static void getZhuanQianJiLu(HCKHttpResponseHandler handler, RequestParams params) {
        get(Urls.GET_ZHUANQIAN_JILU, params, handler);

    }

    /**
     * 获取推广信息.
     */
    public static void getTG(HCKHttpResponseHandler handler, RequestParams params) {
        get(Urls.GET_TuiGuang_Data, params, handler);

    }

    /**
     * 获取兑换记录
     * 
     * @param handler
     */
    public static void getDuiHuanJiLu(HCKHttpResponseHandler handler) {
        get(Urls.GET_DUIHUAN_JILU, handler);

    }

    /**
     * 获取赚钱区.
     * 
     * @param handler
     */
    public static void getKind(HCKHttpResponseHandler handler) {
        get(Urls.GET_KIND, handler);

    }

    /**
     * 增加金币
     * 
     * @param params
     * @param handler
     */

    public static void addPoint(int kid, int num, String adName, boolean isxt, int point, HCKHttpResponseHandler handler) {
        try {
            UserBean userBean = MyData.getData().getUserBean();
            RequestParams params = new RequestParams();
            params.put("uid", userBean.getId() + "");
            params.put("userName", userBean.getName() + "");
            params.put("kid", kid + "");
            params.put("num", num + "");
            params.put("type", adName);
            params.put("money", point + "");
            params.put("shangjia1", userBean.getShangjia1() + "");
            params.put("shangjia2", userBean.getShangjia2() + "");
            params.put("shangjia3", userBean.getShangjia3() + "");
            params.put("shangjia4", userBean.getShangjia4() + "");
            params.put("shangjia5", userBean.getShangjia5() + "");
            params.put("shangjia6", userBean.getShangjia6() + "");
            params.put("shangjia7", userBean.getShangjia7() + "");
            params.put("shangjia8", userBean.getShangjia8() + "");
            if (isxt) {
                params.put("isxt", 1 + "");
            } else {
                params.put("isxt", 0 + "");
            }
            post(Urls.ADD_POINT, params, handler);
        } catch (Exception e) {
            MyToast.showCustomerToast("数据异常 请重新登录");
            AppManager.getAppManager().AppExit();
        }

    }

    /**
     * 获取红包
     * 
     * @param params
     * @param handler
     */
    public static void getHongBao(RequestParams params, HCKHttpResponseHandler handler) {
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        post(Urls.GET_HONGBAO, params, handler);
    }

    /**
     * 修改红包状态.
     * 
     * @param params
     * @param handler
     */
    public static void updateHongBao(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.UPDATE_HONGBAO, params, handler);
    }

    /**
     * 修改用户
     * 
     * @param params
     * @param handler
     */
    public static void updateUserInfo(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.UPDATE_USER_INFO, params, handler);
    }

    /**
     * 增加pushid.
     * 
     * @param params
     * @param handler
     */
    public static void addUserPushId(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_USER_PUSH_ID, params, handler);
    }

    /**
     * 获取用户信息.
     * 
     * @param params
     * @param handler
     */
    public static void getMsg(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_MSG, params, handler);
    }

    /**
     * 删除消息.
     * 
     * @param params
     * @param handler
     */
    public static void deleteMsg(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.DELETE_MSG, params, handler);
    }

    /**
     * 增加意见反馈信息.
     * 
     * @param params
     * @param handler
     */
    public static void addYiJian(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_FANKUI, params, handler);
    }

    /**
     * 增加订单信息.
     * 
     * @param params
     * @param handler
     */
    public static void addOrder(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_ORDER, params, handler);
    }

    /**
     * 
     * 打包apk
     */
    public static void daBao(HCKHttpResponseHandler handler, RequestParams params) {
        get(Urls.GET_APP_DOWN_URL, params, handler);
    }

    /**
     * 获取推广链接.
     * 
     * @param handler
     */
    public static void getMyTgAppUrl(HCKHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        get(Urls.GET_MY_TG_APP_URL, params, handler);
    }

    /**
     * 增加推广信息.
     * 
     * @param params
     * @param handler
     */

    public static void addTGInfo(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_TG_INFO, params, handler);
    }

    /**
     * 更新抽奖次数
     * 
     * @param params
     * @param handler
     */
    public static void updateChouJiangSize(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.UPDATE_CHOUJIANG, params, handler);
    }

    /**
     * 获取抽奖次数.
     * 
     * @param params
     * @param handler
     */
    public static void shareGetCj(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.SHARE_GET_CHOUJIANG, params, handler);
    }

    /**
     * 获取单个用户信息
     * 
     * @param params
     * @param handler
     */
    public static void getUser(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_USER, params, handler);
    }

    /**
     * 获取推广用户size
     * 
     * @param params
     * @param handler
     */
    public static void getTgUserSize(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_TG_USER_SIZE, params, handler);
    }

    /**
     * 获取推广用户
     * 
     * @param params
     * @param handler
     */
    public static void getTgUser(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_TG_USER, params, handler);
    }

    /**
     * 发消息
     * 
     * @param params
     * @param handler
     */
    public static void addMsg(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_MSG, params, handler);
    }

    /**
     * 增加抽奖信息
     * 
     * @param params
     * @param handler
     */
    public static void addCJInfo(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_CJ_INFO, params, handler);
    }

    /**
     * 获取抽奖记录
     * 
     * @param params
     * @param handler
     */
    public static void getChouJiangInfo(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_CJ_INFO, params, handler);
    }

    /**
     * 获取版本信息.
     */
    public static void getAppInfo(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_APP_INFO, params, handler);
    }
}
