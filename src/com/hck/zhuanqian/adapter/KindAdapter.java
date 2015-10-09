package com.hck.zhuanqian.adapter;

import java.util.List;

import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.KindBean;
import com.hck.zhuanqian.data.Contans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class KindAdapter extends BaseAdapter {
    private List<KindBean> kindBeans;
    private Context context;

    public KindAdapter(List<KindBean> kindBean, Context context) {
        this.kindBeans = kindBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return kindBeans.size();
    }

    @Override
    public Object getItem(int arg0) {
        return kindBeans.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View content, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (content == null) {
            content = LayoutInflater.from(context).inflate(R.layout.kind_item, null);
            viewHolder = new ViewHolder();
            viewHolder.kindConten = (TextView) content.findViewById(R.id.kind_content);
            viewHolder.kindName = (TextView) content.findViewById(R.id.kind_name);
            viewHolder.kindIconTextView = (TextView) content.findViewById(R.id.kind_icon);
            content.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) content.getTag();
        }
        setImage(kindBeans.get(arg0).getAid(), viewHolder.kindIconTextView);
        viewHolder.kindConten.setText(kindBeans.get(arg0).getNeirong());
        viewHolder.kindName.setText(kindBeans.get(arg0).getContent());

        return content;
    }

    static class ViewHolder {
        TextView kindName, kindConten, kindIconTextView;
    }

    private void setImage(int type, TextView textView) {
        switch (type) {
        case Contans.AD_DIANLE:
            changeView(textView, Contans.AD_NAME_DIANLE);
            textView.setBackgroundResource(R.drawable.quan1);
            break;
        case Contans.AD_DIANCAI:
            changeView(textView, Contans.AD_NAME_DIANCAI);
            textView.setBackgroundResource(R.drawable.quan2);
            break;
        case Contans.AD_DUOMENG:
            changeView(textView, Contans.AD_NAME_DUOMENG);
            textView.setBackgroundResource(R.drawable.quan3);
            break;
        case Contans.AD_DATOUNIAO:
            changeView(textView, Contans.AD_NAME_DATOUNIAO);
            textView.setBackgroundResource(R.drawable.quan4);
            break;
        case Contans.AD_YOUMI:
            changeView(textView, Contans.AD_NAME_YOUMI);
            textView.setBackgroundResource(R.drawable.quan5);
            break;
        case Contans.AD_YEGUO:
            changeView(textView, Contans.AD_NAME_YEGUO);
            textView.setBackgroundResource(R.drawable.quan6);
            break;
        case Contans.AD_JIONG_YOU:
            changeView(textView, Contans.AD_NMAE_JIONG_YOU);
            textView.setBackgroundResource(R.drawable.quan7);
            break;
        case Contans.AD_GUOMENG:
            changeView(textView, Contans.AD_NMAE_GUOMENG);
            textView.setBackgroundResource(R.drawable.quan8);
            break;
        case Contans.AD_KEKE:
            changeView(textView, Contans.AD_NAME_KEKE);
            textView.setBackgroundResource(R.drawable.quan9);
            break;
        case Contans.AD_ZHONGYI:
            changeView(textView, Contans.AD_NMAE_ZHONYI);
            textView.setBackgroundResource(R.drawable.quan10);
            break;
        case Contans.AD_BEIDUO:
            changeView(textView, Contans.AD_NAME_BEIDUO);
            textView.setBackgroundResource(R.drawable.quan1);
            break;
        case Contans.AD_KIND_JINGDAI:
            changeView(textView, Contans.AD_NAME_JINGDAI);
            textView.setBackgroundResource(R.drawable.quan2);
            break;
        case Contans.AD_ID_WANPU:
            changeView(textView, Contans.AD_NMAE_WANPU);
            textView.setBackgroundResource(R.drawable.quan3);
            break;
        case Contans.AD_ID_LEDIAN:
            changeView(textView, Contans.AD_NAME_LEDIAN);
            textView.setBackgroundResource(R.drawable.quan4);
            break;
        case Contans.AD_KIND_DIQI:
            changeView(textView, Contans.AD_NAME_DIQI);
            textView.setBackgroundResource(R.drawable.quan5);
            break;
        case Contans.AD_KIND_XIAOTANG:
            changeView(textView, Contans.AD_NAME_XIAOTANG);
            textView.setBackgroundResource(R.drawable.quan6);
            break;
        default:
            break;
        }

    }

    private void changeView(TextView textView,String adName) {
        textView.setText(adName);
    }

}
