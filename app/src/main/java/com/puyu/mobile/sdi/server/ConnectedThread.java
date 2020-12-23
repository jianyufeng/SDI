package com.puyu.mobile.sdi.server;

import android.util.Log;

import com.puyu.mobile.sdi.viewmodel.MainViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/8/11 11:15
 * desc   :
 * version: 1.0
 */
public class ConnectedThread extends Thread {
    /**
     * 当前连接的客户端BluetoothSocket
     */
    private final Socket mmSokcet;
    /**
     * 读取数据流
     */
    private final InputStream mmInputStream;
    /**
     * 发送数据流
     */
    private final OutputStream mmOutputStream;
    /**
     * 与主线程通信Handler
     */
    private MainViewModel mHandler;
    private String TAG = "ConnectedThread";

    public ConnectedThread(Socket socket, MainViewModel handler) {
        mmSokcet = socket;
        mHandler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            //EventBus.getDefault().post(new CommunicateError(Params.communicate_link_error));
            handler.wifiState.postValue(Params.communicate_link_error);
        }
        mmInputStream = tmpIn;
        mmOutputStream = tmpOut;
    }

    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                // 读取数据
                int len = mmInputStream.read(buffer);
                int available = mmInputStream.available();
                String content;
                if (len > 0) {
                    byte[] r = new byte[len];
                    System.arraycopy(buffer, 0, r, 0, len);
                    Log.d(TAG, "run messge: " +new String(r));
//                    content = HexConvert.BinaryToHexString(r);
//                    content = new String(buffer, 0, len, "utf-8");
//                    EventBus.getDefault().post(new ReceiveMsgBean(Params.Receive_msg, content));
                    // 把数据发送到主线程, 此处还可以用广播
//                    Message message = mHandler.obtainMessage(Constant.MSG_GOT_DATA, data);
//                    mHandler.sendMessage(message);
                }else {
                    mmSokcet.sendUrgentData(1);
                }
                Log.d(TAG, "messge size :" + len);
            } catch (IOException e) {
                e.printStackTrace();
//                EventBus.getDefault().post(new CommunicateError(Params.communicate_link_error));
                mHandler.wifiState.postValue(Params.communicate_link_error);
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
            mHandler.wifiState.postValue(Params.communicate_link_error);
        }
    }
}
