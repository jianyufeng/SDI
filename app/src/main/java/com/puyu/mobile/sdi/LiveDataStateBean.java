package com.puyu.mobile.sdi;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;

import com.puyu.mobile.sdi.bean.DeviceId;
import com.puyu.mobile.sdi.bean.DeviceMCUVersion;
import com.puyu.mobile.sdi.bean.DeviceType;
import com.puyu.mobile.sdi.bean.LinkStateEnum;
import com.puyu.mobile.sdi.bean.PressureLimit;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/26 15:51
 * desc   :
 * version: 1.0
 */
public class LiveDataStateBean {
    public static LiveDataStateBean dataStateBean = new LiveDataStateBean();


    public static LiveDataStateBean getInstant() {
        return dataStateBean;
    }

    /* public  final int Diluent_index = 0;
     public  final int Stand_1_index = 1;
     public  final int Stand_2_index = 2;
     public  final int Stand_3_index = 3;
     public  final int Stand_4_index = 4;
     public  final int MULDiluent_index = 5;
     public  final int Diluent2_index = 6;*/
    public static final String diluentName = "稀释气";
    public static final String stand1Name = "标气1";
    public static final String stand2Name = "标气2";
    public static final String stand3Name = "标气3";
    public static final String stand4Name = "标气4";
    public static final String mulDiluentName = "多级稀释气";
    public static final String diluent2Name = "二级稀释气";
    public static final int diluentPassNumber = 0;
    public static final int stand1PassNumber = 1;
    public static final int stand2PassNumber = 2;
    public static final int stand3PassNumber = 3;
    public static final int stand4PassNumber = 4;
    public static final int mulDiluentPassNumber = 5;
    public static final int diluent2PassNumber = 6;
    public MutableLiveData<String> diluentNameLiveData = new MutableLiveData<>(diluentName);
    public MutableLiveData<String> stand1NameLiveData = new MutableLiveData<>(stand1Name);
    public MutableLiveData<String> stand2NameLiveData = new MutableLiveData<>(stand2Name);
    public MutableLiveData<String> stand3NameLiveData = new MutableLiveData<>(stand3Name);
    public MutableLiveData<String> stand4NameLiveData = new MutableLiveData<>(stand4Name);
    public MutableLiveData<String> mulDiluentNameLiveData = new MutableLiveData<>(mulDiluentName);
    public MutableLiveData<String> diluent2NameLiveData = new MutableLiveData<>(diluent2Name);

    public LiveDataStateBean() {
    }

    public LinkedBlockingQueue<String> sendData = new LinkedBlockingQueue<String>();


    //wifi连接状态
    public SingleLiveEvent<LinkStateEnum> wifiState = new SingleLiveEvent<>(LinkStateEnum.LinkStart);
    //仪器ID
    public MutableLiveData<DeviceId> deviceIdLiveData = new MutableLiveData<>(new DeviceId("abcdef123"));
    //仪器MCU版本
    public SingleLiveEvent<DeviceMCUVersion> deviceVersion = new SingleLiveEvent<>();
    //仪器类型
    public SingleLiveEvent<DeviceType> deviceType = new SingleLiveEvent<>();
    //压力上下限
    public SingleLiveEvent<PressureLimit> pressureLimit = new SingleLiveEvent<>(new PressureLimit(50f, 0.1f));
    //系统监控
    public SingleLiveEvent<SystemMonitor> systemMonitor = new SingleLiveEvent<>(new SystemMonitor());
    /*********************配气页面的设置*************************************/
    //通道 - 配气页面的设置
    public MutableLiveData<List<StandardGas>> standardGases = new MutableLiveData<>();
    //目标压力值
    public MutableLiveData<String> gasConfigTargetPress = new MutableLiveData<>();


    //标气配置时显示那个页面
    public SingleLiveEvent<Integer> showIndexFrag = new SingleLiveEvent<>();
    //聚焦
    private SingleLiveEvent<View> focusView = new SingleLiveEvent<>();

    //输入框聚焦
    public void viewFocus(View view, boolean focus) {
        if (focus) {
            focusView.setValue(view);
        } else {
            focusView.setValue(null);
        }
    }

    //初始值发生变化  重置目标值和稀释倍数
    public void initValChange(Editable editable, int pos, String init, String mul) {
        if (StringUtil.isEmpty(init) || "0".equals(init.trim())) {
            standardGases.getValue().get(pos).dilutionMul = "";
            standardGases.getValue().get(pos).targetVal = "";
            standardGases.setValue(standardGases.getValue());
            return;
        }
        if (StringUtil.isEmpty(mul) || "0".equals(mul.trim())) {
            standardGases.getValue().get(pos).targetVal = "";
            standardGases.setValue(standardGases.getValue());
            return;
        }
        float initV = NumberUtil.parseFloat(init);
        float mulV = NumberUtil.parseFloat(mul);
        //更新数据
        standardGases.getValue().get(pos).targetVal = String.valueOf(NumberUtil.keepPrecision(initV / mulV, 2));
        //刷新页面
        standardGases.setValue(standardGases.getValue());
    }

    //目标值发生变化 计算稀释倍数
    public void targetValChangecalMul(Editable editable, int pos, String init, String target) {
        View value = focusView.getValue();
        if (value instanceof EditText && ((EditText) value).getEditableText() == editable) {
            float initV = NumberUtil.parseFloat(init);
            float targetV = NumberUtil.parseFloat(target);
            if (initV == 0 || targetV == 0) {
                standardGases.getValue().get(pos).dilutionMul = "";
                standardGases.setValue(standardGases.getValue());
                return;
            }
            //更新数据
            standardGases.getValue().get(pos).dilutionMul = String.valueOf(NumberUtil.keepPrecision(initV / targetV, 2));
            //刷新页面
            standardGases.setValue(standardGases.getValue());
        }

    }

    //稀释倍数发生变化 计算目标值
    public void mulChangecalTarget(Editable editable, int pos, String init, String mul) {
        View value = focusView.getValue();
        if (value instanceof EditText && ((EditText) value).getEditableText() == editable) {
            float initV = NumberUtil.parseFloat(init);
            float mulV = NumberUtil.parseFloat(mul);
            if (initV == 0 || mulV == 0) {
                standardGases.getValue().get(pos).targetVal = "";
                standardGases.setValue(standardGases.getValue());
                return;
            }
            //更新数据
            standardGases.getValue().get(pos).targetVal = String.valueOf(NumberUtil.keepPrecision(initV / mulV, 2));
            //刷新页面
            standardGases.setValue(standardGases.getValue());
        }
    }

    //单位发生变化
    public void unitChange(int index, AdapterView<?> parent, View view, int position, long id) {
        standardGases.getValue().get(index).gasUnit = (String) parent.getItemAtPosition(position);
        System.out.println("unitChange: " + standardGases.getValue().get(index).gasUnit);
    }

    /*********************冲洗界面*************************************/
    //冲洗界面
    public List<StandardGas> standardGasesRinse;
    //稀释气冲洗时间
    public SingleLiveEvent<String> diluentRinseTime = new SingleLiveEvent<>();
    //标气冲洗时间
    public SingleLiveEvent<String> standRinseTime = new SingleLiveEvent<>();


    /*********************加压界面*************************************/
    public SingleLiveEvent<String> pressTargetPress = new SingleLiveEvent<>();

    /*********************加样界面*************************************/
    public SingleLiveEvent<Integer> addSampPressOpen = new SingleLiveEvent<>(-1);
    public SingleLiveEvent<String> addSampTargetPress = new SingleLiveEvent<>();


    /*********************设置界面*************************************/


}
