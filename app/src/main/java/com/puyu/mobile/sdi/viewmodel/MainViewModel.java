package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.LabelSave;
import com.puyu.mobile.sdi.bean.RecPressureLimit;
import com.puyu.mobile.sdi.bean.RecSystemMonitor;
import com.puyu.mobile.sdi.bean.SendGasNameConfig;
import com.puyu.mobile.sdi.bean.SendRinseConfig;
import com.puyu.mobile.sdi.bean.SendStandConfig;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.bean.WifiLinkStateEnum;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.mvvm.view.DialogOption;
import com.puyu.mobile.sdi.mvvm.view.QMUITipDialog;
import com.puyu.mobile.sdi.netty.SenDataUtil;
import com.puyu.mobile.sdi.util.CollectionUtil;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.ArrayList;
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

    public SingleLiveEvent<List<LabelSave>> labels = new SingleLiveEvent<>();

    public MainViewModel(@NonNull Application application, MainRepository model) {
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

    //启动按钮的点击事件  打印标签
    public BindingCommand<String> printLabel = new BindingCommand<>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            labels.setValue(new ArrayList<>());
        }
    });

    //启动按钮的点击事件
    public BindingCommand<String> startRun = new BindingCommand<>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            //如果离线 不能启动
            if (liveDataStateBean.wifiState.getValue() != WifiLinkStateEnum.LinkSuccess) {
                delayShowMsgDisDialog("未连接仪器");
                return;
            }
            //未收到仪器状态 不能启动
            RecSystemMonitor monitor = liveDataStateBean.systemMonitor.getValue();
            if (monitor == null) {
                delayShowMsgDisDialog("暂未获取仪器状态");
                return;
            }
            boolean start = monitor.runProcess == 0x00;//空闲状态可以启动

            //获取当前显示 点击的页面  获取启动的类型
            Integer checkedId = liveDataStateBean.selectType.getValue();

            switch (checkedId) {
                case R.id.standard_gas_config://配气启动
                    //1.获取标气名称
                    List<StandardGas> gasList = liveDataStateBean.standardGases.getValue();
                    //通道开关
                    boolean pass1 = gasList.get(1).passageBean.selected;
                    boolean pass2 = gasList.get(2).passageBean.selected;
                    boolean pass3 = gasList.get(3).passageBean.selected;
                    boolean pass4 = gasList.get(4).passageBean.selected;
                    boolean pass5 = gasList.get(5).passageBean.selected;

                    if (start) {//如果是启动 //启动检查启动参数 并且发送标气名称设置
                        if (liveDataStateBean.systemMonitor.getValue().currentPress > 0.47) {
                            delayShowMsgDisDialog("初始值压力高于0.47psi");
                            return;
                        }
                        if (!(pass1 || pass2 || pass3 || pass4 || pass5)) {
                            delayShowMsgDisDialog("至少打开一路标气开关");
                            return;
                        }
                        String name1 = gasList.get(1).gasName.getValue();
                        String name2 = gasList.get(2).gasName.getValue();
                        String name3 = gasList.get(3).gasName.getValue();
                        String name4 = gasList.get(4).gasName.getValue();
                        System.out.println("标气1名称：" + name1);
                        System.out.println("标气2名称：" + name2);
                        System.out.println("标气3名称：" + name3);
                        System.out.println("标气4名称：" + name4);
                        for (int i = 1; i < 6; i++) {
                            String name = gasList.get(i).gasName.getValue();
                            //2、校验标气名称
                            if (StringUtil.isEmpty(name)) {
                                liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                                delayShowMsgDisDialog(gasList.get(i).passageBean.name + "：气体名称为空");
                                return;
                            }
                            //3、校验标气名称长度最长20个字节
                            if (name.getBytes().length > 20) {
                                liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                                delayShowMsgDisDialog(gasList.get(i).passageBean.name + "：气体名称过长");
                                return;
                            }

                            //通道初始值
                            float initV = NumberUtil.parseFloat(gasList.get(i).initVal);
                            //通道目标值
                            float targetV = NumberUtil.parseFloat(gasList.get(i).targetVal);
                            if ((initV <= 0 || targetV <= 0 || initV < targetV)) {
                                liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                                delayShowMsgDisDialog(gasList.get(i).passageBean.name + "：初始值/目标值 有误");
                                return;
                            }
                            //稀释倍数超过100 无法启动配气
                            float dtm = NumberUtil.parseFloat(gasList.get(i).dilutionMul);
                            if ((dtm <= 0 || dtm > 100)) {
                                liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                                delayShowMsgDisDialog(gasList.get(i).passageBean.name + "：稀释倍数 1~100");
                                return;
                            }

                        }
                        SendGasNameConfig gasNameConfig = new SendGasNameConfig(name1, name2, name3, name4);
                        //发送配气方法名称设置
                        SenDataUtil.sendGasName(gasNameConfig);
                    }
                    //目标压力
                    Float tp = NumberUtil.parseFloat(liveDataStateBean.gasConfigTargetPress.getValue(),
                            -1.0f);
                    if (start) {//启动检查 目标压力
                        if (tp < 0 || tp > 50) {
                            delayShowMsgDisDialog("总压力值范围0-50psi");
                            return;
                        }
                    }
                    //显示加载框
                    showWaitingDialog(new DialogOption("正在设置", QMUITipDialog.Builder.ICON_TYPE_LOADING));
                    //配气方法设置
                    SenDataUtil.sendGasConfig(new SendStandConfig(start, pass1, pass2, pass3
                            , pass4, pass5,
                            NumberUtil.parseFloat(gasList.get(1).initVal),
                            NumberUtil.parseFloat(gasList.get(2).initVal),
                            NumberUtil.parseFloat(gasList.get(3).initVal),
                            NumberUtil.parseFloat(gasList.get(4).initVal),
                            NumberUtil.parseFloat(gasList.get(5).initVal),
                            NumberUtil.parseFloat(gasList.get(1).targetVal),
                            NumberUtil.parseFloat(gasList.get(2).targetVal),
                            NumberUtil.parseFloat(gasList.get(3).targetVal),
                            NumberUtil.parseFloat(gasList.get(4).targetVal),
                            NumberUtil.parseFloat(gasList.get(5).targetVal), tp));
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
                    if (start) {
                        if (diluentRinseTime > 120 || diluentRinseTime < 0 || standRinseTime > 120 || standRinseTime < 0) {
                            delayShowMsgDisDialog("冲洗时间范围0~120秒");
                            return;
                        }
                    }
                    System.out.println(diluentRinseTime + " UUUUUUUUU " + standRinseTime);
                    //显示加载框
                    showWaitingDialog(new DialogOption("正在设置", QMUITipDialog.Builder.ICON_TYPE_LOADING));
                    //配气方法设置
                    SenDataUtil.sendRinseConfig(new SendRinseConfig(start, standard1Gas.passageBean.selected,
                            standard2Gas.passageBean.selected, standard3Gas.passageBean.selected,
                            standard4Gas.passageBean.selected, diluentRinseTime,
                            standRinseTime, 10));
                    break;
                case R.id.pressurize: //加压启动
                    //加压方法设置
                    Float value = NumberUtil.parseFloat(liveDataStateBean.pressTargetPress.getValue());
                    if (start) {
                        RecPressureLimit limit = LiveDataStateBean.getInstant().pressureLimit.getValue();
                        if (value > 50 || value > limit.upLimit || value < limit.lowLimit) {
                            delayShowMsgDisDialog("目标压力值范围：" + limit.lowLimit + "~" + (limit.upLimit > 50 ? limit.upLimit : 50));
                            return;
                        }
                    }
                    //显示加载框
                    showWaitingDialog(new DialogOption("正在设置", QMUITipDialog.Builder.ICON_TYPE_LOADING));
                    SenDataUtil.sendPressureConfig(start, value);
                    break;
                case R.id.add_sample://加样启动
                    //加压方法设置
                    Integer pw = liveDataStateBean.addSampPressOpen.getValue();
                    Float addSampvalue = NumberUtil.parseFloat(liveDataStateBean.addSampTargetPress.getValue());
                    if (start) {
                        if (pw < 0) {
                            delayShowMsgDisDialog("请选择加样种类");
                            return;
                        }
                        RecPressureLimit limitS = LiveDataStateBean.getInstant().pressureLimit.getValue();
                        if (addSampvalue > 50 || addSampvalue > limitS.upLimit || addSampvalue < limitS.lowLimit) {
                            delayShowMsgDisDialog("目标压力值范围：" + limitS.lowLimit + "~" + (limitS.upLimit > 50 ? limitS.upLimit : 50));
                            return;
                        }
                    }
                    //显示加载框
                    showWaitingDialog(new DialogOption("正在设置", QMUITipDialog.Builder.ICON_TYPE_LOADING));
                    SenDataUtil.sendAddSampConfig(start, pw, addSampvalue);
                    break;
            }

        }
    });


    //设置Tab选中事件 切换页面要用
    public void statusCheckListener(int checkId) {
        liveDataStateBean.selectType.setValue(checkId);
    }
}
