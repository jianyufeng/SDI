package com.puyu.mobile.sdi.bean;

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
    public boolean selected;
    public int index;

    public PassageBean(String name, int prassage, boolean selected, int index) {
        this.name = name;
        this.prassage = prassage;
        this.selected = selected;
        this.index = index;
    }
}
