package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 10:23
 * desc   : 压力上下限
 * version: 1.0
 */
public class RecPressureLimit {
    public int wResult; //写入返回

    public float upLimit; //压力上限
    public float lowLimit; //压力下限
    public RecPressureLimit() {
    }

    public RecPressureLimit(float upLimit, float lowLimit) {
        this.upLimit = upLimit;
        this.lowLimit = lowLimit;
    }

    @Override
    public String toString() {
        return "PressureLimit{" +
                "wResult=" + wResult +
                ", upLimit=" + upLimit +
                ", lowLimit=" + lowLimit +
                '}';
    }
}

