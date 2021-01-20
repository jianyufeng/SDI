package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.DeviceId;
import com.puyu.mobile.sdi.bean.DeviceMCUVersion;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.model.SetRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.netty.SenDataUtil;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

public class SetViewModel extends BaseViewModel<SetRepository> {
    public LiveDataStateBean liveDataStateBean;
    //常压的值
    public SingleLiveEvent<String> norPressVal = new SingleLiveEvent<>();

    //压力上下限设置
    //修改上限
    public SingleLiveEvent<String> pressUp = new SingleLiveEvent<>();
    //修改下限
    public SingleLiveEvent<String> pressLow = new SingleLiveEvent<>();
    //修改仪器ID
    public SingleLiveEvent<String> changeDeviceID = new SingleLiveEvent<>();

    public SetViewModel(@NonNull Application application, SetRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
    }


    //压力校准点击事件
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
    //获取限值的点击事件3
    public BindingCommand<String> getPressLimit = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            //获取限值
            SenDataUtil.sendGetPressLimit();
            liveDataStateBean.pressureLimit.setValue(liveDataStateBean.pressureLimit.getValue());

        }
    });
    //修改限值点击事件
    public BindingCommand<String> changePressLimit = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            //获取限值
            Float upV = NumberUtil.parseFloat(pressUp.getValue(), -1.0f);
            if (upV < 0 || upV > 50) {
                showToast("压力上限范围0~50psia");
                return;
            }
            Float lowV = NumberUtil.parseFloat(pressLow.getValue(), -1.0f);
            if (lowV < 0 || lowV > 1) {
                showToast("压力下限范围0~1psia");
                return;
            }
            if (upV < lowV) {
                showToast("压力上限大于下限");
                return;
            }
            SenDataUtil.sendSetPressLimit(upV, lowV);

        }
    });
    //获取版本的点击事件3
    public BindingCommand<String> getVersion = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            //获取版本
            SenDataUtil.sendGetVersion();
            liveDataStateBean.deviceVersion.setValue(new DeviceMCUVersion("2.560"));
        }
    });
    //获取仪器ID的点击事件
    public BindingCommand<String> getDeviceId = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            //获取ID
            SenDataUtil.sendGetDeviceID();
            liveDataStateBean.deviceIdLiveData.setValue(new DeviceId("fsdfdsfds5"));

        }
    });
    //设置仪器ID的点击事件
    public BindingCommand<String> setDeviceId = new BindingCommand<>(new BindingConsumer<String>() {
        @Override
        public void call(String deviceId) {
            if (StringUtil.isEmpty(deviceId) || deviceId.getBytes().length > 12) {
                showToast("仪器ID长度有误");
                return;
            }
            //设置版本
            SenDataUtil.sendSetDeviceID(deviceId);
        }
    });
}
