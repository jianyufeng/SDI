package com.puyu.mobile.sdi.frag;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.databinding.FragStandardGasConfigBinding;
import com.puyu.mobile.sdi.model.StandardGasConfigRepository;
import com.puyu.mobile.sdi.mvvm.BaseFragment;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.viewmodel.StandardGasConfigViewModel;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/16 16:30
 * desc   :
 * version: 1.0
 */
public class StandardGasConfigFrag extends BaseFragment<FragStandardGasConfigBinding, StandardGasConfigViewModel> {
    private static final String TAG = "11111111StandardGasConfigFrag";

    public static StandardGasConfigFrag getInstance() {
        // Required empty public constructor
        return new StandardGasConfigFrag();
    }


    @Override
    public int initContentView() {
        return R.layout.frag_standard_gas_config;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public StandardGasConfigViewModel initViewModel() {
        return new ViewModelProvider(getActivity(), new ViewModelParamsFactory<>(getActivity()
                .getApplication(), new StandardGasConfigRepository())).get(StandardGasConfigViewModel.class);

    }

    @Override
    protected void initData() {
        super.initData();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvPassage.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_h));
        binding.rvPassage.addItemDecoration(divider);
        ArrayList<PassageBean> passageBeans = new ArrayList<>();
        passageBeans.add(new PassageBean("稀释气", 0, true));
        passageBeans.add(new PassageBean("标气1", 1, true));
        passageBeans.add(new PassageBean("标气2", 2, true));
        passageBeans.add(new PassageBean("标气3", 3, true));
        passageBeans.add(new PassageBean("标气4", 4, true));
        passageBeans.add(new PassageBean("二级稀释气", 5, true));
        PassageAdapter stationAdapter = new PassageAdapter(passageBeans);
        stationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PassageBean item = (PassageBean) adapter.getItem(position);
                if (item == null) return;
                if (view.getId() == R.id.layout_content) {
                    if (position < binding.vpPassageDetail.getAdapter().getItemCount()) {
                        binding.vpPassageDetail.setCurrentItem(position,false);
                        stationAdapter.setShowIndex(position);
                    }
                } else if (view.getId() == R.id.checkbox) {
                    item.setSelected(!item.isSelected());
                    stationAdapter.notifyItemChanged(position);
                }
            }
        });
        binding.rvPassage.setAdapter(stationAdapter);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(GasDetailFrag.getInstance());
        fragments.add(GasDetailFrag.getInstance());
        fragments.add(GasDetailFrag.getInstance());
        fragments.add(GasDetailFrag.getInstance());
        fragments.add(GasDetailFrag.getInstance());
        fragments.add(GasDetailFrag.getInstance());
        binding.vpPassageDetail.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        binding.vpPassageDetail.setOffscreenPageLimit(fragments.size());
        binding.vpPassageDetail.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        binding.vpPassageDetail.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                stationAdapter.setShowIndex(position);
                binding.rvPassage.smoothScrollToPosition(position);
            }
        });
        DataInputStream dis = null;
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
        Log.e(TAG, "onDestroyView: ");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");
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
        private int shwoIndex = 0;

        public PassageAdapter(List<PassageBean> data) {
            super(R.layout.item_passage, data);

        }

        public void setShowIndex(int newIndex) {
            int oldIndex = shwoIndex;
            this.shwoIndex = newIndex;
            notifyItemChanged(oldIndex);
            notifyItemChanged(shwoIndex);
        }

        @Override
        protected void convert(BaseViewHolder holder, PassageBean item) {
            holder.setText(R.id.tv_name, item.getName() + "(" + item.getPrassage() + ")")
                    .setTextColor(R.id.tv_name, ContextCompat.getColor(getContext(), item.isSelected() ?
                            R.color.c_16a5ff : R.color.c_384051))
                    .setVisible(R.id.show_flag, holder.getLayoutPosition() == shwoIndex)
                    .setChecked(R.id.checkbox, item.isSelected())
                    .addOnClickListener(R.id.layout_content,R.id.checkbox);
        }
    }
}

