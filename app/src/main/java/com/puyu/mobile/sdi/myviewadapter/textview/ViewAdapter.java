package com.puyu.mobile.sdi.myviewadapter.textview;

import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.PressureLimit;
import com.puyu.mobile.sdi.util.NumberUtil;
import com.puyu.mobile.sdi.util.StringUtil;
import com.puyu.mobile.sdi.util.TimeUtil;

/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {
    @BindingAdapter({"textFloatExpand"})
    public static void setTextFloat2(TextView view, final float val) {
        view.setText(String.valueOf(NumberUtil.keepPrecision(val, 2)));
    }

    @BindingAdapter({"textFloatRunTime"})
    public static void setTextFloatRunTime(TextView view, final float val) {
        view.setText(TimeUtil.getDate(val));
    }

    //设置初始值和目标值报警页面
    @BindingAdapter(value = {"bgInitV", "bgTargetV", "check0"}, requireAll = false)
    public static void setBgE(TextView view, final String initVal, final String targetVal, boolean check0) {
        if (check0) {
            if (NumberUtil.parseFloat(view.getText().toString().trim()) <= 0) {
                view.setBackgroundResource(R.drawable.big_strocke_bg_red);
                return;
            }
        }
        Float init = NumberUtil.parseFloat(initVal);
        Float target = NumberUtil.parseFloat(targetVal);
        if (init < target) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
        } else {
            view.setBackgroundResource(R.drawable.big_corner_bg);
        }
    }

    //设置稀释倍数 报警页面
    @BindingAdapter(value = {"bgMul"}, requireAll = false)
    public static void setMulBg(TextView view, final String mulVal) {
        Float mulV = NumberUtil.parseFloat(mulVal);
        if (mulV > 100 || mulV < 1) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
        } else {
            view.setBackgroundResource(R.drawable.big_corner_bg);
        }
    }

    //设置稀释冲洗时间 报警页面
    @BindingAdapter(value = {"bgRinseTime"}, requireAll = false)
    public static void setRinseBg(TextView view, final String time) {
        Integer mulV = NumberUtil.parseInteger(time, -1);
        if (mulV > 120 || mulV < 0) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
        } else {
            view.setBackgroundResource(R.drawable.big_corner_bg);
        }
    }

    //设置目标压力 报警页面
    @BindingAdapter(value = {"bgPresVal"}, requireAll = true)
    public static void setPreBg(TextView view, final String val) {
        Float mulV = NumberUtil.parseFloat(val);
        PressureLimit limit = LiveDataStateBean.getInstant().pressureLimit.getValue();
        if (mulV > 50 || mulV > limit.upLimit || mulV < limit.lowLimit) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
        } else {
            view.setBackgroundResource(R.drawable.big_corner_bg);
        }
    }

    //设置压力下限 报警页面
    @BindingAdapter(value = {"bgPresLimitUp", "bgPresLimitLow", "checkUp"}, requireAll = true)
    public static void setPreBgLimit(TextView view, final String upLimit, final String lowLimit, final boolean checkUp) {
        //获取限值
        Float upV = NumberUtil.parseFloat(upLimit, -1.0f);
        Float lowV = NumberUtil.parseFloat(lowLimit, -1.0f);
        if (checkUp) {
            if (upV < 0 || upV > 50) {
                view.setBackgroundResource(R.drawable.big_strocke_bg_red);
                return;
            }

        } else {
            if (lowV < 0 || lowV > 1) {
                view.setBackgroundResource(R.drawable.big_strocke_bg_red);
                return;
            }
        }
        if (upV < lowV) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
            return;
        }
        view.setBackgroundResource(R.drawable.big_corner_bg);
    }

    //设置压力下限 报警页面
    @BindingAdapter(value = {"bgPresLimitUp", "bgPresLimitLow", "checkChange"}, requireAll = true)
    public static void setChangeLimitBg(TextView view, final String upLimit, final String lowLimit, final boolean chenkChange) {
        Float upV = NumberUtil.parseFloat(upLimit, -1.0f);
        Float lowV = NumberUtil.parseFloat(lowLimit, -1.0f);
        if (chenkChange) {
            PressureLimit limit = LiveDataStateBean.getInstant().pressureLimit.getValue();
            if (upV == limit.upLimit && lowV == limit.lowLimit) {
                view.setBackgroundResource(R.drawable.big_corner_bg);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.c_384051));
            } else {
                view.setBackgroundResource(R.drawable.big_corner_bg_blue);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.c_ffffff));

            }
        }
    }

    //设置仪器ID 报警页面
    @BindingAdapter(value = {"bgDeviceId"}, requireAll = true)
    public static void setBgDeviceId(TextView view, final String deviceId) {
        if (StringUtil.isEmpty(deviceId) || deviceId.getBytes().length > 12) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
        } else {
            view.setBackgroundResource(R.drawable.big_corner_bg);
        }
    }
}
