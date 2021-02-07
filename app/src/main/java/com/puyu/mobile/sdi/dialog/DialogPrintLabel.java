package com.puyu.mobile.sdi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.LabelSave;
import com.puyu.mobile.sdi.bean.MethodSave;
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
public class DialogPrintLabel extends DialogFragment implements View.OnClickListener {
    List<LabelSave> data;
    private BaseQuickAdapter<LabelSave, BaseViewHolder> adapter;
    Context context;
    ImportMethodCall methodCall;

    public DialogPrintLabel(Context context, ImportMethodCall methodCall, List<LabelSave> data) {
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
        return inflater.inflate(R.layout.dialog_print_label, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ig_dismiss).setOnClickListener(this);
        if (CollectionUtil.isEmpty(data)) return;
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView nameRecycle = view.findViewById(R.id.type_recycle);
        nameRecycle.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_h));
        nameRecycle.addItemDecoration(divider);
        adapter = new BaseQuickAdapter<LabelSave, BaseViewHolder>(R.layout.item_type_print, data) {
            @Override
            protected void convert(@NonNull BaseViewHolder holder, LabelSave methodSave) {
               /* holder.setText(R.id.tv_name, methodSave.gasName)
                        .setTextColor(R.id.tv_name, ContextCompat.getColor(APP.getInstance(), holder.getLayoutPosition() == showIndex ?
                                R.color.c_16a5ff : R.color.c_384051))
                        .setVisible(R.id.show_flag, holder.getLayoutPosition() == showIndex);*/
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
               /* if (showIndex == i) return;
                showIndex = i;
                adapter.notifyDataSetChanged();
                setTableData(adapter.getItem(i).methodGasConfigs);*/
            }
        });
        nameRecycle.setAdapter(adapter);

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
           /* case R.id.btn_import:
                if (CollectionUtil.isEmpty(data)) {
                    ToastInstance.ShowText("暂无方法");
                    return;
                }
                if (methodCall != null) {
                    methodCall.callMethod(adapter.getItem(showIndex));
                }
                dismiss();
                break;*/
        }
    }

    public interface ImportMethodCall {
        void callMethod(MethodSave methodSave);
    }
}
