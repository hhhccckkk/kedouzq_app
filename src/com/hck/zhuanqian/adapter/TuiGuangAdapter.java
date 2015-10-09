package com.hck.zhuanqian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hck.kedouzq.R;
import com.hck.zhuanqian.bean.TuiGBean;
import com.hck.zhuanqian.data.TuiGuangData;

public class TuiGuangAdapter extends BaseAdapter {

	private TuiGuangData tData;
	private Context context;

	public TuiGuangAdapter(TuiGuangData data, Context context) {
		this.tData = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return tData.getTuiGBeans().size();
	}

	@Override
	public Object getItem(int arg0) {
		return tData.getTuiGBeans().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.tuiguang_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.contentTextView = (TextView) view
					.findViewById(R.id.tuiguang_content);
			viewHolder.timeTextView = (TextView) view
					.findViewById(R.id.tuiguang_time);
			viewHolder.userNameTextView = (TextView) view
					.findViewById(R.id.tuiguang_user);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		TuiGBean bean = null;
		bean = tData.getTuiGBeans().get(arg0);
		viewHolder.contentTextView.setText("ÕËµ¥: "+bean.getContent());
		String timeString = bean.getTime();
		viewHolder.timeTextView.setText(timeString.substring(0, 11));
		viewHolder.userNameTextView.setText("À´×Ô:"+bean.getUserName());
		return view;
	}

	static class ViewHolder {
		TextView contentTextView;
		TextView timeTextView;
		TextView userNameTextView;
	}

	public void notifyDataSetChanged(TuiGuangData tData) {
		this.tData.setTuiGBeans(tData.getTuiGBeans());
		this.notifyDataSetChanged();
	}

}
