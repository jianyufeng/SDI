package com.puyu.mobile.sdi.server;

import com.puyu.mobile.sdi.LiveDataStateBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/8/11 11:15
 * desc   :
 * version: 1.0
 */
public class ChatController {
    /**
     * 客户端的线程
     */
    private ClientThread mConnectThread;
    /**
     * 服务端的线程
     */
    private AccepThread mAccepThread;
    private ChatProtocol mProtocol = new ChatProtocol();

    /**
     * 网络协议的处理函数
     */
    private class ChatProtocol {
        private static final String CHARSET_NAME = "utf-8";

        /**
         * 封包（发送数据）
         * 把发送的数据变成  数组 2进制流
         */
        public byte[] encodePackge(String data) {
            if (data == null) {
                return new byte[0];
            } else {
                try {
                    return data.getBytes(CHARSET_NAME);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new byte[0];
                }
            }
        }

        /**
         * 解包（接收处理数据）
         * 把网络上数据变成自己想要的数据体
         */
        public String decodePackage(byte[] netData) {
            if (netData == null) {
                return "";
            } else {
                try {
                    return new String(netData, CHARSET_NAME);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }


    }

    /**
     * 与服务器连接进行聊天
     */
    public void startChatWith() {
        mConnectThread = new ClientThread();
        mConnectThread.start();
    }

    /**
     * 等待客户端来连接
     * handler : 用来跟主线程通信，更新UI用的
     */
    public void waitingForFriends() {
        try {
            mAccepThread = new AccepThread();
            mAccepThread.start();
        } catch (IOException e) {
            e.printStackTrace();
//            EventBus.getDefault().post(new SeverErrorModelBean(Params.MSG_Server_ERROR));
            LiveDataStateBean.getInstant().getWifiState().postValue(Params.MSG_Server_ERROR);
        }
    }

    /**
     * 发出消息
     */
    public void sendMessage(String msg) {
        // 封包
        byte[] data = mProtocol.encodePackge(msg);
        sendMessage(data);
    }

    /**
     * 发出消息
     */
    public void sendMessage(byte[] data) {
        if (mConnectThread != null) {
            mConnectThread.sendData(data);
        } else if (mAccepThread != null) {
            mAccepThread.sendData(data);
        }
    }

    /**
     * 网络数据解码
     */
    public String decodeMessage(byte[] data) {
        return mProtocol.decodePackage(data);
    }

    /**
     * 停止聊天
     */
    public void stopChart() {
        if (mConnectThread != null) {
            mConnectThread.cancle();
        }
        if (mAccepThread != null) {
            mAccepThread.cancle();
        }
    }


    /**
     * 以下是单例写法
     */
    private static class ChatControlHolder {
        private static ChatController mInstance = new ChatController();
    }

    public static ChatController getInstance() {
        return ChatControlHolder.mInstance;
    }
}
