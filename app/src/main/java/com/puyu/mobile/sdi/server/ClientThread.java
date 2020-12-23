package com.puyu.mobile.sdi.server;

import android.bluetooth.BluetoothAdapter;

import com.puyu.mobile.sdi.APP;
import com.puyu.mobile.sdi.viewmodel.MainViewModel;
import com.puyu.mobile.util.IPUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/8/11 11:17
 * desc   :
 * version: 1.0
 */
public class ClientThread extends Thread {
     /**
     * 客户端socket
     */
    private  Socket mmSoket;

    private BluetoothAdapter mBluetoothAdapter;
    /**
     * 主线程通信的Handler
     */
    private final MainViewModel mHandler;
    /**
     * 发送和接收数据的处理类
     */
    private ConnectedThread mConnectedThread;

    public ClientThread(MainViewModel mUIhandler) {
        mHandler = mUIhandler;
    }

    @Override
    public void run() {
        super.run();
        try {
            // 连接服务器
            mmSoket= new Socket(IPUtil.getWifiRouteIPAddress(APP.getInstance()), Params.PORT);
        } catch (IOException e) {
            // 连接异常就关闭
            e.printStackTrace();
            //EventBus.getDefault().post(new ClientLinkError(Params.click_link_error));
            mHandler.wifiState.postValue(Params.click_link_error);
            try {
                if (mmSoket!=null){
                    mmSoket.close();
                }
            } catch (IOException e1) {
                e.printStackTrace();
            }
            return;
        }

//        EventBus.getDefault().post(new ClientLinkSuccess(Params.click_link_success,mmDevice));
        mHandler.wifiState.postValue(Params.click_link_success);
        manageConnectedSocket(mmSoket);
    }

    private void manageConnectedSocket(Socket mmSoket) {

        // 新建一个线程进行通讯,不然会发现线程堵塞
        mConnectedThread = new ConnectedThread(mmSoket, mHandler);
        mConnectedThread.start();
    }

    /**
     * 关闭当前客户端
     */
    public void cancle() {
        try {
            mmSoket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     *
     * @param data
     */
    public void sendData(byte[] data) {
        if (mConnectedThread != null) {
            mConnectedThread.write(data);
        }
    }

}
