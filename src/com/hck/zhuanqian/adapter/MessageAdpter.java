package com.hck.zhuanqian.adapter;import java.util.List;import android.content.Context;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.BaseAdapter;import android.widget.TextView;import com.hck.kedouzq.R;import com.hck.zhuanqian.bean.MessageBean;import com.hck.zhuanqian.util.LogUtil;public class MessageAdpter extends BaseAdapter{  private List<MessageBean> mBeans;  private Context context;    public MessageAdpter(Context context,List<MessageBean> messageBeans)   {	   this.mBeans=messageBeans;	   this.context=context;   }	@Override	public int getCount() {		return mBeans.size();	}	@Override	public Object getItem(int position) {		return mBeans.get(position);	}	@Override	public long getItemId(int position) {		return position;	}	@Override	public View getView(int position, View convertView, ViewGroup parent) {		Holder holder=null;		if (convertView==null) {			convertView=LayoutInflater.from(context).inflate(R.layout.listview_message_item, null);			holder=new Holder();			holder.contentTextView=(TextView) convertView.findViewById(R.id.list_message_text);			holder.timeTextView=(TextView)convertView.findViewById(R.id.list_message_time);			holder.laiziTextView=(TextView) convertView.findViewById(R.id.laizi);			convertView.setTag(holder);		}		else {			holder=(Holder) convertView.getTag();		}		MessageBean messageBean=mBeans.get(position);		String contentString=messageBean.getContent();		holder.contentTextView.setText(contentString.replace("h", ""));		String timeString =messageBean.getTime();		timeString =timeString.substring(0, 11);		holder.timeTextView.setText(timeString);		holder.laiziTextView.setText("����: "+messageBean.getLaizi());		return convertView;	}   static class Holder   {	   TextView contentTextView;	   TextView timeTextView;	   TextView laiziTextView;   }      public void updateMsg(int pos){	   mBeans.remove(mBeans.get(pos));	   this.notifyDataSetChanged();   }	}