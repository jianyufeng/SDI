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
public class LabelSave {
    @Id
    public long dbId;
    public String label;//类型
    public long time; //完成时间
    public String personnel;//配气人员
    public float startPress;//开始压力值
    public float targetPress;//最新压力值
    public int state = 0;//0开始 1完成 2停止
    public String deviceId;//0开始 1完成 2停止
    @Backlink(to = "labelSaveToOne")
    public ToMany<LabelGasVal> labelGasVals;//标气浓度


    public LabelSave() {
    }

    public LabelSave(String label, long time, String personnel, float startPress, float targetPress,String deviceId) {
        this.label = label;
        this.time = time;
        this.personnel = personnel;
        this.startPress = startPress;
        this.targetPress = targetPress;
        this.deviceId = deviceId;
    }
}
