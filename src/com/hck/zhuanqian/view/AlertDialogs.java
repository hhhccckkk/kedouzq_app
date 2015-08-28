package com.hck.zhuanqian.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hck.zhuanqian.R;
import com.hck.zhuanqian.util.MyTools;

public class AlertDialogs {
	public static AlertDialog aDialog;
	public static AlertDialog aDialog2;



	public static OneBtOnclick oneBtOnclick;

	public interface OneBtOnclick {
		public void callBack(int tag);
	}

	public static void alert(Context context, String title, String content,
			boolean big, final OneBtOnclick oneBtOnclick, final int tag) {
		if (aDialog != null && aDialog.isShowing()) {
			aDialog.dismiss();
		}
		AlertDialogs.oneBtOnclick = null;
		AlertDialogs.oneBtOnclick = oneBtOnclick;
		aDialog = new AlertDialog.Builder(context).create();
		aDialog.setCancelable(false);
		final View view = LayoutInflater.from(context).inflate(R.layout.alert,
				null);
		TextView contenTextView = (TextView) view.findViewById(R.id.d_content);
		Button button = (Button) view.findViewById(R.id.d_button);
		contenTextView.setText(content);
		try {
			aDialog.show();
		} catch (Exception e) {
		}

		WindowManager.LayoutParams params = aDialog.getWindow().getAttributes();
		params.width = (int) (MyTools.getScreenWidth() * 0.8); // 设置对话框的宽度为手机屏幕的0.8
		if (big) {
			params.height = (int) (MyTools.getScreenHeight() * 0.35);
		} else {
			params.height = (int) (MyTools.getScreenHeight() * 0.25);
		}
		// ;// 设置对话框的高度为手机屏幕的0.25
		aDialog.getWindow().setAttributes(params);
		aDialog.getWindow().setContentView(view);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aDialog.dismiss();
				if (oneBtOnclick != null) {
					oneBtOnclick.callBack(tag);
				}

			}
		});

	}

	public static void alert(Context context, String title, String content,
			boolean big) {
		alert(context, title, content, big, null, 0);
	}

}
