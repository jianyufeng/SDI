package com.puyu.mobile.sdi.adapter;


import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import com.puyu.mobile.sdi.bean.GasConfig;

import org.jetbrains.annotations.NotNull;


public class DataBindingAdapter extends BaseQuickAdapter<GasConfig, BaseViewHolder> {

    public DataBindingAdapter(int layoutResId) {
        super(layoutResId);
    }


    /**
     * 当 ViewHolder 创建完毕以后，会执行此回掉
     * 可以在这里做任何你想做的事情
     */
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        super.onItemViewHolderCreated(viewHolder, viewType);
        // 绑定 view
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GasConfig item) {
        if (item == null) {
            return;
        }
       /* // 获取 Binding
        ItemMovieBinding binding = DataBindingUtil.getBinding(helper.itemView);
        if (binding != null) {
            // 设置数据
            binding.setMovie(item);
            binding.setPresenter(mPresenter);
            binding.executePendingBindings();
        }*/
    }
}
