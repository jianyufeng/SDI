package com.puyu.mobile.sdi.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puyu.mobile.sdi.APP;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.StandardGas;

import java.util.List;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/13 17:01
 * desc   :
 * version: 1.0
 */
public class StandGasPassageAdapter extends BaseQuickAdapter<StandardGas, BaseViewHolder> {
    private int showIndex = 0;
    private Drawable md;
    private byte mPassage = -1; //上次通道
    private RecyclerView recyclerView;


    public StandGasPassageAdapter(RecyclerView recyclerView, List<StandardGas> standardGases) {
        super(R.layout.item_passage, standardGases.subList(0, standardGases.size() - 1));
        this.recyclerView = recyclerView;
        md = ContextCompat.getDrawable(APP.getInstance(), android.R.drawable.ic_notification_overlay);

    }

    //显示对应的Tab  Tab和标气配置页面一致
    public void setShowIndex(int newIndex) {
        if (showIndex == newIndex) return;
        int oldIndex = showIndex;//保存下
        this.showIndex = newIndex;
        notifyItemChanged(oldIndex);//刷新旧的
        notifyItemChanged(showIndex);//刷新新的
        recyclerView.smoothScrollToPosition(newIndex);
    }

    @Override
    protected void convert(BaseViewHolder holder, StandardGas item) {
        holder.setText(R.id.tv_name, item.passageBean.name + "(" + item.passageBean.prassage + ")")
                .setTextColor(R.id.tv_name, ContextCompat.getColor(APP.getInstance(), item.passageBean.selected ?
                        R.color.c_16a5ff : R.color.c_384051))
                .setVisible(R.id.show_flag, holder.getLayoutPosition() == showIndex)
                .setVisible(R.id.checkbox, item.passageBean.prassage != 0)
                .setChecked(R.id.checkbox, item.passageBean.selected)
                .addOnClickListener(R.id.layout_content, R.id.checkbox);
        ((TextView) holder.getView(R.id.tv_name)).setCompoundDrawables(
                item.passageBean.prassage == mPassage ? md : null, null,
                null, null);
    }

    //设置运行时的状态
    public void setRun(byte cRunProcess, byte cRunPassage) {
        if (cRunProcess == (byte) 0x01) { //配气
            if (cRunPassage != mPassage) {
                mPassage = cRunPassage;
                notifyDataSetChanged();
            }
        } else if (mPassage != -1) {
            mPassage = -1;
            notifyDataSetChanged();
        }
    }

}
