package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.model.StandardGasConfigRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;

public class StandardGasConfigViewModel extends BaseViewModel<StandardGasConfigRepository> {
    public final LiveDataStateBean liveDataStateBean;



    public SingleLiveEvent<Integer> changePassage = new SingleLiveEvent<>();

    public StandardGasConfigViewModel(@NonNull Application application, StandardGasConfigRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();

    }

    //选中事件
    public void choseWhich(int pos, boolean swicth) {
        changePassage.setValue(pos);
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
