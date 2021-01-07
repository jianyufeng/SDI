package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 15:53
 * desc   : wifi通讯状态
 * version: 1.0
 */
public enum LinkStateEnum {
    LinkStart("开始连接..."),
    LinkFail("连接失败 重新连接..."),
    LinkSuccess("连接成功"),
    LinkDisConnect("连接断开 重新连接...");

    public String value;

    LinkStateEnum(String value) {
        this.value = value;
    }
}
