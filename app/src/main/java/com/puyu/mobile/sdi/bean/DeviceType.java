package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 9:25
 * desc   : 仪器类型
 * version: 1.0
 */
public class DeviceType {
    public String deviceType; //仪器类型
    public int wResult; //写入返回

    public DeviceType() {
    }

    public DeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
