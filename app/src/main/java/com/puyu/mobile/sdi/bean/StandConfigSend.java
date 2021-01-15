package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/14 11:41
 * desc   :
 * version: 1.0
 */
public class StandConfigSend {
    //看是否是空闲可以启动
    public boolean start;
    //通道开关
    public boolean pass1;
    public boolean pass2;
    public boolean pass3;
    public boolean pass4;
    public boolean pass5;
    //通道初始值
    public String initV1;
    public String initV2;
    public String initV3;
    public String initV4;
    public String initV5;
    //通道目标值
    public String targetV1;
    public String targetV2;
    public String targetV3;
    public String targetV4;
    public String targetV5;
    //目标压力
    public String targetPress;

    public StandConfigSend(boolean start, boolean pass1, boolean pass2, boolean pass3, boolean pass4, boolean pass5, String initV1, String initV2, String initV3, String initV4, String initV5, String targetV1, String targetV2, String targetV3, String targetV4, String targetV5, String targetPress) {
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
                ", initV1='" + initV1 + '\'' +
                ", initV2='" + initV2 + '\'' +
                ", initV3='" + initV3 + '\'' +
                ", initV4='" + initV4 + '\'' +
                ", initV5='" + initV5 + '\'' +
                ", targetV1='" + targetV1 + '\'' +
                ", targetV2='" + targetV2 + '\'' +
                ", targetV3='" + targetV3 + '\'' +
                ", targetV4='" + targetV4 + '\'' +
                ", targetV5='" + targetV5 + '\'' +
                ", targetPress='" + targetPress + '\'' +
                '}';
    }
}
