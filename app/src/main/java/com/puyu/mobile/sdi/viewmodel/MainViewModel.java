package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingAction;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;

public class MainViewModel extends BaseViewModel<MainRepository> {

    public final LiveDataStateBean liveDataStateBean;
    //tab切换
    public MutableLiveData<Integer> selectType = new MutableLiveData<>(0);

    public MainViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
    }

    //启动按钮的点击事件
    public BindingCommand startRun = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Integer checkedId = selectType.getValue();
            switch (checkedId) {
                case R.id.standard_gas_config://配气启动
                    //1.获取标气名称
                    String name1 = liveDataStateBean.stand1GasName.getValue();
                    String name2 = liveDataStateBean.stand2GasName.getValue();
                    String name3 = liveDataStateBean.stand3GasName.getValue();
                    String name4 = liveDataStateBean.stand4GasName.getValue();
                    System.out.println("标气1名称：" + name1);
                    System.out.println("标气2名称：" + name2);
                    System.out.println("标气3名称：" + name3);
                    System.out.println("标气4名称：" + name4);


                    break;
                case R.id.rinse: //冲洗启动

                    break;
                case R.id.pressurize: //加压启动

                    break;
                case R.id.add_sample://加样启动

                    break;
            }

        }
    });


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
            selectType.setValue(integer);
            /*switch (integer) {
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
            }*/
        }
    });
}
