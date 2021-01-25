package com.puyu.mobile.sdi.db;

import com.puyu.mobile.sdi.bean.MethodGasConfig;
import com.puyu.mobile.sdi.bean.MethodSave;
import com.puyu.mobile.sdi.bean.MethodSave_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.relation.ToMany;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/24 17:31
 * desc   :
 * version: 1.0
 */
public class DBManager {
    private static DBManager dataManager;

    public synchronized static DBManager getInstance() {
        if (dataManager == null) {
            dataManager = new DBManager();
        }
        return dataManager;
    }

    private <T> long put(Class<T> tClass, T entitie) {
        return ObjectBox.get().boxFor(tClass).put(entitie);
    }

    public <T> Box<T> getBox(Class<T> calss) {
        return ObjectBox.get().boxFor(calss);
    }

    private <T> List<T> getAll(Class<T> calss) {
        return ObjectBox.get().boxFor(calss).getAll();
    }


    public long putMethod(MethodSave bean) {
        return put(MethodSave.class, bean);
    }

    public List<MethodSave> getAllMethod() {
        return getBox(MethodSave.class).query().orderDesc(MethodSave_.dbId).build().find();
    }

    public long getAllMethodCount() {
       return getBox(MethodSave.class).count();
    }

    public boolean removeMethod(MethodSave methodSave) {
        ToMany<MethodGasConfig> methodGasConfigs = methodSave.methodGasConfigs;
        getBox(MethodGasConfig.class).remove(methodGasConfigs);
        return getBox(MethodSave.class).remove(methodSave);
    }

}
