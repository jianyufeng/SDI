package com.puyu.mobile.sdi.netty;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/30 10:08
 * desc   :
 * version: 1.0
 */
public class ProtocolParams {
    public static byte[] frameHead = new byte[]{0x7D, 0x7B};
    public static byte[] frameEnd = new byte[]{0x7D, 0x7D};
    public static byte[] frameFilter = new byte[]{0x7D, (byte) 0x82};


    /* public static byte phoneAddrLength = (byte) 0x01;
     public static byte phoneAddr = (byte) 0xF3;
     public static byte mcuAddrLebgth = (byte) 0x01;
     public static byte mcuAddr = (byte) 0xF1;*/
    public static byte[] receiveAddr = new byte[]{0x01, (byte) 0xF1, 0x01, (byte) 0xF3};

    public static byte[] sendAddr = new byte[]{0x01, (byte) 0xF3, 0x01, (byte) 0xF1};


    /**
     * +-----------------+--------+--------+-----------+
     * | 帧头+ | 目标路径长度（1字节）+  | 目标地址区域+ | 源路径长度（1字节）+
     * | 源地址区域+ | 命令码+ | 命令扩展码+ | 通讯数据长度（2字节）+ | 数据区+
     * | 16位CRC校验+ | 帧尾+ |
     * +-----------------+--------+--------+-----------+
     * 7d 7b 01 F1 01 F3 20 55 00 00 ??? 00 00 7d 7d
     * 2+ 1+ 1+ 1+ 1+ 1 +1+    2+  ?+      2+    2 = 14
     */
    public static int minLength = 14 - 4;//去掉帧头和帧尾 并且没有数据


    /**
     * | 命令码+ | 命令扩展码+ | 通讯数据长度（2字节）+ | 数据区+
     * 20 55 00 00 ???
     */
    //仪器ID 指令
    public static byte CMD_DEVICE_ID = (byte) 0x20;

    //软件版本号读取
    public static byte CMD_DEVICE_Version = (byte) 0x21;
    //仪器类型
    public static byte CMD_DEVICE_Type = (byte) 0x23;

    //加压方法设置
    public static byte CMD_ADD_Pressurize = (byte) 0x30;
    //加压方法设置 返回
    public static byte CMD_set_R_f = (byte) 0x11;//写 收到 失败 方法设置失败
    public static byte CMD_set_R_s = (byte) 0x12;//写 收到 成功 方法设置成功

    //冲洗方法设置
    public static byte CMD_rinse = (byte) 0x31;

    //加样
    public static byte CMD_add_samp = (byte) 0x32;
    //配气方法设置
    public static byte CMD_gas_config = (byte) 0x33;
    //配气 气体名称设置
    public static byte CMD_gas_name_config = (byte) 0x34;

    //校准
    public static byte CMD_calibration = (byte) 0x35;

    //压力上下限
    public static byte CMD_pressure_up_low = (byte) 0x36;

    //系统监控
    public static byte CMD_system_monitoring = (byte) 0x45;

    //命令扩展码
    public static byte CMD_Ex_R_S = (byte) 0x55;// 读 发送
    public static byte CMD_Ex_R_R = (byte) 0xaa;// 读 收到

    public static byte CMD_Ex_W_S = (byte) 0x66;//写 发送
    public static byte CMD_Ex_W_R = (byte) 0x99;//写 发送

    /**
     * 写命令成功：				0x88
     * 写命令失败：				0x99
     * 写入数据非法或者超限：	0xaa
     */
    public static byte CMD_Ex_W_R_f = (byte) 0x99;//写 收到 失败
    public static byte CMD_Ex_W_R_s = (byte) 0x88;//写 收到 成功
    public static byte CMD_Ex_W_R_e = (byte) 0xaa;//写 收到 非法或者超限


}
