package com.puyu.mobile.sdi.netty;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2020/12/31 16:42
 * desc   :
 * version: 1.0
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
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
        if (msg instanceof ByteBuf) {
            ByteBuf cmdData = (ByteBuf) msg;
            byte cmd = cmdData.getByte(0);//命令码
           if (cmd==ProtocolParams.CMD_DEVICE_ID){

           }

            //byte cmdEx = ((ByteBuf) msg).getByte(0);//命令扩展码
            //byte cmdEx = ((ByteBuf) msg).getByte(0);//获取数据


        }

        System.out.println("[Server]: " + ByteBufUtil.hexDump((ByteBuf) msg));
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
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        System.out.println("-------handlerAdded" + " 加入\n");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("-------handlerRemoved" + " 离开\n");

    }


}