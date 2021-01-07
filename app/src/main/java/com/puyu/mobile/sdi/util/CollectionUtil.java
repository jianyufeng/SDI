package com.puyu.mobile.sdi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/6/24 11:19
 * desc   :
 * version: 1.0
 */
public class CollectionUtil {
    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }

    public static <T> boolean isEmpty(T[] c) {
        return null == c || c.length == 0;
    }

    public static boolean isEmpty(long[] c) {
        return null == c || c.length == 0;
    }

    public static List<Long> getLongList(long[] c) {
        if (isEmpty(c)) return null;
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < c.length; i++) {
            res.add(c[i]);
        }
        return res;
    }

    public static <E> List<E> deepCopy(List<E> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<E> dest = (List<E>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }
}
