package com.hck.zhuanqian.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.hck.kedouzq.R;

public class HelpActivity extends BaseActivity {
	private WebView help;
	private static final String QUN_KEY = "Sfc-0ET0W87iFwsDxEM0CTVD9IjfNU82";

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
	    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
	   // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	    try {
	        startActivity(intent);
	        return true;
	    } catch (Exception e) {
	        // 未安装手Q或安装的版本不支持
	        return false;
	    }
	}

}
