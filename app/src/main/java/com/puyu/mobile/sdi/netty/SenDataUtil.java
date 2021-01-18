package com.puyu.mobile.sdi.netty;

import com.puyu.mobile.sdi.bean.SendGasNameConfig;
import com.puyu.mobile.sdi.bean.SendRinseConfig;
import com.puyu.mobile.sdi.bean.SendStandConfig;
import com.puyu.mobile.sdi.util.AppCRC;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class SenDataUtil {


    public static void add20(ByteBuf byteBuf, String val, int fixedLen, boolean addLen) {
        byte[] name = val.getBytes(StandardCharsets.UTF_8);
        if (addLen) {
            byteBuf.writeByte(name.length);
        }
        byteBuf.writeBytes(name);
        for (int i = 0; i < fixedLen - name.length; i++) {
            byteBuf.writeByte(0x20);
        }
    }

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

    public static void sendGasName(SendGasNameConfig gasNameConfig) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x34);
        byteBuf.writeByte(0x66);
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

    }

    public static void sendGasConfig(SendStandConfig configSend) {
        System.out.println(configSend);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x33);
        byteBuf.writeByte(0x66);
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


    }

    public static void sendRinseConfig(SendRinseConfig configSend) {
        System.out.println(configSend);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(ProtocolParams.frameHead);
        byteBuf.writeBytes(ProtocolParams.sendAddr);
        byteBuf.writeByte(0x31);
        byteBuf.writeByte(0x66);
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


    }


}
