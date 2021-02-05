package com.puyu.mobile.sdi.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/24 17:14
 * desc   :
 * version: 1.0
 */
@Entity
public class LabelSave {
    @Id
    public long dbId;
    public String label;
    private long time;


    public LabelSave() {
    }

    public LabelSave(String gasName, long time) {
        this.time = time;
        this.label = label;
    }
}
