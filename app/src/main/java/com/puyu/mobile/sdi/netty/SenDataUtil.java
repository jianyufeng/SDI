package com.puyu.mobile.sdi.netty;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class SenDataUtil {

    public static void add7D7B(byte[] data){
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(0x7d);
        byteBuf.writeByte((byte)0x7b);
        byteBuf.writeBytes(data);
        byteBuf.writeByte((byte)0x7d);
        byteBuf.writeByte((byte)0x7d);

        System.out.println(ByteBufUtil.hexDump(byteBuf));
    }
}
