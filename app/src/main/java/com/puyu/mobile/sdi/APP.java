package com.puyu.mobile.sdi;

import android.app.Application;

import com.puyu.mobile.sdi.db.ObjectBox;
import com.puyu.mobile.sdi.mvvm.view.ToastInstance;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/14 10:36
 * desc   :
 * version: 1.0
 */
public class APP extends Application {
    private static APP app;
    public static APP getInstance(){
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        app =this;
        init(this);
        //初始化数据库操作
        ObjectBox.init(this);
        ToastInstance.CreateInstance(this);

    }

    //初始化
    private static void init(Application application) {
        /*new PgyerSDKManager.InitSdk()
                .setContext(application) //设置上下问对象
                .enable(FeatureEnum.CHECK_UPDATE)  //添加检查新版本
                .build();*/
    }
}
