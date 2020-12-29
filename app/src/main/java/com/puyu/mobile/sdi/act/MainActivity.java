package com.puyu.mobile.sdi.act;


import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.pgyersdk.update.PgyUpdateManager;
import com.puyu.mobile.sdi.BR;
import com.puyu.mobile.sdi.LiveDataStateBean;
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
import com.puyu.mobile.sdi.server.Params;
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
        binding.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.standard_gas_config:
                        binding.vp2.setCurrentItem(0, false);
                        break;
                    case R.id.rinse:
                        binding.vp2.setCurrentItem(1, false);
                        break;
                    case R.id.pressurize:
                        binding.vp2.setCurrentItem(2, false);
                        break;
                    case R.id.add_sample:
                        binding.vp2.setCurrentItem(3, false);
                        break;
                    case R.id.set:
                        binding.vp2.setCurrentItem(4, false);
                        indw++;
                        if (indw % 2 == 0) {

                            byte[] bytes = {0x44,0x41,0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x20, 0x55, 0x00, 0x00, 0x00};
                            client.sendMsg(bytes);
                        } else {

                            byte[] bytes = {0x20,0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x20, 0x55, 0x00, 0x00, 0x00, 0x7d, 0x7d};
                            client.sendMsg(bytes);

                        }
                        break;
                }
            }
        });

        LiveDataStateBean.getInstant().getWifiState().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String integer) {
                switch (integer) {
                    case Params.click_linking://客户端开始连接
                        binding.tvWifiState.setText("客户端开始连接");
                        //keyikongzhi = false;
                        break;
                    case Params.click_link_error://客户端连接失败，可能是已经配对的服务器未打开
                        binding.tvWifiState.setText("连接失败,设备未打开");
                        //keyikongzhi = false;
                        break;
                    case Params.click_link_success://连接成功
                        binding.tvWifiState.setText("连接成功");
                        //keyikongzhi = true;
                        break;
                    case Params.communicate_link_error://交流断开
                        binding.tvWifiState.setText("连接被断开");
                        // keyikongzhi = false;
                        break;
                    case Params.MSG_Server_ERROR://服务端异常
                        binding.tvWifiState.setText("服务端异常");
                        break;
                    case Params.MSG_Server_start: //服务端启动
                        binding.tvWifiState.setText("服务端启动");
                        break;
                }
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