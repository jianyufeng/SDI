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


    public SingleLiveEvent<Integer> changePassage = new SingleLiveEvent<>();


    public StandardGasConfigViewModel(@NonNull Application application, StandardGasConfigRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
        List<StandardGas> standardGasesData = new ArrayList<>();
        standardGasesData.add(new StandardGas(liveDataStateBean.diluentNameLiveData, "percent",
                String.valueOf(100), String.valueOf(100),
                String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.diluentName, 0, true, 0)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand1NameLiveData, "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.stand1Name, 1, true, 1)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand2NameLiveData, "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.stand2Name, 2, true, 2)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand3NameLiveData, "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.stand3Name, 3, true, 3)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand4NameLiveData, "ppb", String.valueOf(1000), String.valueOf(20),
                String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.stand4Name, 4, true, 4)));
        standardGasesData.add(new StandardGas(liveDataStateBean.mulDiluentNameLiveData, "ppb", String.valueOf(0), String.valueOf(0),
                String.valueOf(0), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.mulDiluentName, 6, true, 5)));
        standardGasesData.add(new StandardGas(liveDataStateBean.diluent2NameLiveData, "percent", String.valueOf(100),
                String.valueOf(100), String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean(liveDataStateBean.diluent2Name, 0, true, 6)));
        liveDataStateBean.standardGases.setValue(standardGasesData);
    }

    //选中事件
    public void choseWhich(int pos, boolean swicth) {
        changePassage.setValue(pos);
    }


}
