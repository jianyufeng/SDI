package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.model.RinseRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;

public class RinseViewModel extends BaseViewModel<RinseRepository> {
    //tab切换


    public RinseViewModel(@NonNull Application application, RinseRepository model) {
        super(application, model);
        // account.set(this.model.getAccount());
        // password.set(this.model.getPassword());
    }

    //登录按钮的点击事件
 /*   public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });*/


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
