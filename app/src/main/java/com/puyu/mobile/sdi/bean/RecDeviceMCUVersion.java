package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 9:25
 * desc   : MCU 版本
 * version: 1.0
 */
public class RecDeviceMCUVersion {
    public String deviceVersion; //仪器版本

    public RecDeviceMCUVersion() {
    }

    public RecDeviceMCUVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
}
