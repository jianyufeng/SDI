package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/24 14:09
 * desc   : 标气配置
 * version: 1.0
 */
public class StandardGas {
    public String gasName;
    public String gasUnit;
    public float initVal;
    public float targetVal;
    public float dilutionMul;
    public float deviation;
    public boolean enable;
    public long consumeTime;


    public StandardGas() {
    }

    public StandardGas(String gasName, String gasUnit, float initVal, float targetVal,
                       float dilutionMul, float deviation, boolean enable,
                       long consumeTime ) {
        this.gasName = gasName;
        this.gasUnit = gasUnit;
        this.initVal = initVal;
        this.targetVal = targetVal;
        this.dilutionMul = dilutionMul;
        this.deviation = deviation;
        this.enable = enable;
        this.consumeTime = consumeTime;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getGasUnit() {
        return gasUnit;
    }

    public void setGasUnit(String gasUnit) {
        this.gasUnit = gasUnit;
    }

    public float getInitVal() {
        return initVal;
    }

    public void setInitVal(float initVal) {
        this.initVal = initVal;
    }

    public float getTargetVal() {
        return targetVal;
    }

    public void setTargetVal(float targetVal) {
        this.targetVal = targetVal;
    }

    public float getDilutionMul() {
        return dilutionMul;
    }

    public void setDilutionMul(float dilutionMul) {
        this.dilutionMul = dilutionMul;
    }

    public float getDeviation() {
        return deviation;
    }

    public void setDeviation(float deviation) {
        this.deviation = deviation;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(long consumeTime) {
        this.consumeTime = consumeTime;
    }
}
