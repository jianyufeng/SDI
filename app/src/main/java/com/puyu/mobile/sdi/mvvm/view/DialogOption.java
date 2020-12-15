package com.puyu.mobile.sdi.mvvm.view;

public class DialogOption {
    public String title;
    @QMUITipDialog.Builder.IconType
    public int type;

    public DialogOption() {

    }

    public DialogOption(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public DialogOption(int type) {
        this.title = "正在获取数据中，请稍后...";
        this.type = type;
    }

}
