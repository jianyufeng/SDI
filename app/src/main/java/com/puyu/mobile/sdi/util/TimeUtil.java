package com.puyu.mobile.sdi.util;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/8 17:15
 * desc   :
 * version: 1.0
 */
public class TimeUtil {
    public static String getDate(float date1) {
        int date = (int) date1;
        if (date < 60) {
            return date + "秒";
        } else if (date > 60 && date < 3600) {
            int m = date / 60;
            int s = date % 60;
            return m + "分" + s + "秒";
        } else {
            int h = date / 3600;
            int m = (date % 3600) / 60;
            int s = (date % 3600) % 60;
            return h + "时" + m + "分" + s + "秒";
        }
    }
}
