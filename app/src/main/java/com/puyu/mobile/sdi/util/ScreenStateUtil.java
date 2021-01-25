package com.puyu.mobile.sdi.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.puyu.mobile.sdi.APP;

import java.lang.reflect.Method;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/21 14:38
 * desc   :
 * version: 1.0
 */
public class ScreenStateUtil {
    //设置全屏  状态栏的现实与隐藏
    public static void fullScreen(Context context, Boolean b){
        if (b){
            //全屏   隐藏状态栏
            WindowManager.LayoutParams layoutParams = ((Activity)context).getWindow().getAttributes();
            layoutParams.flags |=  WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity)context).getWindow().setAttributes(layoutParams);
           // ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }else{
            //退出全屏
            WindowManager.LayoutParams layoutParams = ((Activity)context).getWindow().getAttributes();
            layoutParams.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity)context).getWindow().setAttributes(layoutParams);
            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }


    static float density;
    static int screenWidth, barHeight, vh;

    /**
     * 返回屏幕密度
     *
     * @return
     */
    public static float getDensity() {
        if (density == 0) {
            density = APP.getInstance().getResources().getDisplayMetrics().density;
        }
        return density;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * getDensity() + 0.5f);
    }

    /**
     * 获取宽度
     */
    public static int getWidth() {
        //if (screenWidth == 0) {
            WindowManager wm = (WindowManager) APP.getInstance().getSystemService(Context.WINDOW_SERVICE);
            screenWidth = wm.getDefaultDisplay().getWidth();
        //}
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight() {
        int screenHeight = 0;
        // if (screenHeight == 0) {
        WindowManager wm = (WindowManager) APP.getInstance().getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
        //}
        return screenHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        if (barHeight == 0) {
            int resourceId = APP.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                barHeight = APP.getInstance().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return barHeight;
    }

    /**
     * 获取虚拟功能键高度
     * 如华为手机有虚拟键盘
     *
     * @param
     * @return
     */
    public static int getVirtualBarHeight() {
        if (vh == 0) {
            WindowManager windowManager = (WindowManager) APP.getInstance().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            try {
                @SuppressWarnings("rawtypes")
                Class c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vh;
    }

    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean result = realSize.y != size.y;
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }
}
