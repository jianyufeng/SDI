package com.puyu.mobile.sdi.netty;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/29 11:28
 * desc   :
 * version: 1.0
 */
public class GT06MsgDecoder extends LengthFieldBasedFrameDecoder {

    private static final byte[] PACKET_START_EXT = new byte[]{0x7d, 0x7b};

    public GT06MsgDecoder() {
        super(1024, 8, 2, 0, 10);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        // 读取 ByteBuf 是根据 位数来读取的

            if (frame == null) {
                return null;
            }

            int frameLen = frame.readableBytes();

            // 起始位
            byte[] header = new byte[2];

            frame.readBytes(header);

            // 是否是0x7d 0x7b 开头的扩展包
            boolean extPacket = false;

            if (Arrays.equals(PACKET_START_EXT, header)) {
                extPacket = true;

            }

          /*  int contentLen = MessageUtils.getContentLen(frameLen, extPacket);

            // 跳过包长度
            frame.skipBytes(MessageUtils.getPacketSizeLen(extPacket));

            // 消息内容
            byte[] msgContent = new byte[contentLen];

            // 消息序列号
            byte[] sequence = new byte[GT06Constant.MESSAGE_SERIAL_LEN];

            // crc校验码
            byte[] crc = new byte[GT06Constant.CRC_ITU_LEN];

            // 终止符
            byte[] endDelimiter = new byte[GT06Constant.END_DELIMITER_LEN];

            return new RootMessage(action, sequence, msgContent);*/
        return frame;
    }
}
