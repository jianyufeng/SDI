package com.puyu.mobile.sdi.mvvm.viewadapter.spinner;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.ArrayRes;
import androidx.databinding.BindingAdapter;

import com.puyu.mobile.sdi.mvvm.command.BindingCommand;

/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    /**
     * 双向的SpinnerViewAdapter, 可以监听选中的条目,也可以回显选中的值
     *
     * @param spinner        控件本身
     * @param itemDatasId    下拉条目的集合
     * @param valueReply     回显的value
     * @param bindingCommand 条目点击的监听
     */
    @BindingAdapter(value = {"itemDatasId", "valueReply", "resource", "dropDownResource", "onItemSelectedCommand"}, requireAll = false)
    public static void onItemSelectedCommand(final Spinner spinner, final @ArrayRes int itemDatasId,
                                             String valueReply, int resource, int dropDownResource, final BindingCommand<String> bindingCommand) {
        if (itemDatasId == 0) {
            throw new NullPointerException("this itemDatas parameter is null");
        }
        String[] itemDatas = spinner.getContext().getResources().getStringArray(itemDatasId);
       /* List<String> lists = new ArrayList<>();
        for (String iKeyAndValue : itemDatas) {
            lists.add(iKeyAndValue.getKey());
        }*/
        if (resource == 0) {
            resource = android.R.layout.simple_spinner_item;
        }
        if (dropDownResource == 0) {
            dropDownResource = android.R.layout.simple_spinner_dropdown_item;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(spinner.getContext(), resource, itemDatas);
        adapter.setDropDownViewResource(dropDownResource);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String iKeyAndValue = itemDatas[position];
                //将IKeyAndValue对象交给ViewModel
                if (bindingCommand != null) {
                    bindingCommand.execute(iKeyAndValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //回显选中的值
        if (!TextUtils.isEmpty(valueReply)) {
            for (int i = 0; i < itemDatas.length; i++) {
                String iKeyAndValue = itemDatas[i];
                if (valueReply.equals(iKeyAndValue)) {
                    spinner.setSelection(i);
                    return;
                }
            }
        }
    }
}
