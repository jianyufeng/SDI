package com.puyu.mobile.sdi.bean;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/24 17:14
 * desc   :
 * version: 1.0
 */
@Entity
public class MethodSave {
    @Id
    public long dbId;
    public String gasName;

    @Backlink(to = "methodSaveToOne")
    public ToMany<MethodGasConfig> methodGasConfigs;

    public MethodSave() {
    }

    public MethodSave(String gasName) {
        this.gasName = gasName;
    }
}
