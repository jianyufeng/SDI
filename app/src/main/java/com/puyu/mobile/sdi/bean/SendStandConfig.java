package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/14 11:41
 * desc   :
 * version: 1.0
 */
public class SendStandConfig {
    //看是否是空闲可以启动
    public boolean start;
    //通道开关
    public boolean pass1;
    public boolean pass2;
    public boolean pass3;
    public boolean pass4;
    public boolean pass5;
    //通道初始值
    public float initV1;
    public float initV2;
    public float initV3;
    public float initV4;
    public float initV5;
    //通道目标值
    public float targetV1;
    public float targetV2;
    public float targetV3;
    public float targetV4;
    public float targetV5;
    //目标压力
    public float targetPress;

    public SendStandConfig(boolean start, boolean pass1, boolean pass2, boolean pass3, boolean pass4, boolean pass5, float initV1, float initV2, float initV3, float initV4, float initV5, float targetV1, float targetV2, float targetV3, float targetV4, float targetV5, float targetPress) {
        this.start = start;
        this.pass1 = pass1;
        this.pass2 = pass2;
        this.pass3 = pass3;
        this.pass4 = pass4;
        this.pass5 = pass5;
        this.initV1 = initV1;
        this.initV2 = initV2;
        this.initV3 = initV3;
        this.initV4 = initV4;
        this.initV5 = initV5;
        this.targetV1 = targetV1;
        this.targetV2 = targetV2;
        this.targetV3 = targetV3;
        this.targetV4 = targetV4;
        this.targetV5 = targetV5;
        this.targetPress = targetPress;
    }

    @Override
    public String toString() {
        return "StandConfigSend{" +
                "start=" + start +
                ", pass1=" + pass1 +
                ", pass2=" + pass2 +
                ", pass3=" + pass3 +
                ", pass4=" + pass4 +
                ", pass5=" + pass5 +
                ", initV1=" + initV1 +
                ", initV2=" + initV2 +
                ", initV3=" + initV3 +
                ", initV4=" + initV4 +
                ", initV5=" + initV5 +
                ", targetV1=" + targetV1 +
                ", targetV2=" + targetV2 +
                ", targetV3=" + targetV3 +
                ", targetV4=" + targetV4 +
                ", targetV5=" + targetV5 +
                ", targetPress=" + targetPress +
                '}';
    }
}
