package com.hck.zhuanqian.net;

import java.net.URL;

import android.content.pm.PackageInfo;

import com.hck.httpserver.HCKHttpClient;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Urls;
import com.hck.zhuanqian.util.AppManager;
import com.hck.zhuanqian.util.LogUtil;
import com.hck.zhuanqian.view.MyToast;

public class Request {
    private static final int TIME_OUT = 15 * 1000;
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
     * ��ȡ������Ϣ
     * 
     * @param handler
     */
    public static void getConfig(HCKHttpResponseHandler handler) {
        get(Urls.GET_CONFIG, handler);
    }

    /**
     * ��ȡ������Ϣ.
     * 
     * @param handler
     */
    public static void getShareInfo(HCKHttpResponseHandler handler, int id) {
        RequestParams params = new RequestParams();
        params.put("id", id + "");
        get(Urls.GET_SHARE, params, handler);
    }

    /**
     * ��ȡ���������Ϣ����.
     * 
     * @param handler
     */
    public static void getSize(HCKHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        get(Urls.GET_SIZE, params, handler);
    }

    /**
     * �����û�
     * 
     * @param handler
     * @param params
     */
    public static void addUser(HCKHttpResponseHandler handler, RequestParams params) {
        params.put("password", MyData.key);
        post(Urls.ADD_USER, params, handler);
    }

    /**
     * ��ȡ׬Ǯ��¼
     * 
     * @param handler
     * @param params
     * @param page
     */
    public static void getZhuanQianJiLu(HCKHttpResponseHandler handler, RequestParams params) {
        get(Urls.GET_ZHUANQIAN_JILU, params, handler);

    }

    /**
     * ��ȡ�ƹ���Ϣ.
     */
    public static void getTG(HCKHttpResponseHandler handler, RequestParams params) {
        get(Urls.GET_TuiGuang_Data, params, handler);

    }

    /**
     * ��ȡ�һ���¼
     * 
     * @param handler
     */
    public static void getDuiHuanJiLu(HCKHttpResponseHandler handler) {
        get(Urls.GET_DUIHUAN_JILU, handler);

    }

    /**
     * ��ȡ׬Ǯ��.
     * 
     * @param handler
     */
    public static void getKind(HCKHttpResponseHandler handler) {
        get(Urls.GET_KIND, handler);

    }

    /**
     * ���ӽ��
     * 
     * @param params
     * @param handler
     */

    public static void addPoint(int kid, int num, String type, int point, boolean isTg, HCKHttpResponseHandler handler) {
        try {
            RequestParams params = new RequestParams();
            params.put("uid", MyData.getData().getUserBean().getId() + "");
            params.put("kid", kid + "");
            params.put("num", num + "");
            params.put("type", type);
            params.put("money", point + "");
            if (isTg) {
                LogUtil.D("tgtg");
                params.put("isTg", 1 + "");
            }
            post(Urls.ADD_POINT, params, handler);
        } catch (Exception e) {
            MyToast.showCustomerToast("�����쳣 �����µ�¼");
            AppManager.getAppManager().AppExit();
        }

    }

    /**
     * ��ȡ���
     * 
     * @param params
     * @param handler
     */
    public static void getHongBao(RequestParams params, HCKHttpResponseHandler handler) {
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        post(Urls.GET_HONGBAO, params, handler);
    }

    /**
     * �޸ĺ��״̬.
     * 
     * @param params
     * @param handler
     */
    public static void updateHongBao(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.UPDATE_HONGBAO, params, handler);
    }

    /**
     * �޸��û�
     * 
     * @param params
     * @param handler
     */
    public static void updateUserInfo(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.UPDATE_USER_INFO, params, handler);
    }

    /**
     * ��ȡ�û���Ϣ.
     * 
     * @param params
     * @param handler
     */
    public static void getMsg(RequestParams params, HCKHttpResponseHandler handler) {
        get(Urls.GET_MSG, params, handler);
    }

    /**
     * ɾ����Ϣ.
     * 
     * @param params
     * @param handler
     */
    public static void deleteMsg(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.DELETE_MSG, params, handler);
    }

    /**
     * �������������Ϣ.
     * 
     * @param params
     * @param handler
     */
    public static void addYiJian(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_FANKUI, params, handler);
    }

    /**
     * ���Ӷ�����Ϣ.
     * 
     * @param params
     * @param handler
     */
    public static void addOrder(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_ORDER, params, handler);
    }

    /**
     * 
     * ���apk
     */
    public static void daBao(HCKHttpResponseHandler handler, RequestParams params) {
        get(Urls.GET_APP_DOWN_URL, params, handler);
    }

    /**
     * ��ȡ�ƹ�����.
     * 
     * @param handler
     */
    public static void getMyTgAppUrl(HCKHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", MyData.getData().getUserBean().getId() + "");
        get(Urls.GET_MY_TG_APP_URL, params, handler);
    }

    /**
     * �����ƹ���Ϣ.
     * 
     * @param params
     * @param handler
     */

    public static void addTGInfo(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.ADD_TG_INFO, params, handler);
    }
   /**
    * ���³齱����
    * @param params
    * @param handler
    */
    public static void updateChouJiangSize(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.UPDATE_CHOUJIANG, params, handler);
    }
    public static void shareGetCj(RequestParams params, HCKHttpResponseHandler handler) {
        post(Urls.SHARE_GET_CHOUJIANG, params, handler);
    }
}
