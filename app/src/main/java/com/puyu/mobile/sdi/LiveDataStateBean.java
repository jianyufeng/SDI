package com.puyu.mobile.sdi;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/26 15:51
 * desc   :
 * version: 1.0
 */
public class LiveDataStateBean {
    public static LiveDataStateBean dataStateBean = new LiveDataStateBean();


    public LinkedBlockingQueue<String> sendData = new LinkedBlockingQueue<String>();


    private MutableLiveData<String> wifiState = new MutableLiveData<>("");

    public static LiveDataStateBean getInstant() {
        return dataStateBean;
    }

    public MutableLiveData<String> getWifiState() {
        return wifiState;
    }
}
