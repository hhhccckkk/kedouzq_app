package com.hck.zhuanqian.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.TGUserBean;
import com.hck.zhuanqian.data.TGUserData;
import com.hck.zhuanqian.util.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class TGUserAdpter extends BaseAdapter {
    private TGUserData tgUserData;
    private Context context;
    private SendMsgCallBack callBack;

    public interface SendMsgCallBack {
        void sendMsg(TGUserBean userBean);
    }

    public TGUserAdpter(TGUserData tgUserData, Context context) {
        this.tgUserData = tgUserData;
        this.context = context;
        callBack = (SendMsgCallBack) context;
    }

    @Override
    public int getCount() {
        return tgUserData.getTgUserDatas().size();
    }

    @Override
    public Object getItem(int arg0) {
        return tgUserData.getTgUserDatas().get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.show_tg_user_item, null);
            viewHolder = new ViewHolder();
            viewHolder.contentTextView = (TextView) arg1.findViewById(R.id.tg_content);
            viewHolder.touxiangImageView = (ImageView) arg1.findViewById(R.id.tg_tx);
            viewHolder.userNameTextView = (TextView) arg1.findViewById(R.id.tg_userName);
            viewHolder.sendMsgBtn = (Button) arg1.findViewById(R.id.tg_send_msg);
            viewHolder.sendMsgBtn.setTag(arg0);
            setListener(viewHolder.sendMsgBtn);
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) arg1.getTag();
        }
        TGUserBean uData = tgUserData.getTgUserDatas().get(arg0);
        String qqString = uData.getQq();

        String content = uData.getJishu() + "级徒弟";
        if (!TextUtils.isEmpty(qqString)) {
            content = content + "qq号码: " + qqString;
        }
        content = content + " 他一共推广用户: " + uData.getTg() + "人";
        viewHolder.contentTextView.setText(content);
        viewHolder.userNameTextView.setText(uData.getUserName());
        ImageLoader.getInstance().displayImage(uData.getTouxiang(), viewHolder.touxiangImageView,getOpTions());
        return arg1;
    }
    public DisplayImageOptions getOpTions() {
        DisplayImageOptions options = null;
        if (options == null) {
            options = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(40)).build();
        }

        return options;
    }
    class ViewHolder {
        ImageView touxiangImageView;
        TextView userNameTextView;
        TextView contentTextView;
        Button sendMsgBtn;

    }

    public void updateData(List<TGUserBean> userBeans) {
        if (userBeans != null) {
            tgUserData.getTgUserDatas().addAll(userBeans);
            this.notifyDataSetChanged();
        }
    }

    private void setListener(Button button) {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LogUtil.D("onClickonClickonClick");
                try {
                    int pos = (int) arg0.getTag();
                    if (callBack != null) {
                        callBack.sendMsg(tgUserData.getTgUserDatas().get(pos));
                    }
                } catch (Exception e) {
                    LogUtil.D("Exception: "+e.toString());

                }

            }
        });
    }

}
