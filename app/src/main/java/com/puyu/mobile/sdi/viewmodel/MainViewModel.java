package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.SendGasNameConfig;
import com.puyu.mobile.sdi.bean.SendRinseConfig;
import com.puyu.mobile.sdi.bean.SendStandConfig;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingAction;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.mvvm.view.DialogOption;
import com.puyu.mobile.sdi.mvvm.view.QMUITipDialog;
import com.puyu.mobile.sdi.netty.SenDataUtil;
import com.puyu.mobile.sdi.util.CollectionUtil;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


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
    public SingleLiveEvent<Integer> selectType = new SingleLiveEvent<>(0);


    public MainViewModel(@NonNull Application application, MainRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
    }

    public void delayDisDialog() {
        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            dismissDialog();
        });
    }

    //启动按钮的点击事件
    public BindingCommand startRun = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //如果离线 不能启动
          /*  if (liveDataStateBean.wifiState.getValue() != LinkStateEnum.LinkSuccess) {
                showWaitingDialog(new DialogOption("未连接仪器", QMUITipDialog.Builder.ICON_TYPE_FAIL));
               delayDisDialog();
                return;
            }*/
            //看是否是空闲可以启动
            SystemMonitor monitor = liveDataStateBean.systemMonitor.getValue();
            boolean start = monitor.runProcess == 0x00;
            if (monitor == null) {
                showToast("请先获取仪器状态");
                return;
            }
            Integer checkedId = selectType.getValue();

            switch (checkedId) {
                case R.id.standard_gas_config://配气启动
                    if (liveDataStateBean.systemMonitor.getValue().currentPress > 0.47) {
                        showWaitingDialog(new DialogOption("初始值压力高于0.47psi", QMUITipDialog.Builder.ICON_TYPE_FAIL));
                        delayDisDialog();
                        return;
                    }
                    //1.获取标气名称
                    List<StandardGas> gasList = liveDataStateBean.standardGases.getValue();
                    String name1 = gasList.get(1).gasName.getValue();
                    String name2 = gasList.get(2).gasName.getValue();
                    String name3 = gasList.get(3).gasName.getValue();
                    String name4 = gasList.get(4).gasName.getValue();
                    System.out.println("标气1名称：" + name1);
                    System.out.println("标气2名称：" + name2);
                    System.out.println("标气3名称：" + name3);
                    System.out.println("标气4名称：" + name4);
                    //2、校验标气名称
                    if (StringUtil.isEmpty(name1)) {
                        showToast(gasList.get(1).passageBean.name + "：气体名称为空");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(1).passageBean.index);
                        return;
                    }
                    if (StringUtil.isEmpty(name2)) {
                        showToast(gasList.get(2).passageBean.name + "：气体名称为空");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(2).passageBean.index);
                        return;
                    }
                    if (StringUtil.isEmpty(name3)) {
                        showToast(gasList.get(3).passageBean.name + "：气体名称为空");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(3).passageBean.index);
                        return;
                    }
                    if (StringUtil.isEmpty(name4)) {
                        showToast(gasList.get(4).passageBean.name + "：气体名称为空");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(4).passageBean.index);
                        return;
                    }
                    byte[] bytes = name1.getBytes();
                    if (bytes.length > 20) {
                        showToast("标气1名称过长");
                        showToast(gasList.get(1).passageBean.name + "：气体名称过长");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(1).passageBean.index);
                        return;
                    }
                    if (name2.getBytes().length > 20) {
                        showToast(gasList.get(2).passageBean.name + "：气体名称过长");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(2).passageBean.index);
                        return;
                    }
                    if (name3.getBytes().length > 20) {
                        showToast(gasList.get(3).passageBean.name + "：气体名称过长");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(3).passageBean.index);
                        return;
                    }
                    if (name4.getBytes().length > 20) {
                        showToast(gasList.get(4).passageBean.name + "：气体名称过长");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(4).passageBean.index);
                        return;
                    }
                    SendGasNameConfig gasNameConfig = new SendGasNameConfig(name1, name2, name3, name4);
                    //发送配气方法名称设置
                    SenDataUtil.sendGasName(gasNameConfig);

                    //通道开关
                    boolean pass1 = gasList.get(1).passageBean.selected;
                    boolean pass2 = gasList.get(2).passageBean.selected;
                    boolean pass3 = gasList.get(3).passageBean.selected;
                    boolean pass4 = gasList.get(4).passageBean.selected;
                    boolean pass5 = gasList.get(5).passageBean.selected;
                    //通道初始值
                    float initV1 = NumberUtil.parseFloat(gasList.get(1).initVal);
                    float initV2 = NumberUtil.parseFloat(gasList.get(2).initVal);
                    float initV3 = NumberUtil.parseFloat(gasList.get(3).initVal);
                    float initV4 = NumberUtil.parseFloat(gasList.get(4).initVal);
                    float initV5 = NumberUtil.parseFloat(gasList.get(5).initVal);
                    //通道目标值
                    float targetV1 = NumberUtil.parseFloat(gasList.get(1).targetVal);
                    float targetV2 = NumberUtil.parseFloat(gasList.get(2).targetVal);
                    float targetV3 = NumberUtil.parseFloat(gasList.get(3).targetVal);
                    float targetV4 = NumberUtil.parseFloat(gasList.get(4).targetVal);
                    float targetV5 = NumberUtil.parseFloat(gasList.get(5).targetVal);
                    if (pass1 && (initV1 <= 0 || targetV1 <= 0 || initV1 < targetV1)) {
                        showToast(gasList.get(1).passageBean.name + "：初始值/目标值 有误");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(1).passageBean.index);
                        return;
                    }
                    if (pass2 && (initV2 <= 0 || targetV2 <= 0 || initV2 < targetV2)) {
                        showToast(gasList.get(2).passageBean.name + "：初始值/目标值 有误");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(2).passageBean.index);
                        return;
                    }
                    if (pass3 && (initV3 <= 0 || targetV3 <= 0 || initV3 < targetV3)) {
                        showToast(gasList.get(3).passageBean.name + "：初始值/目标值 有误");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(3).passageBean.index);
                        return;
                    }
                    if (pass4 && (initV4 <= 0 || targetV4 <= 0 || initV4 < targetV4)) {
                        showToast(gasList.get(4).passageBean.name + "：初始值/目标值 有误");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(4).passageBean.index);
                        return;
                    }
                    if (pass5 && (initV5 <= 0 || targetV5 <= 0 || initV5 < targetV5)) {
                        showToast(gasList.get(5).passageBean.name + "：初始值/目标值 有误");
                        liveDataStateBean.showIndexFrag.setValue(gasList.get(5).passageBean.index);
                        return;
                    }
                    //目标压力
                    Float tp = NumberUtil.parseFloat(liveDataStateBean.targetPress.getValue(),
                            -1.0f);
                    if (tp < 0 || tp > 50) {
                        showToast("总压力值范围0-50psi");
                        return;
                    }
                    //配气方法设置
                    SenDataUtil.sendGasConfig(new SendStandConfig(start, pass1, pass2, pass3
                            , pass4, pass5, initV1, initV2, initV3, initV4, initV5, targetV1,
                            targetV2, targetV3, targetV4, targetV5, tp));
                    break;
                case R.id.rinse: //冲洗启动
                    if (CollectionUtil.isEmpty(liveDataStateBean.standardGasesRinse)) {
                        return;
                    }
                    StandardGas diluentGas = liveDataStateBean.standardGasesRinse.get(0);
                    StandardGas standard1Gas = liveDataStateBean.standardGasesRinse.get(1);
                    StandardGas standard2Gas = liveDataStateBean.standardGasesRinse.get(2);
                    StandardGas standard3Gas = liveDataStateBean.standardGasesRinse.get(3);
                    StandardGas standard4Gas = liveDataStateBean.standardGasesRinse.get(4);
                    StandardGas diluent2Gas = liveDataStateBean.standardGasesRinse.get(5);
                    System.out.println(diluentGas.gasName.getValue() + " UUUUUUUUU " + diluentGas.passageBean.selected + "");
                    System.out.println(standard1Gas.gasName.getValue() + " UUUUUUUUU " + standard1Gas.passageBean.selected + "");
                    System.out.println(standard2Gas.gasName.getValue() + " UUUUUUUUU " + standard2Gas.passageBean.selected + "");
                    System.out.println(standard3Gas.gasName.getValue() + " UUUUUUUUU " + standard3Gas.passageBean.selected + "");
                    System.out.println(standard4Gas.gasName.getValue() + " UUUUUUUUU " + standard4Gas.passageBean.selected + "");
                    System.out.println(diluent2Gas.gasName.getValue() + " UUUUUUUUU " + diluent2Gas.passageBean.selected + "");


                    Integer diluentRinseTime = NumberUtil.parseInteger(liveDataStateBean.diluentRinseTime.getValue(), -1);
                    Integer standRinseTime = NumberUtil.parseInteger(liveDataStateBean.standRinseTime.getValue(), -1);
                    if (diluentRinseTime > 120 || diluentRinseTime < 0 || standRinseTime > 120 || standRinseTime < 0) {
                        showToast("冲洗时间范围0~120秒");
                        return;
                    }
                    System.out.println(diluentRinseTime + " UUUUUUUUU " + standRinseTime);

                    //配气方法设置
                    SenDataUtil.sendRinseConfig(new SendRinseConfig(start, standard1Gas.passageBean.selected,
                            standard2Gas.passageBean.selected, standard3Gas.passageBean.selected,
                            standard4Gas.passageBean.selected,diluentRinseTime,
                            standRinseTime,10));
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
