package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.model.StandardGasConfigRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class StandardGasConfigViewModel extends BaseViewModel<StandardGasConfigRepository> {
    public final LiveDataStateBean liveDataStateBean;

    public SingleLiveEvent<List<StandardGas>> standardGases = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> changePassage = new SingleLiveEvent<>();

    public StandardGasConfigViewModel(@NonNull Application application, StandardGasConfigRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
        List<StandardGas> standardGasesData = new ArrayList<>();
        standardGasesData.add(new StandardGas("稀释气", "percent",
                String.valueOf(100), String.valueOf(100),
                String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean("稀释气", 0, true, 0)));
        standardGasesData.add(new StandardGas("标气1", "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气1", 1, true, 1)));
        standardGasesData.add(new StandardGas("标气2", "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气2", 2, true, 2)));
        standardGasesData.add(new StandardGas("标气3", "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气3", 3, true, 3)));
        standardGasesData.add(new StandardGas("标气4", "ppb", String.valueOf(1000), String.valueOf(20),
                String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气4", 4, true, 4)));
        standardGasesData.add(new StandardGas("多级稀释气", "", String.valueOf(0), String.valueOf(0),
                String.valueOf(0), String.valueOf(0), String.valueOf(0),
                new PassageBean("多级稀释气", 6, true, 5)));
        standardGasesData.add(new StandardGas("二级稀释气", "percent", String.valueOf(100),
                String.valueOf(100), String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean("二级稀释气", 0, true, 0)));
        standardGases.setValue(standardGasesData);

    }

    //选中事件
    public void choeckWhich() {
        changePassage.setValue(true);
    }


    private void getUserMsg() {
       /* model.getUserMsg()
                .subscribe(new HttpObserver<UserData>() {
                    @Override
                    public void onNext(UserData userData) {
                        super.onNext(userData);
                        model.setUserData(userData);
                        startActivity(MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });*/
    }


}
