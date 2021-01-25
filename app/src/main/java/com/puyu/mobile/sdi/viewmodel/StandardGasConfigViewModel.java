package com.puyu.mobile.sdi.viewmodel;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.MethodGasConfig;
import com.puyu.mobile.sdi.bean.MethodSave;
import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.db.DBManager;
import com.puyu.mobile.sdi.model.StandardGasConfigRepository;
import com.puyu.mobile.sdi.mvvm.BaseViewModel;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.mvvm.command.BindingConsumer;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.relation.ToMany;

public class StandardGasConfigViewModel extends BaseViewModel<StandardGasConfigRepository> {
    public final LiveDataStateBean liveDataStateBean;


    public SingleLiveEvent<Integer> changePassage = new SingleLiveEvent<>();
    public SingleLiveEvent<MethodSave> saveStandConfig = new SingleLiveEvent<>();
    public SingleLiveEvent<List<MethodSave>> methods = new SingleLiveEvent<>();


    public StandardGasConfigViewModel(@NonNull Application application, StandardGasConfigRepository model) {
        super(application, model);
        liveDataStateBean = LiveDataStateBean.getInstant();
        List<StandardGas> standardGasesData = new ArrayList<>();
        standardGasesData.add(new StandardGas(liveDataStateBean.diluentNameLiveData, "percent",
                String.valueOf(100), String.valueOf(100),
                String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.diluentName, LiveDataStateBean.diluentPassNumber, true, 0)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand1NameLiveData, "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.stand1Name, LiveDataStateBean.stand1PassNumber, true, 1)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand2NameLiveData, "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.stand2Name, LiveDataStateBean.stand2PassNumber, true, 2)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand3NameLiveData, "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.stand3Name, LiveDataStateBean.stand3PassNumber, true, 3)));
        standardGasesData.add(new StandardGas(liveDataStateBean.stand4NameLiveData, "ppb", String.valueOf(1000), String.valueOf(20),
                String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.stand4Name, LiveDataStateBean.stand4PassNumber, true, 4)));
        standardGasesData.add(new StandardGas(liveDataStateBean.mulDiluentNameLiveData, "ppb", String.valueOf(0), String.valueOf(0),
                String.valueOf(0), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.mulDiluentName, LiveDataStateBean.mulDiluentPassNumber, true, 5)));
        standardGasesData.add(new StandardGas(liveDataStateBean.diluent2NameLiveData, "percent", String.valueOf(100),
                String.valueOf(100), String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean(LiveDataStateBean.diluent2Name, LiveDataStateBean.diluent2PassNumber, true, 6)));
        liveDataStateBean.standardGases.setValue(standardGasesData);
    }

    //选中事件
    public void choseWhich(int pos, boolean swicth) {
        changePassage.setValue(pos);
    }

    //保存方法 点击事件
    public BindingCommand<String> saveMethod = new BindingCommand<>(new BindingConsumer<String>() {
        @Override
        public void call(String s) {
            long count = DBManager.getInstance().getAllMethodCount();
            if (count > 20) {
                showToast("最多保存20个方法");
                return;
            }
            //1.获取标气名称
            List<StandardGas> gasList = liveDataStateBean.standardGases.getValue();
            //通道开关
            boolean pass1 = gasList.get(1).passageBean.selected;
            boolean pass2 = gasList.get(2).passageBean.selected;
            boolean pass3 = gasList.get(3).passageBean.selected;
            boolean pass4 = gasList.get(4).passageBean.selected;
            boolean pass5 = gasList.get(5).passageBean.selected;
            if (!(pass1 || pass2 || pass3 || pass4 || pass5)) {
                showToast("至少打开一路标气开关");
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
                    showToast(gasList.get(i).passageBean.name + "：气体名称为空");
                    liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                    return;
                }
                //3、校验标气名称长度最长20个字节
                if (name.getBytes().length > 20) {
                    showToast(gasList.get(i).passageBean.name + "：气体名称过长");
                    liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                    return;
                }

                //通道初始值
                float initV = NumberUtil.parseFloat(gasList.get(i).initVal);
                //通道目标值
                float targetV = NumberUtil.parseFloat(gasList.get(i).targetVal);
                if ((initV <= 0 || targetV <= 0 || initV < targetV)) {
                    showToast(gasList.get(i).passageBean.name + "：初始值/目标值 有误");
                    liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                    return;
                }
                //稀释倍数超过100 无法启动配气
                float dtm = NumberUtil.parseFloat(gasList.get(i).dilutionMul);
                if ((dtm <= 0 || dtm > 100)) {
                    showToast(gasList.get(i).passageBean.name + "：稀释倍数 1~100");
                    liveDataStateBean.showIndexFrag.setValue(gasList.get(i).passageBean.index);
                    return;
                }

            }
            List<StandardGas> value = liveDataStateBean.standardGases.getValue();
            for (int i = 0; i < value.size(); i++) {
                StandardGas standardGas = value.get(i);
                Log.e("TTTT", "call: " + standardGas.toString());
            }
            //保存标气配置
            MethodSave methodSave = new MethodSave();
            //ArrayList<MethodGasConfig> datas = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                StandardGas standardGas = gasList.get(i);
                methodSave.methodGasConfigs.add(new MethodGasConfig(standardGas.gasName.getValue(), NumberUtil.parseFloat(standardGas.initVal),
                        NumberUtil.parseFloat(standardGas.targetVal),
                        NumberUtil.parseFloat(standardGas.dilutionMul),
                        standardGas.gasUnit, standardGas.passageBean.name,
                        standardGas.passageBean.prassage, standardGas.passageBean.selected));
            }
            //methodSave.methodGasConfigs.addAll(datas);
            saveStandConfig.setValue(methodSave);

        }
    });

    //导入方法 点击事件
    public BindingCommand<String> importMethod = new BindingCommand<>(new BindingConsumer<String>() {

        @Override
        public void call(String s) {
            List<MethodSave> allMethod = DBManager.getInstance().getAllMethod();
            methods.setValue(allMethod);
        }
    });

    //导入放发
    public void setChoseMethod(MethodSave method) {
        if (method == null) return;
        ToMany<MethodGasConfig> methodGasConfigs = method.methodGasConfigs;
        liveDataStateBean.stand1NameLiveData.setValue(methodGasConfigs.get(0).gasName);
        liveDataStateBean.stand2NameLiveData.setValue(methodGasConfigs.get(1).gasName);
        liveDataStateBean.stand3NameLiveData.setValue(methodGasConfigs.get(2).gasName);
        liveDataStateBean.stand4NameLiveData.setValue(methodGasConfigs.get(3).gasName);
        liveDataStateBean.mulDiluentNameLiveData.setValue(methodGasConfigs.get(4).gasName);
        List<StandardGas> value = liveDataStateBean.standardGases.getValue();
        for (int i = 1; i < 6; i++) {
            value.get(i).initVal = String.valueOf(methodGasConfigs.get(i - 1).initVal);
            value.get(i).targetVal = String.valueOf(methodGasConfigs.get(i - 1).targetVal);
            value.get(i).dilutionMul = String.valueOf(methodGasConfigs.get(i - 1).dilutionMul);
            value.get(i).gasUnit = methodGasConfigs.get(i - 1).unit;
            value.get(i).passageBean.selected = methodGasConfigs.get(i - 1).passSwitch;
        }
        liveDataStateBean.standardGases.setValue(value);

    }
}
