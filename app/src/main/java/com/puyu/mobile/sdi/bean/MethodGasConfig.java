package com.puyu.mobile.sdi.bean;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/24 17:16
 * desc   :
 * version: 1.0
 */
@Entity
public class MethodGasConfig {
    @Id
    public long dbId;
    public String gasName;
    public float initVal;
    public float targetVal;
    public String unit;

    public String passName;
    public int passNumber;
    public boolean passSwitch;
    public ToOne<MethodSave> methodSaveToOne; //匹配的牌号结果数据


    public MethodGasConfig() {
    }

    public MethodGasConfig(String gasName, float initVal, float targetVal, String unit,
                           String passName, int passNumber, boolean passSwitch) {
        this.gasName = gasName;
        this.initVal = initVal;
        this.targetVal = targetVal;
        this.unit = unit;
        this.passName = passName;
        this.passNumber = passNumber;
        this.passSwitch = passSwitch;
    }
}
