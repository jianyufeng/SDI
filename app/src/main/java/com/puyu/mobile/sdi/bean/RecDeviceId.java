package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 9:25
 * desc   : 仪器ID
 * version: 1.0
 */
public class RecDeviceId {
    public String deviceId ; //仪器ID
    public int wResult; //写入返回

    public RecDeviceId() {
    }

    public RecDeviceId(String deviceId) {
        this.deviceId=deviceId;
    }

    @Override
    public String toString() {
        return "DeviceId{" +
                "deviceId='" + deviceId + '\'' +
                ", wResult=" + wResult +
                '}';
    }
}
