package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 10:23
 * desc   : 压力校准
 * version: 1.0
 */
public class RecPressCalibration {
    private int wResult; //写入返回

    public RecPressCalibration() {
    }

    public int getwResult() {
        return wResult;
    }

    public void setwResult(int wResult) {
        this.wResult = wResult;
    }
}

