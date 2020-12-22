package com.puyu.mobile.sdi.server;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/22 14:37
 * desc   :
 * version: 1.0
 */
public class ConnectThread extends Thread {

    private final Socket socket;
    private final Handler handler;
    private final Context context;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ConnectThread(Context context, Socket socket, Handler handler) {
        setName("ConnectThread");
        Log.i("ConnectThread", "ConnectThread");
        this.socket = socket;
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        if (socket == null) {
            return;
        }
        handler.sendEmptyMessage(Connect.DEVICE_CONNECTED);
        try {
            //获取数据流
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {  //读取数据
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    final byte[] data = new byte[bytes];
                    System.arraycopy(buffer, 0, data, 0, bytes);

                    Message message = Message.obtain();
                    message.what = Connect.GET_MSG;
                    Bundle bundle = new Bundle();
                    bundle.putString("MSG", new String(data));
                    message.setData(bundle);
                    handler.sendMessage(message);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送数据
     */
    public void sendData(String msg) {
        Log.i("ConnectThread", "发送数据:" + (outputStream == null));
        if (outputStream != null) {
            try {
                outputStream.write(msg.getBytes());
                Log.i("ConnectThread", "发送消息：" + msg);
                Message message = Message.obtain();
                message.what = Connect.SEND_MSG_SUCCSEE;
                Bundle bundle = new Bundle();
                bundle.putString("MSG", new String(msg));
                message.setData(bundle);
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = Connect.SEND_MSG_ERROR;
                Bundle bundle = new Bundle();
                bundle.putString("MSG", new String(msg));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }
}
