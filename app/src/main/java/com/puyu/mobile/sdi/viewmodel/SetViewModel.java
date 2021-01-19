package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.model.SetRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingAction;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.netty.SenDataUtil;
import com.puyu.mobile.sdi.util.NumberUtil;

public class SetViewModel extends BaseViewModel<SetRepository> {
    public LiveDataStateBean liveDataStateBean;
    //常压的值
    public SingleLiveEvent<String> norPressVal = new SingleLiveEvent<>();

    //压力上下限设置
    public SingleLiveEvent<String> pressUp = new SingleLiveEvent<>();//修改上限
    public SingleLiveEvent<String> pressLow = new SingleLiveEvent<>();//修改下限

    public SetViewModel(@NonNull Application application, SetRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
    }

    //启动按钮的点击事件
    public BindingCommand changePressLimit = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


        }
    });
    //登录按钮的点击事件
    public BindingCommand<String> checkPress = new BindingCommand<>(new BindingConsumer<String>() {

        @Override
        public void call(String val) {
            Float aFloat = NumberUtil.parseFloat(val, -1.0f);
            if (aFloat < 0) {
                showToast("请输入常压");
                return;
            }
            //看是否是空闲可以启动
            SystemMonitor monitor = liveDataStateBean.systemMonitor.getValue();
            if (monitor == null) {
                showToast("请先获取仪器状态");
                return;
            }
            boolean canRun = monitor.runProcess == 0x00;
            if (!canRun) {
                showToast("仪器在正在运行.稍后操作");
                return;
            }
            //0校准
            SenDataUtil.sendCheckPress0(aFloat);
        }
    });
}
