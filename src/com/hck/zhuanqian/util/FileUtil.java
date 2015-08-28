package com.hck.zhuanqian.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;

import com.hck.zhuanqian.R;

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


}
