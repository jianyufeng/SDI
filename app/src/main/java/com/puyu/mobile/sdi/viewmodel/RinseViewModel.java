package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.model.RinseRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.util.CollectionUtil;

import java.util.ArrayList;

public class RinseViewModel extends BaseViewModel<RinseRepository> {

    public LiveDataStateBean liveDataStateBean;


    public RinseViewModel(@NonNull Application application, RinseRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
        if (CollectionUtil.isEmpty(liveDataStateBean.standardGasesRinse)){
            liveDataStateBean.standardGasesRinse = new ArrayList<>();
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.diluentNameLiveData, new PassageBean(liveDataStateBean.diluentName, 0, true, 0)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand1NameLiveData, new PassageBean(liveDataStateBean.stand1Name, 1, true, 1)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand2NameLiveData, new PassageBean(liveDataStateBean.stand2Name, 2, true, 2)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand3NameLiveData, new PassageBean(liveDataStateBean.stand3Name, 3, true, 3)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand4NameLiveData, new PassageBean(liveDataStateBean.stand4Name, 4, true, 4)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.diluent2NameLiveData, new PassageBean(liveDataStateBean.diluent2Name, 0, true, 5)));
        }
    }
}
