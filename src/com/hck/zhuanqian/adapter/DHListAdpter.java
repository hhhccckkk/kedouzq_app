package com.hck.zhuanqian.adapter;import java.util.List;import android.content.Context;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.BaseAdapter;import android.widget.ImageView;import android.widget.TextView;import com.hck.kedouzq.R;import com.hck.zhuanqian.bean.OrderBean;import com.nostra13.universalimageloader.core.DisplayImageOptions;import com.nostra13.universalimageloader.core.ImageLoader;import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;public class DHListAdpter extends BaseAdapter {    private Context context;    private List<OrderBean> orderBeans;    public DHListAdpter(Context context, List<OrderBean> beans) {        this.context = context;        this.orderBeans = beans;    }    @Override    public int getCount() {        return orderBeans.size();    }    @Override    public Object getItem(int position) {        return orderBeans.get(position);    }    @Override    public long getItemId(int position) {        return position;    }    @Override    public View getView(int position, View convertView, ViewGroup parent) {        convertView = LayoutInflater.from(context).inflate(R.layout.listview_duihuan_item, null);        Holder holder = null;        holder = new Holder();        try {            OrderBean order = orderBeans.get(position);            holder.nameTextView = (TextView) convertView.findViewById(R.id.dh_text1);            holder.contentTextView = (TextView) convertView.findViewById(R.id.dh_text2);            holder.timeTextView = (TextView) convertView.findViewById(R.id.dh_text3);            holder.nameTextView.setText(order.getUserName());            String content = order.getContent();            holder.contentTextView.setText(content.replace("h", "").replace("null", "").trim());            holder.timeTextView.setText(order.getTime().substring(5, 16));            holder.txImageView = (ImageView) convertView.findViewById(R.id.userTx);            ImageLoader.getInstance().displayImage(order.getTx(), holder.txImageView, getOpTions());        } catch (Exception e) {            Log.d("hck", "eeee: " + e.toString());        }        return convertView;    }    static class Holder {        TextView nameTextView, contentTextView, timeTextView;        ImageView txImageView;    }    public DisplayImageOptions getOpTions() {        DisplayImageOptions options = null;        if (options == null) {            options = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(40)).build();        }        return options;    }}