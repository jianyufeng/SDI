package com.puyu.mobile.sdi.bean;

import androidx.databinding.ObservableField;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/24 14:09
 * desc   : 标气配置
 * version: 1.0
 */
public class StandardGas {
    public ObservableField<String> gasName;
    public String gasUnit;
    public String initVal;
    public String targetVal;
    public String dilutionMul;
    public String deviation;
    public String consumeTime;

    public PassageBean passageBean;


    public StandardGas() {
    }

    public StandardGas(String gasName, String gasUnit, String initVal, String targetVal,
                       String dilutionMul, String deviation,
                       String consumeTime, PassageBean passageBean) {
        this.gasName = new ObservableField<>(gasName);
        this.gasUnit = gasUnit;
        this.initVal = initVal;
        this.targetVal = targetVal;
        this.dilutionMul = dilutionMul;
        this.deviation = deviation;
        this.consumeTime = consumeTime;
        this.passageBean = passageBean;
    }
}
