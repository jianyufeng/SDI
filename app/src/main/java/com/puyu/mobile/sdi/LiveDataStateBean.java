package com.puyu.mobile.sdi;

import com.puyu.mobile.sdi.bean.DeviceId;
import com.puyu.mobile.sdi.bean.DeviceMCUVersion;
import com.puyu.mobile.sdi.bean.DeviceType;
import com.puyu.mobile.sdi.bean.LinkStateEnum;
import com.puyu.mobile.sdi.bean.PressureLimit;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;

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


    private SingleLiveEvent<String> wifiState = new SingleLiveEvent<>("");

    public SingleLiveEvent<LinkStateEnum> wifiState1 = new SingleLiveEvent<>(LinkStateEnum.LinkStart);

    public SingleLiveEvent<DeviceId> deviceIdLiveData = new SingleLiveEvent<>();//仪器ID
    public SingleLiveEvent<DeviceMCUVersion> deviceVersion = new SingleLiveEvent<>();//仪器MCU版本
    public SingleLiveEvent<DeviceType> deviceType = new SingleLiveEvent<>();//仪器类型

    public SingleLiveEvent<PressureLimit> pressureLimit = new SingleLiveEvent<>();//压力上下限

    public SingleLiveEvent<SystemMonitor> systemMonitor = new SingleLiveEvent<>();//系统监控


    //发数据标气名称
    public SingleLiveEvent<String> stand1GasName = new SingleLiveEvent<>("");
    public SingleLiveEvent<String> stand2GasName = new SingleLiveEvent<>("");
    public SingleLiveEvent<String> stand3GasName = new SingleLiveEvent<>("");
    public SingleLiveEvent<String> stand4GasName = new SingleLiveEvent<>("");

    //发数据标气通道开关
    public SingleLiveEvent<Boolean> pass1Swich = new SingleLiveEvent<>(false);
    public SingleLiveEvent<Boolean> pass2Swich = new SingleLiveEvent<>(false);
    public SingleLiveEvent<Boolean> pass3Swich = new SingleLiveEvent<>(false);
    public SingleLiveEvent<Boolean> pass4Swich = new SingleLiveEvent<>(false);


    public static LiveDataStateBean getInstant() {
        return dataStateBean;
    }

    public SingleLiveEvent<String> getWifiState() {
        return wifiState;
    }
}
