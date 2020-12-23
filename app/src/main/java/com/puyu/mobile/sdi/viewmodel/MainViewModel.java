package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;

public class MainViewModel extends BaseViewModel<MainRepository> {
    //tab切换
    public MutableLiveData<Integer> selectType = new MutableLiveData<>(0);
    public MutableLiveData<String> wifiState = new MutableLiveData<>("");

    public MainViewModel(@NonNull Application application, MainRepository model) {
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

    public BindingCommand<Integer> statusCheckListener = new BindingCommand<Integer>(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer integer) {
            switch (integer) {
                case R.id.standard_gas_config:
                    selectType.setValue(0);
                    break;
                case R.id.rinse:
                    selectType.setValue(1);
                    break;
                case R.id.pressurize:
                    selectType.setValue(2);
                    break;
                case R.id.add_sample:
                    selectType.setValue(3);
                    break;
                case R.id.set:
                    selectType.setValue(4);
                    break;
            }
        }
    });
}
