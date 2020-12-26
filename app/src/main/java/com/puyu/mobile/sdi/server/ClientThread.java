package com.puyu.mobile.sdi.server;

import android.bluetooth.BluetoothAdapter;

import com.puyu.mobile.sdi.APP;
import com.puyu.mobile.sdi.LiveDataStateBean;
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
    private Socket mmSoket;

    private BluetoothAdapter mBluetoothAdapter;
    /**
     * 主线程通信的Handler
     */

    /**
     * 发送和接收数据的处理类
     */
    private ConnectedThread mConnectedThread;

    public ClientThread() {

    }

    @Override
    public void run() {
        super.run();
        try {
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.click_linking);
            // 连接服务器
            mmSoket = new Socket(IPUtil.getWifiRouteIPAddress(APP.getInstance()), Params.PORT);
        } catch (IOException e) {
            // 连接异常就关闭
            e.printStackTrace();
            //EventBus.getDefault().post(new ClientLinkError(Params.click_link_error));
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.click_link_error);
            cancle();
            return;
        }

//        EventBus.getDefault().post(new ClientLinkSuccess(Params.click_link_success,mmDevice));
        LiveDataStateBean.getInstant().getWifiState().postValue(Params.click_link_success);
        manageConnectedSocket(mmSoket);
    }

    private void manageConnectedSocket(Socket mmSoket) {

        // 新建一个线程进行通讯,不然会发现线程堵塞
       // mConnectedThread = new ConnectedThread(mmSoket);
      //  mConnectedThread.start();
        SocketConnected s = new SocketConnected(mmSoket);
    }

    /**
     * 关闭当前客户端
     */
    public void cancle() {
        try {
            if (mmSoket != null)
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
