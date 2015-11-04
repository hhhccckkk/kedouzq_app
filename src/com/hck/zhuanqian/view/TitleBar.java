package com.hck.zhuanqian.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hck.kedouzq.R;

/**
 * 公用title.
 */
public class TitleBar extends LinearLayout {
	private ImageView mLeftBackBtn; // 左边返回按钮
	private TextView mCenterTextV; // 中间文本.
	private Context mContext;
    private Button rightButton;
	public TitleBar(Context context) {
		super(context);
		mContext = context;
		init(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(context);
	}

	/**
	 * 初始化view.
	 * 
	 * @param context
	 */
	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.title_bar, this);
		mLeftBackBtn = (ImageView) findViewById(R.id.titleBackBtn);
		mCenterTextV = (TextView) findViewById(R.id.titleCenterTV);
		rightButton =(Button) findViewById(R.id.right_btn);
		setListener();
	}

	/**
	 * 点击返回按钮退出当前activity.
	 */
	private void setListener() {
		mLeftBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) mContext).finish();
			}
		});
	}
	
	public void setTitleContent(String content){
		mCenterTextV.setText(content);
	}
	public ImageView getLeftBtn(){
	    return mLeftBackBtn;
	}
	public Button getRightBtn(){
	    rightButton.setVisibility(View.VISIBLE);
	    return rightButton;
	}
	
	public void hidenRightBtn(){
	    rightButton.setVisibility(View.GONE);
	}

}
