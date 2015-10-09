package com.hck.zhuanqian.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;

import com.hck.kedouzq.R;

public class FileUtil {

	public static String readFile(Context context) {
		  Resources res = context.getResources();
		  InputStream in = null;
		  BufferedReader br = null;
		  StringBuffer sb = new StringBuffer();
		  sb.append("");
		  try {
		   in = res.openRawResource(R.raw.id);
		   String str;
		   br = new BufferedReader(new InputStreamReader(in, "GBK"));
		   while ((str = br.readLine()) != null) {
		    sb.append(str);
		    sb.append("\n");
		   }
		  } catch (Exception e) {
		   e.printStackTrace();
		   LogUtil.D("eee: "+e.toString());
		  }
		  finally {
		   try {
		    if (in != null) {
		     in.close();
		    }
		    if (br != null) {
		     br.close();
		    }
		   } catch (IOException e) {
		    e.printStackTrace();
		   }

		  }
		  return sb.toString();
		}
	
	/**
	* 实现文本复制功能
	* add by wangqianzhou
	* @param content
	*/
	public static void copy(String content, Context context)
	{
	// 得到剪贴板管理器
	ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
	cmb.setText(content.trim());
	}
	/**
	* 实现粘贴功能
	* add by wangqianzhou
	* @param context
	* @return
	*/
	public static String paste(Context context)
	{
	// 得到剪贴板管理器
	ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
	return cmb.getText().toString().trim();
	}


}
