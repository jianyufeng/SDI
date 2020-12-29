package com.puyu.mobile.sdi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/29 10:53
 * desc   :
 * version: 1.0
 */
public class IODecoder extends DelimiterBasedFrameDecoder {



    // 防止 沾包 分隔符
    private static ByteBuf delimiter = Unpooled.copiedBuffer(new byte[]{0x7d,0x7b});  // 沾包 分割符
    private static ByteBuf delimiter1 = Unpooled.copiedBuffer(new byte[]{0x7d,0x7d});  // 沾包 分割符

    public IODecoder(int maxFrameLength) {
        super(maxFrameLength, delimiter,delimiter1);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        Object decode = super.decode(ctx, buffer);
        return decode;

    }
}
