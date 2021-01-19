package com.puyu.mobile.sdi.myviewadapter.textview;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.PressureLimit;
import com.puyu.mobile.sdi.util.NumberUtil;
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

    //设置稀释冲洗时间 报警页面
    @BindingAdapter(value = {"bgPresVal"}, requireAll = false)
    public static void setPreBg(TextView view, final String val) {
        Float mulV = NumberUtil.parseFloat(val);
        PressureLimit limit = LiveDataStateBean.getInstant().pressureLimit.getValue();
        if (mulV > 50 || mulV > limit.upLimit || mulV < limit.lowLimit) {
            view.setBackgroundResource(R.drawable.big_strocke_bg_red);
        } else {
            view.setBackgroundResource(R.drawable.big_corner_bg);
        }
    }
}
