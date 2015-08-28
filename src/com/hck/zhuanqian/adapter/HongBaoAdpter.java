package com.hck.zhuanqian.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.bean.Hongbao;
import com.hck.zhuanqian.util.LogUtil;

public class HongBaoAdpter extends BaseAdapter {
	public List<Hongbao> hongbaos;
	private Context context;
	private static final int IS_OPEN = 1;
	private CallBack callBack;

	public interface CallBack {
		void daKai(Hongbao hongbao,int post);

	}

	public HongBaoAdpter(List<Hongbao> hongbaos, Context context) {
		this.hongbaos = hongbaos;
		this.context = context;
		this.callBack = (CallBack) context;
	}

	@Override
	public int getCount() {
		return hongbaos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return hongbaos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		ViewHolder viewHolder = null;

		contentView = LayoutInflater.from(context).inflate(
				R.layout.hongbao_item, null);
		viewHolder = new ViewHolder();
		viewHolder.contenTextView = (TextView) contentView
				.findViewById(R.id.hongbao_content);
		viewHolder.timeTextView = (TextView) contentView
				.findViewById(R.id.hongbao_time);
		viewHolder.button = (Button) contentView.findViewById(R.id.dakai);
		viewHolder.button.setTag(arg0);

		viewHolder.contenTextView.setText(hongbaos.get(arg0).getContent());
		String time = hongbaos.get(arg0).getTime();
		try {
			time = time.substring(2, 9);
		} catch (Exception e) {
		}
		
		time = "来自: " + hongbaos.get(arg0).getuName() + " " + time;
		viewHolder.timeTextView.setText(time);
		int isOpen = hongbaos.get(arg0).getIsOpen();
		if (isOpen == IS_OPEN) {
			viewHolder.button.setBackground(null);
			viewHolder.button.setText("已领取");
			viewHolder.button.setTextColor(context.getResources().getColor(
					R.color.huise));
			viewHolder.button.setEnabled(false);
		} else {
			viewHolder.button
					.setBackgroundResource(R.drawable.duihuan_btn_submit_selector);
			viewHolder.button.setText("拆开");
			viewHolder.button.setTextColor(context.getResources().getColor(
					R.color.white));
			viewHolder.button.setEnabled(true);
		}
		viewHolder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int post = Integer.parseInt(arg0.getTag().toString());
				Hongbao hongbao = hongbaos.get(post);
				callBack.daKai(hongbao,post);
			}
		});
		return contentView;
	}

	 class ViewHolder {
		TextView contenTextView;
		TextView timeTextView;
		Button button;
	}

	public void update(List<Hongbao> hongbaos) {
		this.hongbaos.addAll(hongbaos);
		this.notifyDataSetChanged();
	}
	public void update(int post){
		this.hongbaos.get(post).setIsOpen(IS_OPEN);
		this.notifyDataSetChanged();
	}

}
