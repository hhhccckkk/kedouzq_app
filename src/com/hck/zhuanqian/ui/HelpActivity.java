package com.hck.zhuanqian.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.hck.zhuanqian.R;

public class HelpActivity extends BaseActivity {
	private WebView help;
	private static final String QUN_KEY = "IY93wI9YZa0sVDSOt1mjttZ1gRPh5TV_";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acivity_help);
		initView();
		initTitle("帮助说明");
	}

	private void initView() {
		help = (WebView) findViewById(R.id.help_content);
		help.loadUrl("file:///android_asset/1.html");
	}

	public void jiaqun(View view) {
		joinQQGroup(QUN_KEY);
	}
    /**
     * 调起qq加群
     * @param key
     * @return
     */
	public boolean joinQQGroup(String key) {

		Intent intent = new Intent();

		intent.setData(Uri
				.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D"
						+ key));
		try {
			startActivity(intent);
			return true;

		} catch (Exception e) {

			return false;
		}
	}
}
