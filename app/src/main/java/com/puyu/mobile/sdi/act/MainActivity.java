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
import com.puyu.mobile.sdi.databinding.ActivityMainBinding;
import com.puyu.mobile.sdi.frag.AddSampleFrag;
import com.puyu.mobile.sdi.frag.PressurizeFrag;
import com.puyu.mobile.sdi.frag.RinseFrag;
import com.puyu.mobile.sdi.frag.SetFrag;
import com.puyu.mobile.sdi.frag.StandardGasConfigFrag;
import com.puyu.mobile.sdi.model.MainRepository;
import com.puyu.mobile.sdi.mvvm.BaseActivity;
import com.puyu.mobile.sdi.mvvm.ViewModelParamsFactory;
import com.puyu.mobile.sdi.netty.NettyConnected;
import com.puyu.mobile.sdi.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {


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
    public MainViewModel initViewModel() {
        ViewModelParamsFactory<MainRepository> factory = new ViewModelParamsFactory<>(getApplication(), new MainRepository());
        return new ViewModelProvider(this, factory).get(MainViewModel.class);

    }

    @Override
    protected void initViewObservable() {
        viewModel.selectType.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer checkedId) {
                switch (checkedId) {
                    case R.id.standard_gas_config:
                        binding.vp2.setCurrentItem(0, false);
                        break;
                    case R.id.rinse:
                        binding.vp2.setCurrentItem(1, false);
                        indw++;
                        if (indw == 1) {
                            byte[] bytes = {0x44, 0x41, 0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf2, 0x7d, (byte) 0x82, (byte) 0xf3, 0x20, 0x7d, (byte) 0x82, 0x7d, 0x7d, 0x00, 0x00};
                            client.sendMsg(bytes);
                        } else if (indw == 2) {//仪器ID
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x20, (byte) 0xaa, 0x00, 0x0C,
                                    0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, (byte) 0x4B, (byte) 0xdc, 0x7d, 0x7d};
                            client.sendMsg(bytes);
                        } else if (indw == 3) {//仪器版本
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x21, (byte) 0xaa, 0x00, 0x20,
                                    0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                                    0x4d, 0x43, 0x55, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
                                    0x38, 0x39, 0x61, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
                                    (byte) 0x36, (byte) 0x53, 0x7d, 0x7d};
                            client.sendMsg(bytes);
                        } else if (indw == 4) {//仪器类型
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x23, (byte) 0xaa, 0x00, 0x01,
                                    0x00,
                                    (byte) 0x28, (byte) 0x64, 0x7d, 0x7d};
                            client.sendMsg(bytes);
                        } else if (indw == 5) {//压力上下限
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x36, (byte) 0xaa, 0x00, 0x08,
                                    0x42, 0x5E, 0x33, 0x33,
                                    0x3E, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0x4a, (byte) 0x7b, 0x7d, 0x7d};
                            client.sendMsg(bytes);
                        } else if (indw == 6) {//监测状态
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x45, (byte) 0xaa, 0x00, 0x4c,
                                    0x00,
                                    0x00,
                                    0x00,
                                    0x3F, 0x26, 0x66, 0x66,
                                    0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                    0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xC0, (byte) 0x20, (byte) 0x00, (byte) 0x00,//温度
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,//误差
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x00,
                                    (byte) 0x76, (byte) 0x41, 0x7d, 0x7d};
                            client.sendMsg(bytes);
                        }
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
            }
        });
    }

    int indw = 0;

    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(StandardGasConfigFrag.getInstance());
        fragments.add(RinseFrag.getInstance());
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
//        ChatController.getInstance().waitingForFriends();
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