package com.puyu.mobile.sdi.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/2/6 16:20
 * desc   :
 * version: 1.0
 */
@Entity
public class LabelGasVal {
    @Id
    public long dbId;
    public String gasName;
    public float initVal; //初识含量
    public float targetVal; //目标含量
    public ToOne<LabelSave> labelSaveToOne; //

    public LabelGasVal() {
    }



    public LabelGasVal(String gasName, float initVal, float targetVal) {
        this.gasName = gasName;
        this.initVal = initVal;
        this.targetVal = targetVal;
    }
}
