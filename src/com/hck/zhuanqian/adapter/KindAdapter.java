package com.hck.zhuanqian.adapter;

import java.util.List;

import com.hck.zhuanqian.R;
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
			content = LayoutInflater.from(context).inflate(R.layout.kind_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.kindConten = (TextView) content
					.findViewById(R.id.kind_content);
			viewHolder.kindName = (TextView) content
					.findViewById(R.id.kind_name);
			viewHolder.imageView = (ImageView) content
					.findViewById(R.id.kind_img);
			content.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) content.getTag();
		}
		setImage(kindBeans.get(arg0).getAid(), viewHolder.imageView);
		viewHolder.kindConten.setText(kindBeans.get(arg0).getNeirong());
		viewHolder.kindName.setText(kindBeans.get(arg0).getContent());
		return content;
	}

	static class ViewHolder {
		TextView kindName, kindConten;
		ImageView imageView;
	}

	private void setImage(int type, ImageView imageView) {
		switch (type) {
		case Contans.AD_DIANLE:
			imageView.setImageResource(R.drawable.dianle);
			break;
		case Contans.AD_DIANCAI:
			imageView.setImageResource(R.drawable.diancai);
			break;
		case Contans.AD_DUOMENG:
			imageView.setImageResource(R.drawable.duomeng);
			break;
		case Contans.AD_DATOUNIAO:
			imageView.setImageResource(R.drawable.datouliao);
			break;
		case Contans.AD_YOUMI:
			imageView.setImageResource(R.drawable.youmi);
			break;
		case Contans.AD_YEGUO:
			imageView.setImageResource(R.drawable.yeguo);
			break;
		case Contans.AD_JIONG_YOU:
			imageView.setImageResource(R.drawable.wanpu);
			break;
		case Contans.AD_GUOMENG:
			imageView.setImageResource(R.drawable.guomeng);
			break;
		case Contans.AD_KEKE:
			imageView.setImageResource(R.drawable.midi);
			break;
		case Contans.AD_ZHONGYI:
			imageView.setImageResource(R.drawable.zhongyi);
			break;
		case Contans.AD_QI_DIAN:
			imageView.setImageResource(R.drawable.qidian);
			break;

		default:
			break;
		}
	}

}
