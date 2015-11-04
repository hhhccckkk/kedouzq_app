package com.hck.zhuanqian.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class MyTools {
    private static Context context;
    private static Display display;
    private static String TAG = "MyTools";

    public MyTools(Context context) {
        MyTools.context = context;
    }

    public static int getScreenHeight()
    {
        if (context == null) {
           
            return 800;
        }
        display = ((Activity) context).getWindowManager().getDefaultDisplay();
        return display.getHeight();
    }

    public static int getScreenWidth()
    {
        if (context == null) {
            return 480;
        }
        display = ((Activity) context).getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    public static String getSDK() {
        return android.os.Build.VERSION.SDK; 

    }

    public static String getModel() 
    {
        return android.os.Build.MODEL;
    }

    public static String getRelease() 
    {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getImei(Context context) 
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager!=null) {
            return telephonyManager.getDeviceId();
        }
        return null;
        
    }

    public long totalMemory(int type) 
    {
        Runtime myRun = Runtime.getRuntime();
        if (1 == type) {
            return myRun.totalMemory() / 1024 / 1024; 
        } else if (2 == type) {
            return myRun.maxMemory() / 1024 / 1024;
        } else if (3 == type) { 
            return myRun.freeMemory() / 1024 / 1024;
        }
        return 0;
    }

    public static String getVerName(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;

            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

    public static int getVerCode(Context context) {
        String pkName = context.getPackageName();
        try {
            int versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            return versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isNetworkAvailable(Context context) { // �ж����������Ƿ����
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
            return false;
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return false;
        return (info.getState() == NetworkInfo.State.CONNECTED);
    }

    public static String trim(String str, int limit) { 
        String mStr = str.trim();
        return mStr.length() > limit ? mStr.substring(0, limit) : mStr;
    }

    public static String getTel(Context context) { 
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telManager != null) {
            return telManager.getLine1Number();
        }
        return "";

    }

    public static String getMac(Context context) { // ��ȡʱ��mac�ַ
        final WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info.getMacAddress() != null) {
                return info.getMacAddress().toLowerCase(Locale.ENGLISH).replace(":", "");
            }
            return "";
        }
        return "";
    }

    /**
     * �� ���� ת���� dp
     * 
     * @param pxValue
     *            ����
     * @return dp
     */
    public static int px2dip(int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * �� ���� �D�Q�� sp
     * 
     * @param pixel
     * @return sp
     */
    public static int px2sp(int px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity);
    }

    /**
     * �� dip �D�Q�ɮ��� px
     * 
     * @param dipValue
     *            dip ���ص�ֵ
     * @return ���� px
     */
    public static int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int[][] getViewsPosition(List<View> views) {
        int[][] location = new int[views.size()][2];
        for (int index = 0; index < views.size(); index++) {
            views.get(index).getLocationOnScreen(location[index]);
        }
        return location;
    }

    /**
     * ����һ��view,����һ��int��������� view���ֻ���Ļ�����Ͻǵľ�����
     * 
     * @param views
     *            �����view
     * @return ����int������,location[0]��ʾx,location[1]��ʾy
     */
    public static int[] getViewPosition(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * onTouch����ʱָ����views�е��ĸ�view����
     * 
     * @param event
     *            ontouch�¼�
     * @param views
     *            view list
     * @return view
     */
    public static View getOnTouchedView(MotionEvent event, List<View> views) {
        int[][] location = getViewsPosition(views);
        for (int index = 0; index < views.size(); index++) {
            if (event.getRawX() > location[index][0] && event.getRawX() < location[index][0] + views.get(index).getWidth()
                    && event.getRawY() > location[index][1] && event.getRawY() < location[index][1] + views.get(index).getHeight()) {
                return views.get(index);
            }
        }
        return null;
    }

    /**
     * �������ͼƬ�洢���ֻ���,�����ش洢·��
     * 
     * @param photo
     *            Bitmap ����,�����ͼƬ
     * @return String ���ش洢·��
     */
    public static String savePic(Bitmap photo, String name, String path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // ����һ��
                                                                  // outputstream
                                                                  // ����ȡ�ļ���
        photo.compress(Bitmap.CompressFormat.PNG, 100, baos); // �� bitmap
                                                              // ��ͼƬת����
                                                              // jpge
                                                              // �ĸ�ʽ�����������
        byte[] byteArray = baos.toByteArray();
        String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(saveDir + "/" + path); // ����һ��·��
        if (!dir.exists()) { // ���·��������,����·��
            dir.mkdir();
        }
        File file = new File(saveDir, name + ".png"); // ����һ���ļ�
        if (file.exists())
            file.delete(); // ɾ��ԭ���д������ļ�,ɾ��
        try {
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file); // ͨ�� FileOutputStream �����ļ�
            BufferedOutputStream bos = new BufferedOutputStream(fos); // ͨ�� bos
                                                                      // ���ļ���д����
            bos.write(byteArray);
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    /**
     * ���� bitmap ��Сϵͳռ�õ���Դ���
     */
    public static void destoryBimap(Bitmap photo) {
        if (photo != null && !photo.isRecycled()) {
            photo.recycle();
            photo = null;
        }
    }

    /**
     * ��ݔ���ִ��� md5 ���a
     * 
     * @param s
     *            : ��a���ִ�
     * @return ���a����ִ�, ��ʧ��, ���� ""
     */
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes("UTF-8"));
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                if ((b & 0xFF) < 0x10)
                    hexString.append("0");
                hexString.append(Integer.toHexString(b & 0xFF));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static boolean isNumber(char c) { // �Ƿ�������
        boolean isNumer = false;
        if (c >= '0' && c <= '9') {
            isNumer = true;
        }
        return isNumer;
    }

    public static boolean isEmail(String strEmail) { // �Ƿ�����ȷ�������ַ
        String checkemail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(checkemail);

        Matcher matcher = pattern.matcher(strEmail);

        return matcher.matches();
    }

    public static boolean isNull(String string) // �ַ��Ƿ�Ϊ��
    {
        if (null == string || "".equals(string)) {
            return true;
        }
        return false;
    }

    public static boolean isLenghtOk(String string, int max, int min) // �ַ��ȼ��
    {
        if (null != string) {
            if (string.length() > max || string.length() < min) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLenghtOk(String string, int max) // �ַ����Ƿ�
    {
        if (null != string) {
            if (string.length() > max) {
                return false;
            }
        }
        return true;
    }

    /**
     * ��ȡ��Դ�ļ�
     */
    public static String geFileFromAssets(Context context, String fileName) { // ����Դ�ļ���ȡ���
        if (context == null || MyTools.isNull(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String geFileFromRaw(Context context, int resId) {// ��Raw�����ȡ���
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ʹ��һ���ַ��ȡָ�����ȵ�������ַ�
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }
        StringBuilder str = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    public String getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            if (packageName.equals("com.test.test")) {
                return new String(info.signatures[0].toByteArray());

            }
        }
        return null;
    }
}
