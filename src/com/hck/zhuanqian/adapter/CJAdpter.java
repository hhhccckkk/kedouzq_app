package com.hck.zhuanqian.adapter;

import java.util.List;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.adapter.TGUserAdpter.ViewHolder;
import com.hck.zhuanqian.bean.ChouJiangBean;
import com.hck.zhuanqian.bean.TGUserBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CJAdpter extends BaseAdapter{
    private Context context;
    private List<ChouJiangBean> beans;
   public CJAdpter(List<ChouJiangBean> beans,Context context){
       this.context=context;
       this.beans=beans;
   }
    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int arg0) {
        return beans.get(arg0);
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
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) arg1.getTag();
        }
        ChouJiangBean bean=beans.get(arg0);
        viewHolder.contentTextView.setText(bean.getContent());
        viewHolder.userNameTextView.setText(bean.getUsername());
        viewHolder.sendMsgBtn.setBackgroundResource(R.color.white);
        String timeString=bean.getTime();
        try {
            timeString=timeString.substring(5, 11);
        } catch (Exception e) {
        }
        viewHolder.sendMsgBtn.setText(timeString);
        viewHolder.sendMsgBtn.setTextColor(context.getResources().getColor(R.color.text_9999));
        ImageLoader.getInstance().displayImage(bean.getTouxiang(), viewHolder.touxiangImageView,getOpTions());
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
    

}
