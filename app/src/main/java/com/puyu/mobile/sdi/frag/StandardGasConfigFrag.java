package com.puyu.mobile.sdi.frag;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;

import com.puyu.mobile.sdi.bean.PassageBean;
import com.puyu.mobile.sdi.bean.SystemMonitor;
import com.puyu.mobile.sdi.databinding.FragStandardGasConfigBinding;
import com.puyu.mobile.sdi.databinding.ItemPassageBinding;
import com.puyu.mobile.sdi.model.StandardGasConfigRepository;
import com.puyu.mobile.sdi.mvvm.BaseFragment;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.mvvm.command.BindingAction;
import com.puyu.mobile.sdi.mvvm.command.BindingCommand;
import com.puyu.mobile.sdi.viewmodel.StandardGasConfigViewModel;

import org.jetbrains.annotations.NotNull;

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
    PassageAdapter stationAdapter;

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
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_h));
        binding.rvPassage.addItemDecoration(divider);
        ArrayList<PassageBean> passageBeans = new ArrayList<>();
        passageBeans.add(new PassageBean("稀释气", 0, new MutableLiveData<>(true)));
        passageBeans.add(new PassageBean("标气1", 1, viewModel.liveDataStateBean.pass1Swich));
        passageBeans.add(new PassageBean("标气2", 2, viewModel.liveDataStateBean.pass2Swich));
        passageBeans.add(new PassageBean("标气3", 3, viewModel.liveDataStateBean.pass3Swich));
        passageBeans.add(new PassageBean("标气4", 4, viewModel.liveDataStateBean.pass4Swich));
        passageBeans.add(new PassageBean("多级稀释气", 6, new MutableLiveData<>(true)));
        stationAdapter = new PassageAdapter(passageBeans);
        stationAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PassageBean item = (PassageBean) adapter.getItem(position);
                if (item == null) return;
                if (view.getId() == R.id.layout_content) {
                    if (position < binding.vpPassageDetail.getAdapter().getItemCount()) {
                        binding.vpPassageDetail.setCurrentItem(position, false);
                        stationAdapter.setShowIndex(position);
                    }
                } /*else if (view.getId() == R.id.checkbox) {
                    boolean selected = !item.isSelected();
                    if (item.getPrassage() == 1) {
                        viewModel.liveDataStateBean.pass1Swich.setValue(selected);

                    } else if (item.getPrassage() == 2) {
                        viewModel.liveDataStateBean.pass2Swich.setValue(selected);

                    } else if (item.getPrassage() == 3) {
                        viewModel.liveDataStateBean.pass3Swich.setValue(selected);

                    } else if (item.getPrassage() == 4) {
                        viewModel.liveDataStateBean.pass4Swich.setValue(selected);
                    }
                    item.setSelected(selected);
                    stationAdapter.notifyItemChanged(position);
                }*/
            }
        });
        binding.rvPassage.setAdapter(stationAdapter);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(GasDiluentDetailFrag.getInstance());
        fragments.add(GasStand1DetailFrag.getInstance());
        fragments.add(GasStand2DetailFrag.getInstance());
        fragments.add(GasStand3DetailFrag.getInstance());
        fragments.add(GasStand4DetailFrag.getInstance());
        fragments.add(GasMulDiluentDetailFrag.getInstance());
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


    }

    @Override
    protected void initViewObservable() {
        viewModel.liveDataStateBean.systemMonitor.observe(this, new Observer<SystemMonitor>() {
            @Override
            public void onChanged(SystemMonitor systemMonitor) {
                //  stationAdapter.setRun(systemMonitor.runProcess, systemMonitor.runPassage);
            }
        });

        viewModel.liveDataStateBean.pass1Swich.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //  stationAdapter.setCheckPass(aBoolean);

            }
        });
        viewModel.liveDataStateBean.pass2Swich.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
        viewModel.liveDataStateBean.pass3Swich.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
        viewModel.liveDataStateBean.pass4Swich.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

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

    public class PassageAdapter extends BaseQuickAdapter<PassageBean, BaseViewHolder> {
        private int shwoIndex = 0;
        private Drawable md;
        private byte mPassage = -1; //上次通道


        public PassageAdapter(List<PassageBean> data) {
            super(R.layout.item_passage, data);
            md = ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_notification_overlay);
        }

        public void itemClick(View v,PassageBean passageBean,int pos){
            showToast(passageBean.name);
        };

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

        public void setShowIndex(int newIndex) {
            int oldIndex = shwoIndex;
            this.shwoIndex = newIndex;
            notifyItemChanged(oldIndex);
            notifyItemChanged(shwoIndex);
        }

        @Override
        protected void convert(BaseViewHolder helper, PassageBean item) {
            if (item == null) {
                return;
            }

            // 获取 Binding
            ItemPassageBinding binding = DataBindingUtil.getBinding(helper.itemView);
            if (binding != null) {
                // 设置数据
                binding.setViewModel(item);
                binding.setAdapter(this);
                binding.executePendingBindings();
                binding.setPos(helper.getLayoutPosition());
            }
        }

/*
            holder.setText(R.id.tv_name,item.getName()+"("+item.getPrassage()+")")
                .

        setTextColor(R.id.tv_name, ContextCompat.getColor(getContext(),item.

        isSelected() ?
        R.color.c_16a5ff :R.color.c_384051))
                .

        setVisible(R.id.show_flag, holder.getLayoutPosition() ==shwoIndex)
                .

        setChecked(R.id.checkbox, item.isSelected())
                .

        addOnClickListener(R.id.layout_content, R.id.checkbox);
            ((TextView)holder.getView(R.id.tv_name)).

        setCompoundDrawables(
                item.getPrassage() ==mPassage ?md :null,null,
                null,null);
    }

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

    public void setCheckPass(Boolean aBoolean) {

    }*/
    }
}

