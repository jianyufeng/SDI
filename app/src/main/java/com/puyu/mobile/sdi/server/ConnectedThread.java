package com.puyu.mobile.sdi.server;

import android.util.Log;

import com.puyu.mobile.sdi.LiveDataStateBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/8/11 11:15
 * desc   :
 * version: 1.0
 */
public class ConnectedThread extends Thread {
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

    private String TAG = "ConnectedThread";

    public ConnectedThread(Socket socket) {
        mmSokcet = socket;
    }

    @Override
    public void run() {
        super.run();
        try {
            mmInputStream = mmSokcet.getInputStream();
            mmOutputStream = mmSokcet.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            //EventBus.getDefault().post(new CommunicateError(Params.communicate_link_error));
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.communicate_link_error);
            cancle();
            return;
        }
        byte[] buffer = new byte[1024];
        ArrayList<byte[]> bytes = new ArrayList<>();
        while (true) {
            try {
                // 读取数据
                int len = mmInputStream.read(buffer);
//                int available = mmInputStream.available();
//                String content;
                if (len > 0) {
                    byte[] r = new byte[len];
                    System.arraycopy(buffer, 0, r, 0, len);
                    bytes.add(r);
                    if (mmInputStream.available() == 0) { //一个请求
                        for (int i = 0; i < bytes.size(); i++) {
                            mmOutputStream.write(bytes.get(i));
                        }
                        mmOutputStream.flush();
                    }
                    Log.d(TAG, "run bytes.size: " + bytes.size());
                    bytes.clear();
                }
            } catch (Exception e) {//wifi 关闭会走异常 包括本地和服务端 不包括服务端软件关闭
                e.printStackTrace();
//                EventBus.getDefault().post(new CommunicateError(Params.communicate_link_error));
                LiveDataStateBean.getInstant().getWifiState().postValue(Params.communicate_link_error);
                cancle();
                break;
            }
        }

    }

    // 踢掉当前客户端
    public void cancle() {
        try {
            if (mmSokcet != null)
                mmSokcet.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端发送数据
     *
     * @param data
     */
    public void write(byte[] data) {
        try {

            mmOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            // EventBus.getDefault().post(new CommunicateError(Params.communicate_link_error));
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.communicate_link_error);
        }
    }
}
