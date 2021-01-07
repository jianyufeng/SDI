package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 15:53
 * desc   : 系统状态
 * version: 1.0
 */
public enum SysStateEnum {
    SysNormal("正常"),
    SysAlarm("报警"),
    SysErr("出错");
    public String value;

    SysStateEnum(String value) {
        this.value = value;
    }
}
