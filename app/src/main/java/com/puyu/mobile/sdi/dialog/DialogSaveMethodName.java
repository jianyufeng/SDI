package com.puyu.mobile.sdi.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.mvvm.view.ToastInstance;
import com.puyu.mobile.sdi.util.StringUtil;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/9/7 15:11
 * desc   :
 * version: 1.0
 */
public class DialogSaveMethodName extends DialogFragment {

    EditText etName;
    ConfirmMsg confirmMsg;
    String name;

    public DialogSaveMethodName(ConfirmMsg confirmMsg, String name) {
        this.confirmMsg = confirmMsg;
        this.name = name;

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
        return inflater.inflate(R.layout.dialog_change_remark, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.et_name);
        if (!StringUtil.isEmpty(name)) {
            etName.setText(name);
            etName.setSelection(name.length());
        }
        view.findViewById(R.id.btn_cfm).setOnClickListener(this::onViewClicked);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this::onViewClicked);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().setLayout(ScreenUtil.getWidth(),ScreenUtil.getHeight());
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置动画、位置、宽度等属性（注意一：必须放在onStart方法中）

    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_cfm:
                String old = etName.getText().toString().trim();
                if (StringUtil.isEmpty(old)) {
                    ToastInstance.ShowText("请输入方法名");
                    return;
                }
                if (confirmMsg != null) confirmMsg.changePsw(old);
                dismiss();
                break;
        }
    }

    public interface ConfirmMsg {
        void changePsw(String newPsw);
    }

}
