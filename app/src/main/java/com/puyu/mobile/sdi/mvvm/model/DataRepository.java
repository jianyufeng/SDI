package com.puyu.mobile.sdi.mvvm.model;

import com.puyu.mobile.sdi.mvvm.BaseModel;


/**
 * Created by ZhuHui
 * 总的数据层，包括Http，SharedPreference，后续有需要可能会加room数据库
 * 作用是在ViewModel层只管接受这里的数据，不管他是来自后台，本地，自己生成的数据
 * 假如后续修改，也更加简单
 * 此类也可以在MVP中充当model的责任，只是大部分数据都是从线上拿的，之前好几个项目都没用
 */
public class DataRepository extends BaseModel {
    private volatile static DataRepository INSTANCE = null;


    public static DataRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository();
                }
            }
        }
        return INSTANCE;
    }

}
