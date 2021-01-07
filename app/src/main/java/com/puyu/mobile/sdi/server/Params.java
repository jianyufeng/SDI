package com.puyu.mobile.sdi.server;


public class Params {
    public static final int PORT = 54321;
    public static final String MSG_Server_start = "server_start"; //服务端启动
    public static final String MSG_Server_ERROR = "server_error"; //服务端异常
    public static final String MSG_Server_finish = "server_finis"; //服务端断开
    public static final String Receive_msg = "receive_msg";  //收到的消息
    public static final String click_link_error = "click_link_error";  //客户端连接失败，可能是已经配对的服务器未打开
    public static final String click_link_success = "click_link_success";  //客户端成功
    public static final String click_linking = "click_linking";  //客户端开始连接
    public static final String communicate_link_error = "communicate_link_error";  //交流断开

}
