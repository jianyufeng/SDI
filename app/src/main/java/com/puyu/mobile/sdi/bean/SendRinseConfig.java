package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/18 20:38
 * desc   :
 * version: 1.0
 */
public class SendRinseConfig {
    //看是否是空闲可以启动
    public boolean start;
    //通道开关
    public boolean pass1;
    public boolean pass2;
    public boolean pass3;
    public boolean pass4;
    public float dRimsetime;//稀释气冲洗时间
    public float sRimsetime;//标气冲洗时间
    public float iimsetime;//冲洗间隔时间

    public SendRinseConfig(boolean start, boolean pass1, boolean pass2, boolean pass3, boolean pass4, float dRimsetime, float sRimsetime, float iimsetime) {
        this.start = start;
        this.pass1 = pass1;
        this.pass2 = pass2;
        this.pass3 = pass3;
        this.pass4 = pass4;
        this.dRimsetime = dRimsetime;
        this.sRimsetime = sRimsetime;
        this.iimsetime = iimsetime;
    }

    @Override
    public String toString() {
        return "SendRinseConfig{" +
                "start=" + start +
                ", pass1=" + pass1 +
                ", pass2=" + pass2 +
                ", pass3=" + pass3 +
                ", pass4=" + pass4 +
                ", dRimsetime=" + dRimsetime +
                ", sRimsetime=" + sRimsetime +
                ", iimsetime=" + iimsetime +
                '}';
    }
}
