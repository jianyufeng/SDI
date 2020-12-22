package com.puyu.mobile.sdi.server;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/22 14:35
 * desc   :
 * version: 1.0
 */
public class Connect {
    public static final int DEVICE_CONNECTING = 1;//有设备正在连接热点
    public static final int DEVICE_CONNECTED  = 2;//有设备连上热点
    public static final int SEND_MSG_SUCCSEE  = 3;//发送消息成功
    public static final int SEND_MSG_ERROR    = 4;//发送消息失败
    public static final int GET_MSG           = 6;//获取新消息
}
