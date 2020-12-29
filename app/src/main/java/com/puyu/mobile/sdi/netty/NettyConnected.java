package com.puyu.mobile.sdi.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/26 15:51
 * desc   :
 * version: 1.0
 */

public class NettyConnected extends Thread {
    public static String host = "192.168.43.1";
    public static int port = 54321;
    private Bootstrap bootstrap;
    private Channel channel;

    public NettyConnected() {
    }

    @Override
    public void run() {
        super.run();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        bootstrap = new Bootstrap()
                .group(worker)// 指定EventLoopGroup
                .channel(NioSocketChannel.class)// 指定channel类型
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        //添加心跳处理Handler
                        pipeline.addLast("timeOut", new IdleStateHandler(10, 5, 0));
                        pipeline.addLast("frame", new HeaderEnderDecoder());
                        pipeline.addLast("encoder", new ByteArrayEncoder());
                      //  pipeline.addLast("frame1", new GT06MsgDecoder());
                        pipeline.addLast("handler", new ClientHandler(NettyConnected.this)); //添加数据处理器
                    }
                });// 指定Handler
        // 连接到服务端
        ChannelFuture channelFuture = bootstrap.connect(host,
                port);
        // 添加连接状态监听
        channelFuture.addListener(new ConnectListener(NettyConnected.this));
        try {
            channel = channelFuture.sync().channel(); //获取连接通道
            System.out.println("客户端首次连接成功");
        } catch (Exception e) {
            System.out.println("客户端首次连接失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
        System.out.println("重新连接中------：");
        // 添加连接状态监听
        channelFuture.addListener(new ConnectListener(NettyConnected.this));
        try {
            channel = channelFuture.sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("重新连接失败：" + e.getMessage());
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (bootstrap != null) {
            bootstrap.group().shutdownGracefully();
        }
        System.out.println("关闭连接：");

    }

    public void sendMsg(byte[] data) {
        if (channel != null) {
            channel.writeAndFlush(data).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    boolean success = future.isSuccess();
                    boolean done = future.isDone();
                    System.out.println("发送：future.isSuccess()：" + future.isSuccess());
                    System.out.println("发送：future.isDone()：" + future.isDone());
                }
            });
        }
    }

    class ClientHandler extends SimpleChannelInboundHandler<Object> {
        NettyConnected clientInitializer;

        public ClientHandler(NettyConnected clientInitializer) {
            this.clientInitializer = clientInitializer;
        }

        /**
         * 5s重连一次服务端
         */
        private void reConnect(final ChannelHandlerContext ctx) {
            EventLoop loop = ctx.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("连接断开，发起重连");
                    clientInitializer.reConnect();
                }
            }, 5, TimeUnit.SECONDS);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            super.userEventTriggered(ctx, evt);
            System.out.println("-------userEventTriggered" + "  超时\n");
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.WRITER_IDLE) {
                  //  byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf1, 0x01, (byte) 0xf3, 0x20, 0x55, 0x00, 0x00, 0x7d, 0x7d};
                  //  channel.writeAndFlush(bytes);
                    System.out.println("-------userEventTriggered WRITER_IDLE" + "  超时\n");
                    // TODO: 2018/6/13
                    //ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else if (e.state() == IdleState.READER_IDLE) {
                    System.out.println("-------userEventTriggered READER_IDLE" + "  超时\n");

                } else if (e.state() == IdleState.ALL_IDLE) {
                    System.out.println("-------userEventTriggered ALL_IDLE" + "  超时\n");
                }
            }
        }

        /**
         * 当收到数据的回调
         *
         * @param ctx 封装的连接对像
         * @param msg
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

            System.out.println("[Server]: " + msg);
        }

        /**
         * 与服务端连接成功的回调
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);//
            Channel channel = ctx.channel();
            System.out.println("-------channelActive" + "  在线\n");

        }

        /**
         * 与服务端断开的回调
         *
         * @param ctx
         * @throws Exception
         */
        @Override //这里是断线要进行的操作
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            Channel channel = ctx.channel();
            System.out.println("-------channelInactive" + "  离线\n");
            //启动重连
            reConnect(ctx);
        }


        //这里是出现异常的话要进行的操作
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            Channel channel = ctx.channel();
            System.out.println("-------exceptionCaught" + "  异常\n");
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx)   {
            Channel channel = ctx.channel();
            System.out.println("-------handlerAdded" + " 加入\n");

        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx)   {
            System.out.println("-------handlerRemoved" + " 离开\n");

        }



    }


    private class ConnectListener implements ChannelFutureListener {
        private NettyConnected nettyClient;

        public ConnectListener(NettyConnected nettyClient) {
            this.nettyClient = nettyClient;
        }

        @Override
        public void operationComplete(ChannelFuture channelFuture) {
            //连接失败发起重连
            System.out.println("连接状态监听：连接状态 " + channelFuture.isSuccess());
            if (!channelFuture.isSuccess()) {//重新连接
                final EventLoop loop = channelFuture.channel().eventLoop();
                loop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("连接状态监听中，发起重新连---");
                        nettyClient.reConnect();
                    }
                }, 5, TimeUnit.SECONDS);
            }
        }
    }
}
