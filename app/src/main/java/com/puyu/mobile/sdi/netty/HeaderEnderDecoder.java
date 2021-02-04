package com.puyu.mobile.sdi.netty;

import com.puyu.mobile.sdi.util.AppCRC;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/29 15:36
 * desc   :
 * version: 1.0
 */
public class HeaderEnderDecoder extends ByteToMessageDecoder {
    private final ByteBuf header;
    private final ByteBuf ender;
    private final ByteBuf filter;


    public HeaderEnderDecoder() {
        header = Unpooled.copiedBuffer(ProtocolParams.frameHead);
        ender = Unpooled.copiedBuffer(ProtocolParams.frameEnd);
        filter = Unpooled.copiedBuffer(ProtocolParams.frameFilter);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 显示十六进制的接收码
        System.out.println("协议收到数据为:" + ByteBufUtil.hexDump(in));

        //1.验证7D7B 7D7D;调用decode方法，去除帧头和帧尾
        ByteBuf childBuf = decode(in);
        if (childBuf == null) return;
        //2.验证最小长度已经去掉帧头和帧尾的 7d 7b 01 F1 01 F3 20 55 00 00 ??? 00 00 7d 7d
        int lengthD = childBuf.readableBytes();
        if (lengthD < ProtocolParams.minLength) {
            System.out.println("协议长度有问题 最小长度是:" + ProtocolParams.minLength + " 当前长度是:" + lengthD);
            return;
        }
        //3.验证 地址 及地址长度 发送方和接收方 主板F1  平板F3
        byte[] bytesAddr = new byte[4];
        childBuf.getBytes(0, bytesAddr);
        if (!Arrays.equals(ProtocolParams.receiveAddr, bytesAddr)) {
            System.out.println("协议地址有问题:" + ByteBufUtil.hexDump(bytesAddr));
            return;
        }
        //4.验证数据位长度 数据帧中包含多少个字节数据
        byte[] dateLen = new byte[2];
        childBuf.getBytes(6, dateLen);
        int len = (dateLen[0] << 8) + dateLen[1];
        System.out.println("验证数据长度----:" + ByteBufUtil.hexDump(dateLen));
        System.out.println("验证数据长度:" + len);
        if ((childBuf.readableBytes() - 10) != len) {
            System.out.println("数据长度不符,实际数据长度:" + (childBuf.readableBytes() - 10));
            return;
        }
        //5. 验证数据帧7D82
        System.out.println("协议数据去掉头尾----:" + ByteBufUtil.hexDump(childBuf));
        int index;
        while ((index = ByteBufUtil.indexOf(filter, childBuf)) != -1) {
            //有
            int length = childBuf.readableBytes();
            childBuf.writerIndex(index + 1);
            childBuf.writeBytes(childBuf, index + filter.capacity(), length - childBuf.writerIndex() - 1);
            System.out.println("协议数据去掉7D82中的82----:" + ByteBufUtil.hexDump(childBuf));
        }
        //6.验证CRC校验
        //CRC校验
        byte[] crc = new byte[2];
        childBuf.getBytes(childBuf.readableBytes() - 2, crc);
        byte[] crcByte = AppCRC.GetCRC(childBuf,0,childBuf.readableBytes()-2);
        if (!Arrays.equals(crc, crcByte)) {
            System.out.println("帧CRC校验失败: 验证CRC:" + ByteBufUtil.hexDump(crc) + " 本地计算的CRC：" + ByteBufUtil.hexDump(crcByte));
            return;
        }
        // 如果获得有效数据
        // 将有效数据备份加入接收列表
        out.add(childBuf.copy(4,childBuf.readableBytes()-6));
    }


    /**
     * +-----------------+--------+--------+-----------+
     * |       帧=        |帧头+    |数据帧+   |帧尾     |
     * +-----------------+--------+--------+-----------+
     */
    /***
     * 利用帧头和帧尾取出数据帧，过滤帧头前无效数据，过滤没有帧头和帧尾的数据。
     * @param  buf:字节缓冲区
     * @return ByteBuf:数据帧（去掉帧头和帧尾）
     *
     *
     *  */
    protected ByteBuf decode(ByteBuf buf) {
        // 帧头起始位置
        int sliceStart = 0;
        // 帧尾起始位置
        int sliceEnd = 0;
        // 数据帧
        ByteBuf frame = null;
        // 帧头是存在
        if (header != null) {
            // 获取帧头位置
            int index = ByteBufUtil.indexOf(header, buf);
            // 帧头第一次出现位置找到
            if (index > -1 && index < buf.capacity()) {
                // 设置帧头位置读取 丢弃之前的数据
                buf.readerIndex(index);
                // 将帧头位置保存
                sliceStart = index;
            }
        }
        // 帧尾存在
        if (ender != null) {
            // 获取帧尾的起始位置
            int endindex = ByteBufUtil.indexOf(ender, buf);
            // 帧尾找到，并且在帧头的后面
            if (endindex > -1 && endindex > sliceStart && endindex < buf.capacity()) {
                // 保存帧尾的位置
                sliceEnd = endindex;
                // 计算数据帧的长度：帧尾的起始位置-帧头的起始位置-帧头的长度
                int length = sliceEnd - sliceStart - header.readableBytes();
                // 获取数据子帧
                frame = buf.slice(sliceStart + header.readableBytes(), length);
                // 将reader索引设定到帧尾的后面
                buf.skipBytes(sliceEnd - sliceStart + ender.readableBytes());
                // 将数据帧返回
                return frame;
            }
        }
        //去掉剩余的垃圾数据
        return null;
    }
}
