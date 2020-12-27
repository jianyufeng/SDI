package com.puyu.mobile.sdi.server;

import com.puyu.mobile.sdi.LiveDataStateBean;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/8/11 11:18
 * desc   :
 * version: 1.0
 */
public class AccepThread extends Thread {

    /**
     * 服务端蓝牙Sokcet
     */
    private final ServerSocket mmServerSocket;


    private final int port;
    /**
     * 监听到有客户端连接，新建一个线程单独处理，不然在此线程中会堵塞
     */
    private ConnectedThread mConnectedThread;

    public AccepThread() throws IOException {
        this.port = Params.PORT;
        // 获取服务端蓝牙socket
        mmServerSocket = new ServerSocket(port);  //监听本机的54321端口
    }

    @Override
    public void run() {
        super.run();
        // 连接的客户端soacket
        Socket socket = null;
        // 服务端是不退出的,要一直监听连接进来的客户端，所以是死循环
        while (true) {
            try {
                // 获取连接的客户端socket
                //  EventBus.getDefault().post(new SeverStarted(Params.MSG_Server_start));
                LiveDataStateBean.getInstant().getWifiState().postValue(Params.MSG_Server_start);
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                // 通知主线程更新UI, 获取异常
                LiveDataStateBean.getInstant().getWifiState().postValue(Params.MSG_Server_ERROR);
                // EventBus.getDefault().post(new SeverErrorModelBean(Params.MSG_Server_ERROR));
//                mHandler.sendEmptyMessage();
                e.printStackTrace();
                // 服务端退出一直监听线程
                break;
            }
            // EventBus.getDefault().post(new ClientLinkSuccess(Params.click_link_success, socket.getRemoteDevice()));
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.click_link_success);

            // 管理连接的客户端socket
            manageConnectSocket(socket);
        }
    }

    /**
     * 管理连接的客户端socket
     *
     * @param socket
     */
    private void manageConnectSocket(Socket socket) {
        // 只支持同时处理一个连接
        // mConnectedThread不为空,踢掉之前的客户端
        if (mConnectedThread != null) {
            mConnectedThread.cancle();
        }

        // 新建一个线程,处理客户端发来的数据
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    /**
     * 断开服务端，结束监听
     */
    public void cancle() {
        try {
            //断开等待
            mmServerSocket.close();
            //断开已经连接的客户端
            if (mConnectedThread != null) {
                mConnectedThread.cancle();
            }
//            mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING);
//            EventBus.getDefault().post(new ServerFinishBean(Params.MSG_Server_finish));
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
