package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/17 11:09
 * desc   :
 * version: 1.0
 */
public class PassageBean {
    private String name;
    private int prassage;

    private boolean selected;

    public PassageBean(String name, int prassage, boolean selected) {
        this.name = name;
        this.prassage = prassage;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrassage() {
        return prassage;
    }

    public void setPrassage(int prassage) {
        this.prassage = prassage;
    }



    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
