package com.puyu.mobile.sdi.server;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/22 14:32
 * desc   :
 * version: 1.0
 */
public class ListenerThread extends Thread{


    private final int port;
    private final Handler handler;
    private ServerSocket serverSocket = null;
    private Socket socket;
    public ListenerThread(int port, Handler handler) {
        setName("ListenerThread");
        this.port = port;
        this.handler = handler;
        try {
            serverSocket = new ServerSocket(port);//监听本机的12345端口
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        super.run();
        try {
            Log.i("ListennerThread", "阻塞");
            //阻塞，等待设备连接
            if (serverSocket != null)
                socket = serverSocket.accept();
            Message message = Message.obtain();
            message.what = Connect.DEVICE_CONNECTING;
            handler.sendMessage(message);
        } catch (IOException e) {
            Log.i("ListennerThread", "error:" + e.getMessage());
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return socket;
    }
}
