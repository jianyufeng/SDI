package com.puyu.mobile.sdi.netty;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.SendGasNameConfig;
import com.puyu.mobile.sdi.bean.SendRinseConfig;
import com.puyu.mobile.sdi.bean.SendStandConfig;
import com.puyu.mobile.sdi.util.AppCRC;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class SenDataUtil {
    public static final byte[] getDeviceID = new byte[]{0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, ProtocolParams.CMD_DEVICE_ID,ProtocolParams.CMD_Ex_R_S, 0x00, 0x00, 0x7f, 0x1c, 0x7d, 0x7d};
    public static final byte[] getDeviceVersion = new byte[]{0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, ProtocolParams.CMD_DEVICE_Version, ProtocolParams.CMD_Ex_R_S, 0x00, 0x00, 0x7e, (byte) 0xe0, 0x7d, 0x7d};
    public static final byte[] getDeviceType = new byte[]{0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, ProtocolParams.CMD_DEVICE_Type, ProtocolParams.CMD_Ex_R_S, 0x00, 0x00, 0x7f, 0x58, 0x7d, 0x7d};
    public static final byte[] getDeviceLimit = new byte[]{0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, ProtocolParams.CMD_pressure_up_low, ProtocolParams.CMD_Ex_R_S, 0x00, 0x00, 0x7b, 0x54, 0x7d, 0x7d};
    public static final byte[] getDeviceMonitor = new byte[]{0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, ProtocolParams.CMD_system_monitoring, ProtocolParams.CMD_Ex_R_S, 0x00, 0x00, 0x61, (byte) 0xd0, 0x7d, 0x7d};


    //字符串 要添加空格
    public static void add20(ByteBuf byteBuf, String val, int fixedLen, boolean addLen) {
        byte[] name = val.getBytes(StandardCharsets.UTF_8);
        if (addLen) {
            byteBuf.writeShort(fixedLen);
        }
        byteBuf.writeBytes(name);
        for (int i = 0; i < fixedLen - name.length; i++) {
            byteBuf.writeByte(0x20);
        }
    }

    //检查协议中的7d 要添加0x82
    public static void add82(ByteBuf byteBuf) {
        //验证7d
        int index = 2;
        while ((index = byteBuf.indexOf(index, byteBuf.writerIndex() - 2, (byte) 0x7d)) != -1) {
            //有
            int length = byteBuf.writerIndex();
            byteBuf.writerIndex(index + 2);
            byteBuf.writeBytes(byteBuf, index + 1, length - index - 1);
            byteBuf.markWriterIndex();
            byteBuf.writerIndex(index + 1);
            byteBuf.writeByte(0x82);
            byteBuf.resetWriterIndex();
            index = index + 2;
            System.out.println(ByteBufUtil.hexDump(byteBuf));
        }

    }

    //发送获取仪器ID
    public static byte[] sendGetDeviceID() {
       /* ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x20);
        byteBuf.writeByte(0x55);
        byteBuf.writeShort(0);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        return bytes;*/
        //7d7b01f101f3205500007f1c7d7d
        return getDeviceID;
    }

    //发送设置仪器ID
    public static void sendSetDeviceID(String deviceID) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_DEVICE_ID);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        add20(byteBuf, deviceID, 12, true);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送设置仪器ID

    }

    //发送获取软件版本
    public static byte[] sendGetVersion() {
        /*ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x21);
        byteBuf.writeByte(0x55);
        byteBuf.writeShort(0);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        return bytes;*/
        //7d7b01f101f3215500007ee07d7d
        return getDeviceVersion;

    }

    //发送获取仪器类型
    public static byte[] sendGetDeviceType() {
        /*ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x23);
        byteBuf.writeByte(0x55);
        byteBuf.writeShort(0);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        return bytes;*/
        //7d7b01f101f3235500007f587d7d
        return getDeviceType;

    }

    //发送设置仪器类型
    public static byte[] sendSetDeviceType() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_DEVICE_Type);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(1);
        byteBuf.writeByte(0x00);//1字节(INT8U)仪器类型0x00:静态稀释仪
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        return bytes;
    }

    //发送加压指令
    public static void sendPressureConfig(boolean start, float targetVal) {
        System.out.println("start:" + start + " targetVal:" + targetVal);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_ADD_Pressurize);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(5);
        byteBuf.writeBoolean(start);
        byteBuf.writeFloat(targetVal);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送加压指令

    }

    //发送冲洗指令
    public static void sendRinseConfig(SendRinseConfig configSend) {
        System.out.println(configSend);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_rinse);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(17);
        byteBuf.writeBoolean(configSend.start);
        byteBuf.writeBoolean(configSend.pass1);
        byteBuf.writeBoolean(configSend.pass2);
        byteBuf.writeBoolean(configSend.pass3);
        byteBuf.writeBoolean(configSend.pass4);
        byteBuf.writeFloat(configSend.dRimsetime);
        byteBuf.writeFloat(configSend.sRimsetime);
        byteBuf.writeFloat(configSend.iimsetime);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送冲洗指令

    }

    //发送加样指令
    public static void sendAddSampConfig(boolean start, int passWitch, Float addSampvalue) {
        System.out.println("start:" + start + " addSampvalue:" + addSampvalue + " passWitch:" + passWitch);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_add_samp);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(10);
        byteBuf.writeBoolean(start);
        byteBuf.writeBoolean(passWitch == LiveDataStateBean.stand1PassNumber);
        byteBuf.writeBoolean(passWitch == LiveDataStateBean.stand2PassNumber);
        byteBuf.writeBoolean(passWitch == LiveDataStateBean.stand3PassNumber);
        byteBuf.writeBoolean(passWitch == LiveDataStateBean.stand4PassNumber);
        byteBuf.writeBoolean(passWitch == LiveDataStateBean.mulDiluentPassNumber);//多级稀释气
        byteBuf.writeFloat(addSampvalue);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送加样指令


    }

    //发送标气配置指令
    public static void sendGasConfig(SendStandConfig configSend) {
        System.out.println(configSend);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_gas_config);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(50);
        byteBuf.writeBoolean(configSend.start);
        byteBuf.writeBoolean(configSend.pass1);
        byteBuf.writeBoolean(configSend.pass2);
        byteBuf.writeBoolean(configSend.pass3);
        byteBuf.writeBoolean(configSend.pass4);
        byteBuf.writeBoolean(configSend.pass5);
        byteBuf.writeFloat(configSend.initV1);
        byteBuf.writeFloat(configSend.initV2);
        byteBuf.writeFloat(configSend.initV3);
        byteBuf.writeFloat(configSend.initV4);
        byteBuf.writeFloat(configSend.initV5);
        byteBuf.writeFloat(configSend.targetV1);
        byteBuf.writeFloat(configSend.targetV2);
        byteBuf.writeFloat(configSend.targetV3);
        byteBuf.writeFloat(configSend.targetV4);
        byteBuf.writeFloat(configSend.targetV5);
        byteBuf.writeFloat(configSend.targetPress);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送配气设置
    }

    //发送标气名称配置指令
    public static void sendGasName(SendGasNameConfig gasNameConfig) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_gas_name_config);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(84);
        add20(byteBuf, gasNameConfig.sGasName1, 20, true);
        add20(byteBuf, gasNameConfig.sGasName2, 20, true);
        add20(byteBuf, gasNameConfig.sGasName3, 20, true);
        add20(byteBuf, gasNameConfig.sGasName4, 20, true);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送配气名称
    }

    //发送压力校准
    public static void sendCheckPress0(float currentPress) {
        System.out.println("currentPress:" + currentPress);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_calibration);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(4);
        byteBuf.writeFloat(currentPress);//大气压力 Kpa
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送压力校准
    }

    //发送获取压力上下限
    public static byte[] sendGetPressLimit() {
        /*ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x36);
        byteBuf.writeByte(0x55);
        byteBuf.writeShort(0);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        return bytes;*/
        //7d7b01f101f3365500007b547d7d
        return getDeviceLimit;
    }

    //发送设置压力上下限
    public static void sendSetPressLimit(float upLimit, float lowLimit) {
        System.out.println("upLimit:" + upLimit + " lowLimit:" + lowLimit);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(ProtocolParams.CMD_pressure_up_low);
        byteBuf.writeByte(ProtocolParams.CMD_Ex_W_S);
        byteBuf.writeShort(8);
        byteBuf.writeFloat(upLimit);//0-50psia
        byteBuf.writeFloat(lowLimit);//0-50psia
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        //添加到发送协议数据的队列中
        LiveDataStateBean.getInstant().sendData.offer(bytes);//发送设置压力上下限
    }


    //获取系统监控
    public static byte[] sendGetMonitorState() {
       /* ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x45);
        byteBuf.writeByte(0x55);
        byteBuf.writeShort(0);
        byte[] crcByte = AppCRC.GetCRC(byteBuf, 2, byteBuf.readableBytes() - 2);
        byteBuf.writeBytes(crcByte);
        byteBuf.writeBytes(ProtocolParams.frameEnd);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        add82(byteBuf);
        System.out.println(ByteBufUtil.hexDump(byteBuf));
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        byteBuf.release();
        return bytes;*/
        //7d7b01f101f34555000061d07d7d
        return getDeviceMonitor;
    }


}
