package com.puyu.mobile.sdi.frag;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.adapter.MyFragmentStateAdapter;
import com.puyu.mobile.sdi.adapter.StandGasPassageAdapter;
import com.puyu.mobile.sdi.bean.RecSystemMonitor;
import com.puyu.mobile.sdi.bean.StandardGas;
import com.puyu.mobile.sdi.databinding.FragStandardGasConfigBinding;
import com.puyu.mobile.sdi.model.StandardGasConfigRepository;
import com.puyu.mobile.sdi.mvvm.BaseFragment;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.viewmodel.StandardGasConfigViewModel;

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
    private StandGasPassageAdapter passageAdapter;

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
        //设置左边的通道页面
        binding.etFinalPressure.getEditableText();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvPassage.setLayoutManager(manager);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_h));
        binding.rvPassage.addItemDecoration(divider);
        passageAdapter = new StandGasPassageAdapter(binding.rvPassage, viewModel.liveDataStateBean.standardGases.getValue());
        //通道点击事件
        passageAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            StandardGas item = (StandardGas) adapter.getItem(position);
            if (item == null) return;
            if (view.getId() == R.id.layout_content) {
                //点击切换 标气设置页面
                binding.vpPassageDetail.setCurrentItem(position, true);
                //点击改变自身
                passageAdapter.setShowIndex(position);

            } else if (view.getId() == R.id.checkbox) {
                //实体反转状态
                item.passageBean.selected = !item.passageBean.selected;
                //修改视图
                passageAdapter.notifyItemChanged(position);
                //修改 标气设置页面中的选中
                viewModel.liveDataStateBean.standardGases.setValue(viewModel.liveDataStateBean.standardGases.getValue());
                //点击切换 标气设置页面
                binding.vpPassageDetail.setCurrentItem(position, true);
                //点击改变自身
                passageAdapter.setShowIndex(position);

            }
        });
        binding.rvPassage.setAdapter(passageAdapter);


        //创建右边的配置页面
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(GasDiluentDetailFrag.getInstance());
        fragments.add(GasStand1DetailFrag.getInstance());
        fragments.add(GasStand2DetailFrag.getInstance());
        fragments.add(GasStand3DetailFrag.getInstance());
        fragments.add(GasStand4DetailFrag.getInstance());
        fragments.add(GasMulDiluentDetailFrag.getInstance());
        binding.vpPassageDetail.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        binding.vpPassageDetail.setOffscreenPageLimit(fragments.size());
        binding.vpPassageDetail.setAdapter(new MyFragmentStateAdapter(getActivity(), fragments));
        //页面滑动事件
        binding.vpPassageDetail.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                passageAdapter.setShowIndex(position);
            }
        });

    }


    @Override
    protected void initViewObservable() {
        viewModel.liveDataStateBean.systemMonitor.observe(this,
                new Observer<RecSystemMonitor>() {
                    @Override
                    public void onChanged(RecSystemMonitor systemMonitor) {
                        passageAdapter.setRun(systemMonitor.runProcess, systemMonitor.runPassage);
                    }
                });
        //标气设置页面中的选中 发生变化
        viewModel.changePassage.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer pos) {
                //实体数据应经修改，但是RecycleView不是bading的需要手动刷新
                passageAdapter.notifyItemChanged(binding.vpPassageDetail.getCurrentItem());
            }
        });
        //代码被动显示frag
        viewModel.liveDataStateBean.showIndexFrag.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer pos) {
                binding.vpPassageDetail.setCurrentItem(pos, true);
            }
        });
    }
}

