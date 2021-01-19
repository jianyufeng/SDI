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
        if (CollectionUtil.isEmpty(liveDataStateBean.standardGasesRinse)) {
            liveDataStateBean.standardGasesRinse = new ArrayList<>();
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.diluentNameLiveData, new PassageBean(LiveDataStateBean.diluentName, LiveDataStateBean.diluentPassNumber, true, 0)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand1NameLiveData, new PassageBean(LiveDataStateBean.stand1Name, LiveDataStateBean.stand1PassNumber, true, 1)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand2NameLiveData, new PassageBean(LiveDataStateBean.stand2Name, LiveDataStateBean.stand2PassNumber, true, 2)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand3NameLiveData, new PassageBean(LiveDataStateBean.stand3Name, LiveDataStateBean.stand3PassNumber, true, 3)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.stand4NameLiveData, new PassageBean(LiveDataStateBean.stand4Name, LiveDataStateBean.stand4PassNumber, true, 4)));
            liveDataStateBean.standardGasesRinse.add(new StandardGas(liveDataStateBean.diluent2NameLiveData, new PassageBean(LiveDataStateBean.diluent2Name, LiveDataStateBean.diluent2PassNumber, true, 5)));
        }
    }
}
