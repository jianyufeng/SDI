package com.puyu.mobile.sdi.act;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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
import com.puyu.mobile.sdi.server.Connect;
import com.puyu.mobile.sdi.server.ConnectThread;
import com.puyu.mobile.sdi.server.ListenerThread;
import com.puyu.mobile.sdi.viewmodel.MainViewModel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity1 extends BaseActivity<ActivityMainBinding, MainViewModel> {



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
                        break;
                }
            }
        });



        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //检查Wifi状态
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);
        binding.tvCurrentPressureVal.setText("已连接到：" + wifiManager.getConnectionInfo().getSSID() +
                "\nIP:" /*+ getIp()*/
                + "\n路由：" + getWifiRouteIPAddress(MainActivity1.this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(getWifiRouteIPAddress(MainActivity1.this), PORT);
                    connectThread = new ConnectThread(MainActivity1.this,socket, handler);
                    connectThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.tvCurrentPressureVal.setText("通信连接失败");
                        }
                    });

                }
            }
        }).start();


     //   listenerThread = new ListenerThread(PORT, handler);
      //  listenerThread.start();


      /*listenerThread = new ListenerThread(PORT, handler);
        listenerThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //        开启连接线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("ip", "getWifiApIpAddress()" + getWifiApIpAddress());
                    //本地路由开启通信
                    String ip = getWifiApIpAddress();
                    if (ip != null) {
                    } else {
                        ip = "192.168.43.1";
                    }
                    Socket socket = new Socket(ip, PORT);
                    connectThread = new ConnectThread(MainActivity1.this, socket, handler);
                    connectThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.tvCurrentPressureVal.setText("创建通信失败");
                        }
                    });

                }
            }
        }).start();*/

    }

    /**
     * wifi获取 已连接网络路由  路由ip地址
     * @param context
     * @return
     */
    private static String getWifiRouteIPAddress(Context context) {
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        //        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        //        System.out.println("Wifi info----->" + wifiinfo.getIpAddress());
        //        System.out.println("DHCP info gateway----->" + Formatter.formatIpAddress(dhcpInfo.gateway));
        //        System.out.println("DHCP info netmask----->" + Formatter.formatIpAddress(dhcpInfo.netmask));
        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
        Log.i("route ip", "wifi route ip：" + routeIp);
        return routeIp;
    }


    private WifiManager   wifiManager;
    /**
     * 监听线程
     */
    private ListenerThread listenerThread;
    /**
     * 连接线程
     */
    private ConnectThread connectThread;
    /**
     * 端口号
     */
    private static final int    PORT              = 54321;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Connect.DEVICE_CONNECTING:
                    connectThread = new ConnectThread(getApplicationContext(),listenerThread.getSocket(), handler);
                    connectThread.start();
                    break;
                case Connect.DEVICE_CONNECTED:
                    binding.tvCurrentPressureVal.setText("设备连接成功");
                    break;
                case Connect.SEND_MSG_SUCCSEE:
                    binding.tvCurrentPressureVal.setText("发送消息成功:" + msg.getData().getString("MSG"));
                    break;
                case Connect.SEND_MSG_ERROR:
                    binding.tvCurrentPressureVal.setText("发送消息失败:" + msg.getData().getString("MSG"));
                    break;
                case Connect.GET_MSG:
                    binding.tvCurrentPressureVal.setText("收到消息:" + msg.getData().getString("MSG"));
                    break;
            }
        }
    };

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d("Main", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Main", ex.toString());
        }
        return null;
    }


}