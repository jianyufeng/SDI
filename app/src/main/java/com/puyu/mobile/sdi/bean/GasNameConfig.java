package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 9:25
 * desc   : 配气名称写入
 * version: 1.0
 */
public class GasNameConfig {
    public String sGasName1;
    public String sGasName2;
    public String sGasName3;
    public String sGasName4;


    private int wResult; //写入返回

    public GasNameConfig() {
    }

    public GasNameConfig(String sGasName1, String sGasName2, String sGasName3, String sGasName4) {
        this.sGasName1 = sGasName1;
        this.sGasName2 = sGasName2;
        this.sGasName3 = sGasName3;
        this.sGasName4 = sGasName4;
    }

    public int getwResult() {
        return wResult;
    }

    public void setwResult(int wResult) {
        this.wResult = wResult;
    }
}
