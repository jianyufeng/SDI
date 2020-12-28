package com.puyu.mobile.sdi.netty;

import android.util.Log;

import com.puyu.mobile.sdi.server.Params;

import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/27 16:16
 * desc   :
 * version: 1.0
 */
public class NettyServer {
    private static final String TAG = "NettyServer";
    //端口
    private static final int PORT = Params.PORT;

    /**
     * 启动tcp服务端
     */
    public void startServer() {
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //添加发送数据编码器
                            pipeline.addLast(new ServerEncoder());
                            //添加解码器，对收到的数据进行解码
                            pipeline.addLast(new ServerDecoder());
                            //添加数据处理
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            //服务器启动辅助类配置完成后，调用 bind 方法绑定监听端口，调用 sync 方法同步等待绑定操作完成
            b.bind(PORT).sync();
            Log.d(TAG, "TCP 服务启动成功 PORT = " + PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerEncoder extends MessageToByteEncoder<Object> {

        private static final String TAG = "ServerEncoder";

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Object data, ByteBuf byteBuf) throws Exception {
            //自己发送过来的东西进行编码
            byteBuf.writeBytes(data.toString().getBytes());
        }
    }
    class ServerDecoder extends ByteToMessageDecoder {
        private static final String TAG = "ServerDecoder";

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            //收到的数据长度
            int length = byteBuf.readableBytes();
            //创建个byteBuf存储数据，进行编辑
            ByteBuf dataBuf = Unpooled.buffer(length);
            //写入收到的数据
            dataBuf.writeBytes(byteBuf);
            //将byteBuf转为数组
            String data = new String(dataBuf.array());
            Log.d(TAG, "收到了客户端发送的数据：" + data);
            //将数据传递给下一个Handler，也就是在NettyServer给ChannelPipeline添加的处理器
            list.add(data);
        }
    }
    class ServerHandler extends SimpleChannelInboundHandler<Object> {

        private static final String TAG = "ServerHandler";

        /**
         * 当收到数据的回调
         *
         * @param channelHandlerContext 封装的连接对像
         * @param o
         * @throws Exception
         */
        @Override
        protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            Log.d(TAG, "收到了解码器处理过的数据：" + o.toString());

            Channel channel = channelHandlerContext.channel();
            channel.writeAndFlush(o.toString());
        }

        /**
         * 有客户端连接过来的回调
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            Log.d(TAG, "有客户端连接过来：" + ctx.toString());
        }

        /**
         * 有客户端断开了连接的回调
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            Log.d(TAG, "有客户端断开了连接：" + ctx.toString());
        }


    }
}
