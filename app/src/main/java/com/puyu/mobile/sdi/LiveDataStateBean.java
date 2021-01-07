package com.puyu.mobile.sdi;

import androidx.lifecycle.MutableLiveData;

import com.puyu.mobile.sdi.bean.DeviceId;
import com.puyu.mobile.sdi.bean.DeviceMCUVersion;
import com.puyu.mobile.sdi.bean.DeviceType;
import com.puyu.mobile.sdi.bean.LinkStateEnum;
import com.puyu.mobile.sdi.bean.PressureLimit;
import com.puyu.mobile.sdi.bean.SystemMonitor;

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

    public MutableLiveData<LinkStateEnum> wifiState1 = new MutableLiveData<>(LinkStateEnum.LinkStart);

    public MutableLiveData<DeviceId> deviceIdLiveData = new MutableLiveData<>();//仪器ID
    public MutableLiveData<DeviceMCUVersion> deviceVersion = new MutableLiveData<>();//仪器MCU版本
    public MutableLiveData<DeviceType> deviceType = new MutableLiveData<>();//仪器类型

    public MutableLiveData<PressureLimit> pressureLimit = new MutableLiveData<>();//压力上下限

    public MutableLiveData<SystemMonitor> systemMonitor = new MutableLiveData<>();//系统监控

    public static LiveDataStateBean getInstant() {
        return dataStateBean;
    }

    public MutableLiveData<String> getWifiState() {
        return wifiState;
    }
}
