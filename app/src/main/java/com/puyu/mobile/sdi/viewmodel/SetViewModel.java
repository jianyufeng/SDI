package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.RecSystemMonitor;
import com.puyu.mobile.sdi.bean.WifiLinkStateEnum;
import com.puyu.mobile.sdi.model.SetRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.mvvm.view.DialogOption;
import com.puyu.mobile.sdi.mvvm.view.QMUITipDialog;
import com.puyu.mobile.sdi.netty.SenDataUtil;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SetViewModel extends BaseViewModel<SetRepository> {
    public LiveDataStateBean liveDataStateBean;
    //常压的值
    public SingleLiveEvent<String> norPressVal = new SingleLiveEvent<>();


    public SetViewModel(@NonNull Application application, SetRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
    }

    //延迟自动消失
    public void delayShowMsgDisDialog(String msg) {
        showWaitingDialog(new DialogOption(msg, QMUITipDialog.Builder.ICON_TYPE_FAIL));
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            dismissDialog();
        });
    }

    private boolean check() {
        //如果离线 不能启动
        if (liveDataStateBean.wifiState.getValue() != WifiLinkStateEnum.LinkSuccess) {
            delayShowMsgDisDialog("未连接仪器");
            return false;
        }
        //未收到仪器状态 不能启动
        RecSystemMonitor monitor = liveDataStateBean.systemMonitor.getValue();
        if (monitor == null) {
            delayShowMsgDisDialog("暂未获取仪器状态");
            return false;
        }
        boolean canRun = monitor.runProcess == 0x00;
        if (!canRun) {
            delayShowMsgDisDialog("仪器在正在运行.稍后操作");
            return false;
        }

        return true;
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

            if (!check()) return;
            //显示加载框
            showWaitingDialog(new DialogOption("正在校准", QMUITipDialog.Builder.ICON_TYPE_LOADING));
            //0校准
            SenDataUtil.sendCheckPress0(aFloat);
        }
    });
    //获取限值的点击事件3
    public BindingCommand<String> getPressLimit = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {

            if (!check()) return;
            //显示加载框
            showWaitingDialog(new DialogOption("正在获取", QMUITipDialog.Builder.ICON_TYPE_LOADING));
            //获取限值
            //SenDataUtil.sendGetPressLimit();
            LiveDataStateBean.getInstant().sendData.offer(SenDataUtil.getDeviceLimit);//获取压力上下限
            //liveDataStateBean.pressureLimit.setValue(liveDataStateBean.pressureLimit.getValue());

        }
    });
    //修改限值点击事件
    public BindingCommand<String> changePressLimit = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            //获取限值
            Float upV = NumberUtil.parseFloat(liveDataStateBean.pressUp.getValue(), -1.0f);
            if (upV < 0 || upV > 50) {
                showToast("压力上限范围0~50psia");
                return;
            }
            Float lowV = NumberUtil.parseFloat(liveDataStateBean.pressLow.getValue(), -1.0f);
            if (lowV < 0 || lowV > 1) {
                showToast("压力下限范围0~1psia");
                return;
            }
            if (upV <= lowV) {
                showToast("压力上限大于下限");
                return;
            }
            if (!check()) return;
            //显示加载框
            showWaitingDialog(new DialogOption("正在设置", QMUITipDialog.Builder.ICON_TYPE_LOADING));
            SenDataUtil.sendSetPressLimit(upV, lowV);

        }
    });
    //获取版本的点击事件3
    public BindingCommand<String> getVersion = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            if (!check()) return;
            //显示加载框
            showWaitingDialog(new DialogOption("正在获取", QMUITipDialog.Builder.ICON_TYPE_LOADING));
            //获取版本
            //SenDataUtil.sendGetVersion();
            //liveDataStateBean.deviceVersion.setValue(new RecDeviceMCUVersion("123456"));
            LiveDataStateBean.getInstant().sendData.offer(SenDataUtil.getDeviceVersion);//获取版本


        }
    });
    //获取仪器ID的点击事件
    public BindingCommand<String> getDeviceId = new BindingCommand<String>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            if (!check()) return;
            //显示加载框
            showWaitingDialog(new DialogOption("正在获取", QMUITipDialog.Builder.ICON_TYPE_LOADING));
            //获取ID
            // SenDataUtil.sendGetDeviceID();
            //liveDataStateBean.deviceIdLiveData.setValue(new RecDeviceId("fsdfdsfds5"));
            LiveDataStateBean.getInstant().sendData.offer(SenDataUtil.getDeviceID);//获取ID

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
            if (!check()) return;
            //显示加载框
            showWaitingDialog(new DialogOption("正在设置", QMUITipDialog.Builder.ICON_TYPE_LOADING));
            //设置版本
            SenDataUtil.sendSetDeviceID(deviceId);
        }
    });
}
