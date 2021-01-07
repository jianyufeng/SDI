package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 9:25
 * desc   : 仪器ID
 * version: 1.0
 */
public class DeviceId {
    public String deviceId; //仪器ID
    public int wResult; //写入返回

    public DeviceId() {
    }

    public DeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
