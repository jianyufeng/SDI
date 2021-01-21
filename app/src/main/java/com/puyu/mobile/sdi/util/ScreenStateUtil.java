package com.puyu.mobile.sdi.util;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

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
            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }else{
            //退出全屏
            WindowManager.LayoutParams layoutParams = ((Activity)context).getWindow().getAttributes();
            layoutParams.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity)context).getWindow().setAttributes(layoutParams);
            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }
}
