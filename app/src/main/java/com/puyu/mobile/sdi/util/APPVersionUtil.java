package com.puyu.mobile.sdi.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/5/23 16:34
 * desc   : 获取 APP 版本信息
 * version: 1.0
 */
public class APPVersionUtil {
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
    //版本名
    public static String getVersionName(Context context, String apkPath) {
        return getPackageInfo(context,apkPath).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    private static PackageInfo getPackageInfo(Context context, String apkPath) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
    //版本号
    public static int getVersionCode(Context context, String apkPath) {
        return getPackageInfo(context,apkPath).versionCode;
    }
    //获取APK图标
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageInfo info = getPackageInfo(context, apkPath);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(context.getPackageManager());
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }

    //获取APK名字
    public static String getApkName(Context context, String apkPath) {
        PackageInfo info = getPackageInfo(context, apkPath);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            try {
                return appInfo.loadLabel(context.getPackageManager()).toString();
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }

    //获取APK包名
    public static String getApkPackageName(Context context, String apkPath) {
        PackageInfo info = getPackageInfo(context, apkPath);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            try {
                return appInfo.packageName;
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }

    //推断APK是否安装
    public static boolean isApkInstalled(Context context, String packagename) {
        PackageManager localPackageManager = context.getPackageManager();
        try {
            PackageInfo localPackageInfo = localPackageManager.getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return false;
        }

    }

    //安装APK
    public static  void installAPK(String apkPath, Context mContext) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    //打开APK
    public static void openAPK(String packagename, Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage(packagename);
        mContext.startActivity(intent);
    }
}
