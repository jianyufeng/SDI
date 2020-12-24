package com.puyu.mobile.sdi.db;

import android.content.Context;
import android.util.Log;

import com.puyu.mobile.sdi.BuildConfig;
import com.puyu.mobile.sdi.bean.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/6/7 0:23
 * desc   :
 * version: 1.0
 */
public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
        if (BuildConfig.DEBUG) {//开启浏览器访问ObjectBox
            new AndroidObjectBrowser(boxStore).start(context);
        }
        Log.d("App===", "Using ObjectBox :" + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
    }

    public static BoxStore get() {
        return boxStore;
    }
}
