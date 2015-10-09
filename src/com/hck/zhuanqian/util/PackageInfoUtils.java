package com.hck.zhuanqian.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageInfoUtils {
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            String name = getApplicationName(context);
            verCode = context.getPackageManager().getPackageInfo(name, 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return verCode;
    }

    public static String getVersionName(Context context) {
        String verName = "";
        try {
            String name = getApplicationName(context);
            verName = context.getPackageManager().getPackageInfo(name, 0).versionName;
        } catch (NameNotFoundException e) {
        }
        return verName;
    }

    public static String getApplicationName(Context context) {
        return context.getPackageName();
    }
}
