package com.puyu.mobile.sdi.viewmodel;


import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.RecSystemMonitor;
import com.puyu.mobile.sdi.model.AddSampRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.view.DialogOption;
import com.puyu.mobile.sdi.mvvm.view.QMUITipDialog;
import com.puyu.mobile.sdi.util.NumberUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddSampViewModel extends BaseViewModel<AddSampRepository> {
    public LiveDataStateBean liveDataStateBean;
    public ObservableField<Byte> runPassage = new ObservableField<Byte>((byte) -1);

    public AddSampViewModel(@NonNull Application application, AddSampRepository model) {
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
    //点击选中切换通达
    @SuppressLint("NonConstantResourceId")
    public void addWhicSamp(int checkedId) {
        switch (checkedId) {
            case R.id.pas_stand1:
                liveDataStateBean.addSampPressOpen.setValue(LiveDataStateBean.stand1PassNumber);
                break;
            case R.id.pas_stand2:
                liveDataStateBean.addSampPressOpen.setValue(LiveDataStateBean.stand2PassNumber);
                break;
            case R.id.pas_stand3:
                liveDataStateBean.addSampPressOpen.setValue(LiveDataStateBean.stand3PassNumber);
                break;
            case R.id.pas_stand4:
                liveDataStateBean.addSampPressOpen.setValue(LiveDataStateBean.stand4PassNumber);
                break;
            case R.id.pas_diluent2:
                liveDataStateBean.addSampPressOpen.setValue(LiveDataStateBean.mulDiluentPassNumber);
                break;
        }

    }

    //倍数加压点击
    @SuppressLint("NonConstantResourceId")
    public void multipleClick(int checkedId) {
        RecSystemMonitor value = liveDataStateBean.systemMonitor.getValue();
        if (value==null){
            delayShowMsgDisDialog("暂未获取仪器状态");
            return;
        }
        switch (checkedId) {
            case R.id.mul_1p5:
                //1.5倍加压
                liveDataStateBean.addSampTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 1.5f)
                                + "", 2));
                break;
            case R.id.mul_2:
                liveDataStateBean.addSampTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 2f)
                                + "", 2));
                //2倍加压
                break;
            case R.id.mul_3:
                liveDataStateBean.addSampTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 3f)
                                + "", 2));
                //3倍加压
                break;
            case R.id.mul_4:
                liveDataStateBean.addSampTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 4f)
                                + "", 2));
                //4倍加压
                break;
        }

    }
}
