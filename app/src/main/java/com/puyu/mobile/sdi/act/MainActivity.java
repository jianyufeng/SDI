package com.puyu.mobile.sdi.act;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.pgyersdk.update.PgyUpdateManager;
import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.R;
import com.puyu.mobile.sdi.bean.LabelSave;
import com.puyu.mobile.sdi.databinding.ActivityMainBinding;
import com.puyu.mobile.sdi.dialog.DialogPrintLabel;
import com.puyu.mobile.sdi.frag.AddSampleFrag;
import com.puyu.mobile.sdi.frag.PressurizeFrag;
import com.puyu.mobile.sdi.frag.RinseFrag;
import com.puyu.mobile.sdi.frag.SetFrag;
import com.puyu.mobile.sdi.frag.StandardGasConfigFrag;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseActivity;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.netty.NettyConnected;
import com.puyu.mobile.sdi.util.ScreenStateUtil;
import com.puyu.mobile.sdi.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private RinseFrag rinseFrag;
    /**
     * 延时显示更新对话框
     */
    private void updateAPP() {
        new PgyUpdateManager.Builder()
                .register();
    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initParam() {
        //开启全屏模式
     /*   getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_U/I_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);*/
        ScreenStateUtil.fullScreen(this,true);
    }

    @Override
    public MainViewModel initViewModel() {
        ViewModelParamsFactory<MainRepository> factory = new ViewModelParamsFactory<>(getApplication(), new MainRepository());
        return new ViewModelProvider(this, factory).get(MainViewModel.class);

    }

    @Override
    protected void initViewObservable() {
        viewModel.liveDataStateBean.selectType.observe(this, checkedId -> {
            switch (checkedId) {
                case R.id.standard_gas_config:
                    binding.vp2.setCurrentItem(0, false);
                    break;
                case R.id.rinse:
                    binding.vp2.setCurrentItem(1, false);
                    rinseFrag.setRefreshAdapter();
                    break;
                case R.id.pressurize:
                    binding.vp2.setCurrentItem(2, false);
                    break;
                case R.id.add_sample:
                    binding.vp2.setCurrentItem(3, false);
                    break;
                case R.id.set:
                    binding.vp2.setCurrentItem(4, false);
                    break;
            }
            binding.tvAllTime.setVisibility(checkedId == R.id.standard_gas_config ? View.VISIBLE : View.GONE);
            binding.tvAllTimeTip.setVisibility(checkedId == R.id.standard_gas_config ? View.VISIBLE : View.GONE);
            binding.layoutRealState.setVisibility(checkedId == R.id.set ? View.GONE : View.VISIBLE);
        });
        viewModel.liveDataStateBean.mainActivityDisLoadDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                dismissDialog();
                showToast(msg);
            }
        });
         viewModel.labels.observe(this, new Observer<List<LabelSave>>() {
             @Override
             public void onChanged(List<LabelSave> labelSaves) {
                 new DialogPrintLabel(MainActivity.this,null, labelSaves).showNow(getSupportFragmentManager(), "labelDialog");
             }
         });
    }


    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(StandardGasConfigFrag.getInstance());
        fragments.add(rinseFrag = RinseFrag.getInstance());
        fragments.add(PressurizeFrag.getInstance());
        fragments.add(AddSampleFrag.getInstance());
        fragments.add(SetFrag.getInstance());
        binding.vp2.setOffscreenPageLimit(fragments.size());
        binding.vp2.setAdapter(new FragmentStateAdapter(this) {
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
        binding.vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                View childAt = binding.rg.getChildAt(position);
                binding.rg.check(childAt.getId());
            }
        });
        client = new NettyConnected();
        client.start();
        viewModel.liveDataStateBean.setNettyConnected(client);
//        ChatController.getInstance().waitingForFriends();
//        NettyServer nettyServer = new NettyServer();
//        nettyServer.startServer();
    }
    NettyConnected client;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.close();
        }
    }
}