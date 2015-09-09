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

	public static void alert(Context context, String btnText, String content,
			boolean big, final OneBtOnclick oneBtOnclick, final int tag) {
		if (aDialog != null && aDialog.isShowing()) {
			aDialog.dismiss();
		}
		AlertDialogs.oneBtOnclick = null;
		AlertDialogs.oneBtOnclick = oneBtOnclick;
		aDialog = new AlertDialog.Builder(context).create();
		aDialog.setCancelable(true);
		final View view = LayoutInflater.from(context).inflate(R.layout.alert,
				null);
		TextView contenTextView = (TextView) view.findViewById(R.id.d_content);
		Button button = (Button) view.findViewById(R.id.d_button);
		button.setText(btnText);
		contenTextView.setText(content);
		try {
			aDialog.show();
		} catch (Exception e) {
		}

		WindowManager.LayoutParams params = aDialog.getWindow().getAttributes();
		
		if (big) {
			params.height = (int) (MyTools.getScreenHeight() * 0.5);
			params.width = (int) (MyTools.getScreenWidth() * 0.95);
		} else {
		    params.width = (int) (MyTools.getScreenWidth() * 0.65); 
	        params.height = (int) (MyTools.getScreenHeight() * 0.35); 
		}
		
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
