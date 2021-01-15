package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.GasNameConfig;
import com.puyu.mobile.sdi.bean.StandConfigSend;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingAction;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.netty.SenDataUtil;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.List;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 9:25
 * desc   : 主页面的viewModel
 * version: 1.0
 */
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
            //如果离线 不能启动
          /*  if (liveDataStateBean.wifiState.getValue() != LinkStateEnum.LinkSuccess) {
                showWaitingDialog(new DialogOption("未连接仪器", QMUITipDialog.Builder.ICON_TYPE_FAIL));
                Observable.timer(3, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                    dismissDialog();
                });
                return;
            }*/

            Integer checkedId = selectType.getValue();
            switch (checkedId) {
                case R.id.standard_gas_config://配气启动
                    //1.获取标气名称
                    List<StandardGas> gasList = liveDataStateBean.standardGases.getValue();
                    String name1 = gasList.get(1).gasName;
                    String name2 = gasList.get(2).gasName;
                    String name3 = gasList.get(3).gasName;
                    String name4 = gasList.get(4).gasName;
                    System.out.println("标气1名称：" + name1);
                    System.out.println("标气2名称：" + name2);
                    System.out.println("标气3名称：" + name3);
                    System.out.println("标气4名称：" + name4);
                    //2、校验标气名称
                    if (StringUtil.isEmpty(name1)) {
                        showToast(gasList.get(1).passageBean.name + "：气体名称为空");
                        return;
                    }
                    if (StringUtil.isEmpty(name2)) {
                        showToast(gasList.get(2).passageBean.name + "：气体名称为空");

                        return;
                    }
                    if (StringUtil.isEmpty(name3)) {
                        showToast(gasList.get(3).passageBean.name + "：气体名称为空");
                        return;
                    }
                    if (StringUtil.isEmpty(name4)) {
                        showToast(gasList.get(4).passageBean.name + "：气体名称为空");
                        return;
                    }
                    byte[] bytes = name1.getBytes();
                    if (bytes.length > 20) {
                        showToast("标气1名称过长");
                        showToast(gasList.get(1).passageBean.name + "：气体名称过长");
                        return;
                    }
                    if (name2.getBytes().length > 20) {
                        showToast(gasList.get(2).passageBean.name + "：气体名称过长");
                        return;
                    }
                    if (name3.getBytes().length > 20) {
                        showToast(gasList.get(3).passageBean.name + "：气体名称过长");
                        return;
                    }
                    if (name4.getBytes().length > 20) {
                        showToast(gasList.get(4).passageBean.name + "：气体名称过长");
                        return;
                    }
                    GasNameConfig gasNameConfig = new GasNameConfig(name1, name2, name3, name4);
                    //发送配气方法名称设置
                    SenDataUtil.sendGasName(gasNameConfig);
                    //看是否是空闲可以启动
                    SystemMonitor monitor = liveDataStateBean.systemMonitor.getValue();
                    if (monitor == null) {
                        showToast("请先获取仪器状态");
                        return;
                    }
                    boolean start = monitor.runProcess == 0x00;
                    //通道开关
                    boolean pass1 = gasList.get(1).passageBean.selected;
                    boolean pass2 = gasList.get(2).passageBean.selected;
                    boolean pass3 = gasList.get(3).passageBean.selected;
                    boolean pass4 = gasList.get(4).passageBean.selected;
                    boolean pass5 = gasList.get(5).passageBean.selected;
                    //通道初始值
                    String initV1 = gasList.get(1).initVal;
                    String initV2 = gasList.get(2).initVal;
                    String initV3 = gasList.get(3).initVal;
                    String initV4 = gasList.get(4).initVal;
                    String initV5 = gasList.get(5).initVal;
                    //通道目标值
                    String targetV1 = gasList.get(1).targetVal;
                    String targetV2 = gasList.get(2).targetVal;
                    String targetV3 = gasList.get(3).targetVal;
                    String targetV4 = gasList.get(4).targetVal;
                    String targetV5 = gasList.get(5).targetVal;
                    if (pass1 && NumberUtil.parseFloat(initV1) < NumberUtil.parseFloat(targetV1)) {
                        showToast(gasList.get(1).passageBean.name + "：初始值小于目标值");
                        return;
                    }
                    if (pass2 && NumberUtil.parseFloat(initV2) < NumberUtil.parseFloat(targetV2)) {
                        showToast(gasList.get(2).passageBean.name + "：初始值小于目标值");
                        return;
                    }
                    if (pass3 && NumberUtil.parseFloat(initV3) < NumberUtil.parseFloat(targetV3)) {
                        showToast(gasList.get(3).passageBean.name + "：初始值小于目标值");
                        return;
                    }
                    if (pass4 && NumberUtil.parseFloat(initV4) < NumberUtil.parseFloat(targetV4)) {
                        showToast(gasList.get(4).passageBean.name + "：初始值小于目标值");
                        return;
                    }
                    if (pass5 && NumberUtil.parseFloat(initV5) < NumberUtil.parseFloat(targetV5)) {
                        showToast(gasList.get(5).passageBean.name + "：初始值小于目标值");
                        return;
                    }
                    //目标压力
                    String targetPress = liveDataStateBean.systemMonitor.getValue().targetPress;
                    Float tp = NumberUtil.parseFloat(targetPress, -1.0f);
                    if (tp < 0 || tp > 50) {
                        showToast("总压力值范围0-50psi");
                        return;
                    }
                    //配气方法设置
                    SenDataUtil.sendGasConfig(new StandConfigSend(start, pass1, pass2, pass3
                            , pass4, pass5, initV1, initV2, initV3, initV4, initV5, targetV1,
                            targetV2, targetV3, targetV4, targetV5, targetPress));
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
        }
    });
}
