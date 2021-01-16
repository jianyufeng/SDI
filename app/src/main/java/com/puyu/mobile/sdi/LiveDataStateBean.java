package com.puyu.mobile.sdi;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;

import com.puyu.mobile.sdi.bean.DeviceId;
import com.puyu.mobile.sdi.bean.DeviceMCUVersion;
import com.puyu.mobile.sdi.bean.DeviceType;
import com.puyu.mobile.sdi.bean.LinkStateEnum;
import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.bean.PressureLimit;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.mvvm.livedata.SingleLiveEvent;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;

import java.util.ArrayList;
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

    public LiveDataStateBean() {
        List<StandardGas> standardGasesData = new ArrayList<>();
        standardGasesData.add(new StandardGas("稀释气", "percent",
                String.valueOf(100), String.valueOf(100),
                String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean("稀释气", 0, true, 0)));
        standardGasesData.add(new StandardGas("标气1", "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气1", 1, true, 1)));
        standardGasesData.add(new StandardGas("标气2", "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气2", 2, true, 2)));
        standardGasesData.add(new StandardGas("标气3", "ppb", String.valueOf(1000),
                String.valueOf(20), String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气3", 3, true, 3)));
        standardGasesData.add(new StandardGas("标气4", "ppb", String.valueOf(1000), String.valueOf(20),
                String.valueOf(50), String.valueOf(0), String.valueOf(0),
                new PassageBean("标气4", 4, true, 4)));
        standardGasesData.add(new StandardGas("多级稀释气", "", String.valueOf(0), String.valueOf(0),
                String.valueOf(0), String.valueOf(0), String.valueOf(0),
                new PassageBean("多级稀释气", 6, true, 5)));
        standardGasesData.add(new StandardGas("二级稀释气", "percent", String.valueOf(100),
                String.valueOf(100), String.valueOf(1), String.valueOf(0), String.valueOf(0),
                new PassageBean("二级稀释气", 0, true, 0)));
        standardGases.setValue(standardGasesData);
    }

    public LinkedBlockingQueue<String> sendData = new LinkedBlockingQueue<String>();

    //wifi连接状态
    public SingleLiveEvent<LinkStateEnum> wifiState = new SingleLiveEvent<>(LinkStateEnum.LinkStart);
    //仪器ID
    public SingleLiveEvent<DeviceId> deviceIdLiveData = new SingleLiveEvent<>();
    //仪器MCU版本
    public SingleLiveEvent<DeviceMCUVersion> deviceVersion = new SingleLiveEvent<>();
    //仪器类型
    public SingleLiveEvent<DeviceType> deviceType = new SingleLiveEvent<>();
    //压力上下限
    public SingleLiveEvent<PressureLimit> pressureLimit = new SingleLiveEvent<>();
    //系统监控
    public SingleLiveEvent<SystemMonitor> systemMonitor = new SingleLiveEvent<>(new SystemMonitor());

    //通道 - 配气的过程
    public MutableLiveData<List<StandardGas>> standardGases = new MutableLiveData<>();
    //目标压力值
    public MutableLiveData<String> targetPress = new MutableLiveData<>();

    private SingleLiveEvent<View> focusView = new SingleLiveEvent<>();

    public void viewFocus(View view, boolean focus) {
        Log.d("TTTTTTTTTTTTTTTTTTT", "viewFocus: " + focus);
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

    //单位发生变化
    public void unitChange(int pos) {
        Log.d("TTTTTTTTTTTTTTTTTTTUnit", "unitChange: ");
    /*    View value = focusView.getValue();
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
        }*/

    }

    //目标值发生变化 计算稀释倍数
    public void targetValChangecalMul(Editable editable, int pos, String init, String target) {
        Log.d("TTTTTTTTTTTTTTTTTTT", "calMul: ");
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
        Log.d("TTTTTTTTTTTTTTTTTTT", "calTarget: ");
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
}
