package com.puyu.mobile.sdi.server;

import com.puyu.mobile.sdi.LiveDataStateBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/26 15:51
 * desc   :
 * version: 1.0
 */

public class SocketConnected {
    /**
     * 当前连接的客户端Socket
     */
    private final Socket mmSokcet;
    /**
     * 读取数据流
     */
    private InputStream mmInputStream;
    /**
     * 发送数据流
     */
    private OutputStream mmOutputStream;


    public SocketConnected(Socket socket) {
        mmSokcet = socket;
        try {
            mmInputStream = socket.getInputStream();
            mmOutputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.communicate_link_error);
            return;
        }
        new ReceiveThread().start();
        new SendThread().start();
    }

    class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();


        }
    }

    class SendThread extends Thread {
        @Override
        public void run() {
            super.run();

        }
    }
}
