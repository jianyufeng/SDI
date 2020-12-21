package com.puyu.mobile.sdi.frag;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.databinding.FragRinseBinding;
import com.puyu.mobile.sdi.model.RinseRepository;
import com.puyu.mobile.sdi.mvvm.BaseFragment;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.viewmodel.RinseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/16 16:30
 * desc   : 冲洗
 * version: 1.0
 */
public class RinseFrag extends BaseFragment<FragRinseBinding, RinseViewModel> {
    private static final String TAG = "11111111RinseFrag";
    public static RinseFrag getInstance() {
        // Required empty public constructor
        return new RinseFrag();
    }


    @Override
    public int initContentView() {
        return R.layout.frag_rinse;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RinseViewModel initViewModel() {
        return new ViewModelProvider(getActivity(), new ViewModelParamsFactory<>(getActivity()
                .getApplication(), new RinseRepository())).get(RinseViewModel.class);

    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new GridLayoutManager(getContext(),4);
        binding.rvPassage.setLayoutManager(manager);
        ArrayList<PassageBean> passageBeans = new ArrayList<>();
        passageBeans.add(new PassageBean("稀释气", 0, true));
        passageBeans.add(new PassageBean("标气1", 1, true));
        passageBeans.add(new PassageBean("标气2", 2, true));
        passageBeans.add(new PassageBean("标气3", 3, true));
        passageBeans.add(new PassageBean("标气4", 4, true));
        passageBeans.add(new PassageBean("二级稀释气", 5, true));
        PassageAdapter stationAdapter = new PassageAdapter(passageBeans);
        binding.rvPassage.setAdapter(stationAdapter);
        stationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PassageBean item = (PassageBean) adapter.getItem(position);
                if (item == null) return;
                if (view.getId() == R.id.layout_content) {
                    item.setSelected(!item.isSelected());
                    stationAdapter.notifyItemChanged(position);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: " );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    private class PassageAdapter extends BaseQuickAdapter<PassageBean, BaseViewHolder> {

        public PassageAdapter(List<PassageBean> data) {
            super(R.layout.item_passage_1, data);

        }

        @Override
        protected void convert(BaseViewHolder holder, PassageBean item) {
            holder.setText(R.id.checkbox, item.getName() + "(" + item.getPrassage() + ")")
                    .setChecked(R.id.checkbox, item.isSelected())
                    .addOnClickListener(R.id.layout_content);
        }
    }
}
