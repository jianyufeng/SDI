package com.puyu.mobile.sdi.netty;

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
    private ByteBuf header;
    private ByteBuf ender;


    public HeaderEnderDecoder() {
        byte[] head = new byte[]{0x7D, 0x7B};
        byte[] end = new byte[]{0x7D, 0x7D};
        header = Unpooled.copiedBuffer(head);
        ender = Unpooled.copiedBuffer(end);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 显示十六进制的接收码
        System.out.println("协议收到数据为:" + ByteBufUtil.hexDump(in));
        // 调用decode方法，去除帧头和帧尾
        ByteBuf childBuf = decode(in);
        // 如果获得有效数据
        if (childBuf != null) {
            // 将有效数据备份加入接收列表
            out.add(childBuf.copy());
        }
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
                // 设置帧头位置读取
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
        if (sliceEnd == -1) {
            //将可读数据设置为0
            buf.skipBytes(buf.readableBytes());
        }
        return null;
    }
}
