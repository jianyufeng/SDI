package com.puyu.mobile.sdi.bean;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/24 17:16
 * desc   :
 * version: 1.0
 */
public class MethodConfig {
    @Id
    public long dbId;
    public String gasName;



    @Backlink(to = "methodConfigs")
    public ToOne<MethodSave> methodSaveToOne;



}
