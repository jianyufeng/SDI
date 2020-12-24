package com.puyu.mobile.sdi.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/24 11:20
 * desc   :
 * version: 1.0
 */
@Entity
public class User {
    @Id
    public long id;
    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
