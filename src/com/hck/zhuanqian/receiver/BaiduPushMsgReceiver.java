package com.hck.zhuanqian.receiver;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.hck.httpserver.HCKHttpResponseHandler;
import com.hck.httpserver.JsonHttpResponseHandler;
import com.hck.httpserver.RequestParams;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.UserBean;
import com.hck.zhuanqian.data.MyData;
import com.hck.zhuanqian.net.Request;
import com.hck.zhuanqian.ui.MainActivity;
import com.hck.zhuanqian.ui.SplashActivity;
import com.hck.zhuanqian.util.LogUtil;

public class BaiduPushMsgReceiver extends PushMessageReceiver {

    /**
     * 在手机顶部显示通知.
     * 
     * @param context
     * @param tickerText
     * @param titleText
     * @param contentText
     * @param targetIntent
     * @param type
     */
    public void sendNotification(Context context, String tickerText, String titleText, String contentText, Intent targetIntent, int type) {
        try {
            clearAllNotification(context);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, type, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = builder.setSmallIcon(R.drawable.icon).setContentText(contentText).setTicker(tickerText).setContentTitle(titleText)
                    .setContentIntent(pendingIntent).setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).build();
            manager.notify(type, notification);
        } catch (Exception e) {
        }

    }

    /**
     * 清除所有通知.
     * 
     * @param context
     */

    public static void clearNotification(Context context, int type) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(type);
        } catch (Exception e) {
        }

    }

    public static void clearAllNotification(Context context) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();

        } catch (Exception e) {
        }

    }

    @Override
    public void onBind(Context arg0, int arg1, String arg2, String arg3, String arg4, String arg5) {
        LogUtil.D("onBindonBind: "+arg4);
        try {
            UserBean userBean = MyData.getData().getUserBean();
          
            if (userBean == null) {
                return;
            }
            if (TextUtils.isEmpty(userBean.getPushId())) {
                updateUser(arg4, userBean.getId());
            }
        } catch (Exception e) {
        }
        
    }

    @Override
    public void onMessage(Context arg0, String arg1, String arg2) {
        try {
            JSONObject object = new JSONObject(arg1);
            Intent intent = new Intent(arg0, SplashActivity.class);
            String title = object.getString("title");
            String msg = object.getString("description");
            sendNotification(arg0, title, title, msg, intent, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNotificationArrived(Context arg0, String arg1, String arg2, String arg3) {
    }

    @Override
    public void onDelTags(Context arg0, int arg1, List<String> arg2, List<String> arg3, String arg4) {
    }

    @Override
    public void onListTags(Context arg0, int arg1, List<String> arg2, String arg3) {
    }

    @Override
    public void onNotificationClicked(Context arg0, String arg1, String arg2, String arg3) {
    }

    @Override
    public void onSetTags(Context arg0, int arg1, List<String> arg2, List<String> arg3, String arg4) {

    }

    @Override
    public void onUnbind(Context arg0, int arg1, String arg2) {

    }

    private void updateUser(String pushId, long uid) {
        RequestParams params = new RequestParams();
        params.put("uid", uid + "");
        params.put("pushId", pushId);
        Request.addUserPushId(params, new JsonHttpResponseHandler() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response, String url) {
                super.onSuccess(statusCode, headers, response, url);
            }
        });
    }

}
