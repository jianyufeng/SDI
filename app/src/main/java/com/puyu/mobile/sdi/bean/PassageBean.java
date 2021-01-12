package com.puyu.mobile.sdi.bean;

import androidx.lifecycle.MutableLiveData;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/17 11:09
 * desc   : 通道
 * version: 1.0
 */

public class PassageBean {


    public String name;
    public int prassage;

    public MutableLiveData<Boolean> pass1Swich;
    public PassageBean(String name, int prassage, MutableLiveData<Boolean> pass1Swich) {
        this.name = name;
        this.prassage = prassage;
        this.pass1Swich = pass1Swich;
    }




}
