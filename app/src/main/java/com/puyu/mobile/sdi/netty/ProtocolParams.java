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
     *| 命令码+ | 命令扩展码+ | 通讯数据长度（2字节）+ | 数据区+
     * 20 55 00 00 ???
     */
    //仪器ID 指令
    public static byte CMD_DEVICE_ID=  (byte) 0x20;



    //命令扩展码
    public static byte CMD_Ex_R=  (byte) 0x55;//读
    public static byte CMD_Ex_W=  (byte) 0x66;//写

    //写回复状态
    public static byte WRITE_FAIL=  (byte) 0x99;// 写命令失败
    public static byte WRITE_SUCCESS=  (byte) 0x88;//写命令成功
    public static byte Write_ERROR=  (byte) 0xaa;//写入数据非法或者超限



}
