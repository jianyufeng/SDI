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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.IBackgroundFormat;
import com.bin.david.form.data.format.grid.BaseGridFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puyu.mobile.sdi.APP;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.MethodGasConfig;
import com.puyu.mobile.sdi.bean.MethodSave;
import com.puyu.mobile.sdi.db.DBManager;
import com.puyu.mobile.sdi.mvvm.view.ToastInstance;
import com.puyu.mobile.sdi.util.CollectionUtil;
import com.puyu.mobile.sdi.util.ScreenStateUtil;

import java.util.List;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/9/7 15:11
 * desc   :
 * version: 1.0
 */
public class DialogChoseMethod extends DialogFragment implements View.OnClickListener {
    List<MethodSave> data;
    int showIndex = 0;
    private BaseQuickAdapter<MethodSave, BaseViewHolder> adapter;
    TableData<MethodGasConfig> tableData;
    SmartTable<MethodGasConfig> detailTable;
    Context context;
    ImportMethodCall methodCall;

    public DialogChoseMethod(Context context, ImportMethodCall methodCall, List<MethodSave> data) {
        this.data = data;
        this.context = context;
        this.methodCall = methodCall;
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
        return inflater.inflate(R.layout.dialog_chose_method, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ig_dismiss).setOnClickListener(this);
        view.findViewById(R.id.btn_import).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);
        if (CollectionUtil.isEmpty(data)) return;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView nameRecycle = view.findViewById(R.id.name_recycle);
        nameRecycle.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_h));
        nameRecycle.addItemDecoration(divider);
        adapter = new BaseQuickAdapter<MethodSave, BaseViewHolder>(R.layout.item_method_name, data) {
            @Override
            protected void convert(@NonNull BaseViewHolder holder, MethodSave methodSave) {
                holder.setText(R.id.tv_name, methodSave.gasName)
                        .setTextColor(R.id.tv_name, ContextCompat.getColor(APP.getInstance(), holder.getLayoutPosition() == showIndex ?
                                R.color.c_16a5ff : R.color.c_384051))
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
        nameRecycle.setAdapter(adapter);
        detailTable = view.findViewById(R.id.detail_table);


        //int dp5 = ScreenStateUtil.dip2px(5);
        detailTable.getConfig()
                .setMinTableWidth((int) ((ScreenStateUtil.getWidth() - ScreenStateUtil.dip2px(50)) * 0.8))
                .setColumnTitleStyle(new FontStyle(context, 16,
                        ContextCompat.getColor(context, R.color.c_384051)))
                .setContentStyle(new FontStyle(context, 15,
                        ContextCompat.getColor(context, R.color.c_384051)))
                .setColumnTitleBackground(new IBackgroundFormat() {
                    @Override
                    public void drawBackground(Canvas canvas, Rect rect, Paint paint) {
                        paint.setColor(ContextCompat.getColor(context, R.color.c_D5EFFF));
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRoundRect(new RectF(0, 0,
                                rect.right - rect.left, rect.bottom - rect.top), 0, 0, paint);
                    }
                })
                .setShowTableTitle(false)
                .setShowXSequence(false)
                .setShowYSequence(false)
                .setTableGridFormat(new BaseGridFormat() {
                    @Override
                    protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
                        return false;
                    }
                });
        detailTable.setZoom(false);
        Column<String> passNameCol = new Column<>("通道", "passName");
        Column<Boolean> passSwitch = new Column<>("开启", "passSwitch");
        int minHeight = ScreenStateUtil.dip2px(40);
        passNameCol.setFixed(true);
        passNameCol.setMinHeight(minHeight);
        Column<String> gasNameCol = new Column<>("标气名称", "gasName");
        Column<Float> gasInitValCol = new Column<>("初始值", "initVal");
        Column<Float> gasTargetValCol = new Column<>("目标值", "targetVal");
        Column<String> gasUnitCol = new Column<>("单位", "unit");
        tableData = new TableData<>("标气配置",
                data.get(0).methodGasConfigs, passNameCol, passSwitch, gasNameCol, gasInitValCol, gasTargetValCol, gasUnitCol);
        detailTable.setTableData(tableData);
    }

    public void setTableData(List<MethodGasConfig> data) {
        if (tableData != null) {
            tableData.setT(data);
            detailTable.notifyDataChanged();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ScreenStateUtil.getWidth() - ScreenStateUtil.dip2px(50)
                , ScreenStateUtil.getHeight() - ScreenStateUtil.dip2px(50));
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置动画、位置、宽度等属性（注意一：必须放在onStart方法中）

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ig_dismiss:
                dismiss();
                break;
            case R.id.btn_delete:
                if (CollectionUtil.isEmpty(data)) {
                    ToastInstance.ShowText("暂无方法");
                    return;
                }
                DBManager.getInstance().removeMethod(data.get(showIndex));
                data.remove(showIndex);
                adapter.notifyDataSetChanged();
                if (CollectionUtil.isEmpty(data)) {
                    tableData = null;
                    detailTable.setTableData(null);
                } else {
                    showIndex = 0;
                    setTableData(data.get(0).methodGasConfigs);
                }
                ToastInstance.ShowText("删除成功");
                break;
            case R.id.btn_import:
                if (CollectionUtil.isEmpty(data)) {
                    ToastInstance.ShowText("暂无方法");
                    return;
                }
                if (methodCall != null) {
                    methodCall.callMethod(adapter.getItem(showIndex));
                }
                dismiss();
                break;
        }
    }

    public interface ImportMethodCall {
        void callMethod(MethodSave methodSave);
    }
}
