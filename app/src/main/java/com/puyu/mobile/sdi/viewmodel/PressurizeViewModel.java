package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.model.PressurizeRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.util.NumberUtil;

public class PressurizeViewModel extends BaseViewModel<PressurizeRepository> {
    public LiveDataStateBean liveDataStateBean;

    public PressurizeViewModel(@NonNull Application application, PressurizeRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
    }

    //倍数加压点击
    public void multipleClick(int checkedId) {
        switch (checkedId) {
            case R.id.mul_1p5:
                //1.5倍加压
                liveDataStateBean.pressTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 1.5f)
                                + "", 2));
                break;
            case R.id.mul_2:
                liveDataStateBean.pressTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 2f)
                                + "", 2));
                //2倍加压
                break;
            case R.id.mul_3:
                liveDataStateBean.pressTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 3f)
                                + "", 2));
                //3倍加压
                break;
            case R.id.mul_4:
                liveDataStateBean.pressTargetPress.setValue(NumberUtil.format(
                        (liveDataStateBean.systemMonitor.getValue().currentPress * 4f)
                                + "", 2));
                //4倍加压
                break;


        }

    }


}
