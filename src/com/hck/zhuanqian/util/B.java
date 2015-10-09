package com.hck.zhuanqian.util;

import com.hck.zhuanqian.net.Urls;

public class B {

   public static void a(String a){
       LogUtil.D("aaa: "+a);
       Urls.MAIN_HOST_URL=a+"/";
   }

}
