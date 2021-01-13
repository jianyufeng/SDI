package com.puyu.mobile.sdi.netty;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.LinkStateEnum;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

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
                        //pipeline.addLast("timeOut", new IdleStateHandler(10, 5, 0));
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
            System.out.println("客户端首次开始连接");
            channel = channelFuture.sync().channel(); //获取连接通道
            System.out.println("客户端首次连接成功");
            LiveDataStateBean.getInstant().wifiState.postValue(LinkStateEnum.LinkSuccess);
        } catch (Exception e) {
            System.out.println("客户端首次连接失败：" + e.getMessage());
            LiveDataStateBean.getInstant().wifiState.postValue(LinkStateEnum.LinkFail);
            e.printStackTrace();
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        System.out.println("重新连接中------：");
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
        // 添加连接状态监听
        channelFuture.addListener(new ConnectListener(NettyConnected.this));
        try {
            channel = channelFuture.sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("重新连接失败：" + e.getMessage());
            LiveDataStateBean.getInstant().wifiState.postValue(LinkStateEnum.LinkFail);

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
            bootstrap.config().group().shutdownGracefully();
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
                LiveDataStateBean.getInstant().wifiState.postValue(LinkStateEnum.LinkFail);
                final EventLoop loop = channelFuture.channel().eventLoop();
                loop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("连接状态监听中，发起重新连---");
                        nettyClient.reConnect();
                    }
                }, 5, TimeUnit.SECONDS);
            } else {
                LiveDataStateBean.getInstant().wifiState.postValue(LinkStateEnum.LinkSuccess);
            }
        }
    }
}
