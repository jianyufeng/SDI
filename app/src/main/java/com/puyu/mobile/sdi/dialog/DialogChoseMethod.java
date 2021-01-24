package com.puyu.mobile.sdi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.IBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.MethodGasConfig;
import com.puyu.mobile.sdi.bean.MethodSave;
import com.puyu.mobile.sdi.databinding.DialogChoseMethodBinding;
import com.puyu.mobile.sdi.mvvm.view.ToastInstance;
import com.puyu.mobile.sdi.util.CollectionUtil;
import com.puyu.mobile.sdi.util.ScreenStateUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/9/7 15:11
 * desc   :
 * version: 1.0
 */
public class DialogChoseMethod extends DialogFragment {



    DialogChoseMethodBinding binding;
    List<MethodSave> data;
    int showIndex = 1;
    private BaseQuickAdapter<MethodSave, BaseViewHolder> adapter;
    TableData<MethodGasConfig> tableData;

    Context context;
    public DialogChoseMethod(Context context,List<MethodSave> data) {
        this.data = data;

        this.context =context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去除标题栏
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogStyle;
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_chose_method, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.igDismiss.setOnClickListener(this::onViewClicked);
        binding.btnImport.setOnClickListener(this::onViewClicked);
        if (CollectionUtil.isEmpty(data))return;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.nameRecycle.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_h));
        binding.nameRecycle.addItemDecoration(divider);
        adapter = new BaseQuickAdapter<MethodSave, BaseViewHolder>(R.layout.item_method_name, data) {
            @Override
            protected void convert(@NonNull BaseViewHolder holder, MethodSave methodSave) {
                holder.setText(R.id.tv_name, methodSave.gasName)
                        .setVisible(R.id.show_flag, holder.getLayoutPosition() == showIndex);
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (showIndex == i) return;
                showIndex = i;
                adapter.notifyDataSetChanged();
                setTableData(adapter.getItem(i).methodGasConfigs);
            }
        });
        binding.nameRecycle.setAdapter(adapter);
        //binding.detailTable.getConfig().setMinTableWidth(ScreenUtil.getWidth() - ScreenUtil.dip2px(10));
        binding.detailTable.getConfig().setContentStyle(new FontStyle(context, 15,
                ContextCompat.getColor(context, R.color.c_384051)));
        binding.detailTable.getConfig().setColumnTitleStyle(new FontStyle(context, 16,
                ContextCompat.getColor(context, R.color.c_384051)));
        int dp5 = ScreenStateUtil.dip2px(5);
        binding.detailTable.getConfig().setColumnTitleBackground(new IBackgroundFormat() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(ContextCompat.getColor(context, R.color.c_D5EFFF));
                canvas.drawRoundRect(new RectF(0, 0,
                        rect.right - rect.left, rect.bottom - rect.top), dp5, dp5, paint);
            }
        });
        binding.detailTable.getConfig().setShowTableTitle(false);
        binding.detailTable.getConfig().setShowXSequence(false);
        binding.detailTable.getConfig().setShowYSequence(false);
        LineStyle contentGridStyle = new LineStyle();
        contentGridStyle.setColor(Color.TRANSPARENT);
        binding.detailTable.getConfig().setContentGridStyle(contentGridStyle);
        binding.detailTable.getConfig().setColumnTitleGridStyle(contentGridStyle);
        binding.detailTable.setZoom(false);
        Column<String> passNameCol = new Column<>("通道", "passName");
        int minHeight = ScreenStateUtil.dip2px(40);
        passNameCol.setFixed(true);
        passNameCol.setMinHeight(minHeight);
        Column<String> gasNameCol = new Column<>("标气名称", "gasName");
        Column<Float> gasInitValCol = new Column<>("初始值", "initVal");
        Column<Float> gasTargetValCol = new Column<>("目标值", "targetVal");
        Column<String> gasUnitCol = new Column<>("单位", "unit");
        tableData = new TableData<>("标气配置",
                data.get(0).methodGasConfigs, passNameCol, gasNameCol, gasInitValCol, gasTargetValCol, gasUnitCol);
        binding.detailTable.setTableData(tableData);
    }

    public void setTableData(List<MethodGasConfig> data) {
        if (tableData != null) {
            tableData.setT(data);
            binding.detailTable.notifyDataChanged();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ScreenStateUtil.getWidth()
                ,ScreenStateUtil.getHeight());
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置动画、位置、宽度等属性（注意一：必须放在onStart方法中）

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.unbind();
        }
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ig_dismiss:
               // dismiss();
                break;
            case R.id.btn_import:
                ToastInstance.ShowText(adapter.getItem(showIndex).gasName);
                break;
        }
    }




}
