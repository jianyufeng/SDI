package com.puyu.mobile.sdi.netty;

import android.util.Log;

import com.puyu.mobile.sdi.server.Params;
import com.puyu.mobile.sdi.util.AppCRC;

import java.util.Arrays;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
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
import io.netty.handler.codec.bytes.ByteArrayEncoder;

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


    public NettyServer() {
    }

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
                            //pipeline.addLast(new ServerEncoder());
                            //添加解码器，对收到的数据进行解码
                            pipeline.addLast("frame",new ServerDecoder());
                            pipeline.addLast("encoder", new ByteArrayEncoder());
                            //添加数据处理
                            pipeline.addLast("handler", new ServerHandler()); //添加数据处理器
                        }
                    });
            //服务器启动辅助类配置完成后，调用 bind 方法绑定监听端口，调用 sync 方法同步等待绑定操作完成
            ChannelFuture f = b.bind(PORT).sync();
            Log.d(TAG, "TCP 服务启动成功 PORT = " + PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerEncoder extends MessageToByteEncoder<Object> {

        private static final String TAG = "ServerEncoder";

        @Override
        protected void encode(ChannelHandlerContext channelHantendlerCoxt, Object data, ByteBuf byteBuf) throws Exception {
            //自己发送过来的东西进行编码
          //  byteBuf.writeBytes(data.toString().getBytes());

        }
    }

    class ServerDecoder extends ByteToMessageDecoder {
        private static final String TAG = "ServerDecoder";
        private final ByteBuf header;
        private final ByteBuf ender;
        private final ByteBuf filter;

        public ServerDecoder() {
            header = Unpooled.copiedBuffer(ProtocolParams.frameHead);
            ender = Unpooled.copiedBuffer(ProtocolParams.frameEnd);
            filter = Unpooled.copiedBuffer(ProtocolParams.frameFilter);
        }

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
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
            if (!Arrays.equals(ProtocolParams.sendAddr, bytesAddr)) {
                System.out.println("协议地址有问题:" + ByteBufUtil.hexDump(bytesAddr));
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

            //6.验证CRC校验
            //CRC校验
            byte[] crc = new byte[2];
            childBuf.getBytes(childBuf.readableBytes() - 2, crc);
            byte[] crcByte = AppCRC.GetCRC(childBuf, 0, childBuf.readableBytes() - 2);
            if (!Arrays.equals(crc, crcByte)) {
                System.out.println("帧CRC校验失败: 验证CRC:" + ByteBufUtil.hexDump(crc) + " 本地计算的CRC：" + ByteBufUtil.hexDump(crcByte));
                return;
            }
            // 如果获得有效数据
            // 将有效数据备份加入接收列表
            out.add(childBuf.copy(4, childBuf.readableBytes() - 6));
        }

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


    class ServerHandler extends SimpleChannelInboundHandler<Object> {

        private static final String TAG = "ServerHandler";

        /**
         * 当收到数据的回调
         *
         * @param channelHandlerContext 封装的连接对像
         * @param msg
         * @throws Exception
         */
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
            if (msg instanceof ByteBuf) {
                ByteBuf cmdData = (ByteBuf) msg;
                byte cmd = cmdData.getByte(0);//命令码
                byte rw = cmdData.getByte(1);//是读还是写
                byte[] date = new byte[cmdData.readableBytes() - 4];
                cmdData.getBytes(4, date);
                if (cmd == ProtocolParams.CMD_DEVICE_ID) { //仪器ID
                    type = 0;
                    if (rw == ProtocolParams.CMD_Ex_R_S) { //读取仪器ID 返回 12个字节
                        //仪器ID
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x20, (byte) 0xaa, 0x00, 0x0C,
                                0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, (byte) 0x93, (byte) 0x04, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //写入仪器ID 返回 1个字节

                        if (date.length == 12) {
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x20, (byte) 0x99, 0x00, 0x01,
                                    (byte) 0x88, (byte) 0xE3, (byte) 0x7D, (byte) 0x82, 0x7d, 0x7d};
                            channelHandlerContext.channel().writeAndFlush(bytes);
                            //写入成功
                            System.out.println("仪器ID 写入成功:" + date.length);

                           /* byte[] byte1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x20, (byte) 0x99, 0x00, 0x01,
                                    (byte) 0x88, (byte) 0x23, (byte) 0x71, 0x7d, 0x7d};
                            channelHandlerContext.channel().writeAndFlush(byte1);

                            //写入失败
                            System.out.println("仪器ID 写入失败:" + date.length);
                            byte[] byte2 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x20, (byte) 0x99, 0x00, 0x01,
                                    (byte) 0xaa, (byte) 0x63, (byte) 0x64, 0x7d, 0x7d};
                            channelHandlerContext.channel().writeAndFlush(byte2);
                            //写入出错
                            System.out.println("仪器ID 写入出错:" + date.length);*/

                        }
                    }
                } else if (cmd == ProtocolParams.CMD_DEVICE_Version) { //软件版本号读取
                    type = 0;
                    if (rw == ProtocolParams.CMD_Ex_R_S) { //软件版本号读取 返回 32个字节
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x21, (byte) 0xaa, 0x00, 0x20,
                                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                                0x4d, 0x43, 0x55, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
                                0x38, 0x39, 0x61, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
                                (byte) 0x1e, (byte) 0x2b, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);

                    }

                } else if (cmd == ProtocolParams.CMD_DEVICE_Type) { //仪器类型

                    if (rw == ProtocolParams.CMD_Ex_R_S) { //仪器类型 返回 1个字节 0x00:静态稀释仪
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x23, (byte) 0xaa, 0x00, 0x01,
                                0x00,
                                (byte) 0xa8, (byte) 0x5f, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //写入仪器类型 返回 1个字节
                        if (date.length == 1) {
                            if (date[0] == ProtocolParams.CMD_Ex_W_R_s) {
                                //写入成功
                                System.out.println("写入仪器类型 写入成功:" + date.length);

                            } else if (date[0] == ProtocolParams.CMD_Ex_W_R_f) {
                                //写入失败
                                System.out.println("写入仪器类型 写入失败:" + date.length);

                            } else if (date[0] == ProtocolParams.CMD_Ex_W_R_e) {
                                //写入出错
                                System.out.println("写入仪器类型 写入出错:" + date.length);

                            }
                        }
                    }
                } else if (cmd == ProtocolParams.CMD_ADD_Pressurize) { //加压方法设置

                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //获取数据
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //加压方法设置 返回 1个字节
                        type = 2;

                        run = !run;
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x30, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0xA2, (byte) 0xC1, 0x7d, 0x7d};

                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("加压方法设置 成功:" + date.length);

                     /*   byte[] bytes1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x30, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x11, (byte) 0xE2, (byte) 0xD4, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes1);
                        //写入失败
                        System.out.println("加压方法设置 失败:" + date.length);*/


                    }
                } else if (cmd == ProtocolParams.CMD_rinse) { //冲洗方法设置
                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //获取数据
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //冲洗方法设置 返回 1个字节
                        type = 3;
                        run = !run;
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x31, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0x9F, (byte) 0x01, 0x7d, 0x7d};

                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("冲洗方法设置 成功:" + date.length);

                    /*    byte[] bytes1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x30, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x11, (byte) 0xDF, (byte) 0x14, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes1);
                        //写入失败
                        System.out.println("冲洗方法设置 失败:" + date.length);*/


                    }
                } else if (cmd == ProtocolParams.CMD_add_samp) { //加样
                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //获取数据
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //加样方法设置 返回 1个字节
                        type = 4;

                        run = !run;
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x32, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0xDB, (byte) 0x01, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("加样方法 成功:" + date.length);
/*

                        byte[] bytes1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x32, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x11, (byte) 0x9B, (byte) 0x14, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes1);
                        //写入失败
                        System.out.println("加样方法 失败:" + date.length);
*/

                    }
                } else if (cmd == ProtocolParams.CMD_gas_config) { //配气方法设置
                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //获取数据
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //配气方法设置 返回 1个字节
                        run = !run;
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x33, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0xE6, (byte) 0xC1, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("配气方法设置 成功:" + date.length);
                        type = 1;
                       /* byte[] bytes1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x33, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0xA6, (byte) 0xD4, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes1);
                        //写入失败
                        System.out.println("配气方法设置 失败:" + date.length);*/
                    }
                } else if (cmd == ProtocolParams.CMD_gas_name_config) { //配气 气体名称设置
                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //获取数据
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //配气 气体名称设置 返回 1个字节

                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x34, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x88, (byte) 0xD3, (byte) 0x7E, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("配气 气体名称设置 写入成功:" + date.length);
 /*
                        byte[] bytes1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x34, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x88, (byte) 0x13, (byte) 0x72, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes1);

                        //写入失败
                        System.out.println("配气 气体名称设置 写入失败:" + date.length);
                        byte[] bytes2 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x34, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x88, (byte) 0x53, (byte) 0x67, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes2);

                        //写入出错
                        System.out.println("配气 气体名称设置 写入出错:" + date.length);*/

                    }
                } else if (cmd == ProtocolParams.CMD_calibration) { //压力校准 设置
                    type = 0;

                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //获取数据
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //校准设置 返回 1个字节
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x35, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0x6E, (byte) 0xC1, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("校准 成功:" + date.length);
                       /* byte[] bytes1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x35, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0x2E, (byte) 0xD4, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes1);
                        //写入失败
                        System.out.println("校准 失败:" + date.length);*/
                    }
                } else if (cmd == ProtocolParams.CMD_pressure_up_low) { //压力上下限 设置
                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        //读取压力上下限 返回 8 个字节 4字节(FP32)上限压力(0-50psia)4字节(FP32)下限压力(0-1psia)
                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x36, (byte) 0xaa, 0x00, 0x08,
                                0x42, 0x5E, 0x33, 0x33,
                                0x3E, 0x19, (byte) 0x99, (byte) 0x99,
                                (byte) 0xcf, (byte) 0x38, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                    } else if (rw == ProtocolParams.CMD_Ex_W_S) { //压力设置 返回 1个字节

                        byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x36, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x22, (byte) 0x2A, (byte) 0xC1, 0x7d, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(bytes);
                        //写入成功
                        System.out.println("压力上下限 设置成功:" + date.length);

                      /*  byte[] byte1 = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x36, (byte) 0x99, 0x00, 0x01,
                                (byte) 0x11, (byte) 0x6A, (byte) 0xD4, 0x2A, 0x7d};
                        channelHandlerContext.channel().writeAndFlush(byte1);
                        //写入失败
                        System.out.println("压力上下限 设置失败:" + date.length);*/


                    }
                } else if (cmd == ProtocolParams.CMD_system_monitoring) { //系统监控
                    if (rw == ProtocolParams.CMD_Ex_R_S) {
                        if (!run) {
                            byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x45, (byte) 0xaa, 0x00, 0x4c,
                                    0x00,
                                    0x00,
                                    0x00,
                                    0x3e, 0x4c, (byte) 0xcc, (byte) 0xcc,
                                    0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                    0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xC0, (byte) 0x20, (byte) 0x00, (byte) 0x00,//温度
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,//误差
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                    0x00,
                                    (byte) 0x79, (byte) 0x46, 0x7d, 0x7d};
                            channelHandlerContext.channel().writeAndFlush(bytes);
                        } else {
                            if (type == 1) {
                                byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x45, (byte) 0xaa, 0x00, 0x4c,
                                        0x00,
                                        0x01,
                                        0x01,
                                        0x3e, 0x4c, (byte) 0xcc, (byte) 0xcc,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xC0, (byte) 0x20, (byte) 0x00, (byte) 0x00,//温度
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,//误差
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x00,
                                        (byte) 0xd4, (byte) 0x87, 0x7d, 0x7d};
                                channelHandlerContext.channel().writeAndFlush(bytes);

                            } else if (type == 2) {
                                byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x45, (byte) 0xaa, 0x00, 0x4c,
                                        0x00,
                                        0x02,
                                        0x00,
                                        0x3F, 0x26, 0x66, 0x66,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xC0, (byte) 0x20, (byte) 0x00, (byte) 0x00,//温度
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,//误差
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x00,
                                        (byte) 0x62, (byte) 0x54, 0x7d, 0x7d};
                                channelHandlerContext.channel().writeAndFlush(bytes);

                            } else if (type == 3) {
                                byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x45, (byte) 0xaa, 0x00, 0x4c,
                                        0x00,
                                        0x03,
                                        0x00,
                                        0x3F, 0x26, 0x66, 0x66,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xC0, (byte) 0x20, (byte) 0x00, (byte) 0x00,//温度
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,//误差
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x00,
                                        (byte) 0x9f, (byte) 0x68, 0x7d, 0x7d};
                                channelHandlerContext.channel().writeAndFlush(bytes);
                            } else if (type == 4) {
                                byte[] bytes = {0x7d, 0x7b, 0x01, (byte) 0xf3, 0x01, (byte) 0xf1, 0x45, (byte) 0xaa, 0x00, 0x4c,
                                        0x00,
                                        0x04,
                                        0x01,
                                        0x3F, 0x26, 0x66, 0x66,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xB9, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xC0, (byte) 0x20, (byte) 0x00, (byte) 0x00,//温度
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,//误差
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        (byte) 0xBE, 0x19, (byte) 0x99, (byte) 0x99,
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x40, (byte) 0xA0, 0x00, 0x00, //时间
                                        0x00,
                                        (byte) 0x3c, (byte) 0x23, 0x7d, 0x7d};
                                channelHandlerContext.channel().writeAndFlush(bytes);
                            }
                        }


                    }
                }
            }
            System.out.println("[Server]: " + ByteBufUtil.hexDump((ByteBuf) msg));
        }

        int type = 0;
        public boolean run = false;

        /**
         * 有客户端连接过来的回调
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("有客户端连接过来：" + ctx.toString());
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
            System.out.println("有客户端断开了连接：" + ctx.toString());
        }


    }
}
