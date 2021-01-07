package com.puyu.mobile.sdi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/7/9 13:44
 * desc   :
 * version: 1.0
 */
public class StringUtil {
    /**
     * 字符串判空
     *
     * @param src
     * @return
     */
    public static boolean isEmpty(String src) {
        return src == null || "".equals(src.trim()) || "null".equalsIgnoreCase(src) || "<null>".equalsIgnoreCase(src);
    }

    public static int getStringNumberIndex(String res, String rule, boolean last) {
        int index;
        if (last) {
            index = res.lastIndexOf(rule);
        } else {
            index = res.indexOf(rule);
        }
        if (index < 0) return -1;
        String substring = res.substring(index);
        if (NumberUtil.isInteger(substring)) {
            return index;
        }
        return -1;
    }

    public static List<String> strToArray(String s, String regex) {
        ArrayList<String> res = new ArrayList<>();
        if ( StringUtil.isEmpty(s)) {
            return res;
        }
        String[] split = s.split(regex);
        res.addAll(Arrays.asList(split));
        return res;
    }

    public static String ArrayToStr(List<String> res, String regex) {
        if (CollectionUtil.isEmpty(res)) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (String re : res) {
            s.append(re);
        }
        return s.toString();
    }
}
