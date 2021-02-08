package com.puyu.mobile.sdi.netty;

import com.puyu.mobile.sdi.LiveDataStateBean;
import com.puyu.mobile.sdi.bean.RecDeviceId;
import com.puyu.mobile.sdi.bean.RecDeviceMCUVersion;
import com.puyu.mobile.sdi.bean.RecDeviceType;
import com.puyu.mobile.sdi.bean.RecPressureLimit;
import com.puyu.mobile.sdi.bean.RecSystemMonitor;
import com.puyu.mobile.sdi.bean.SysStateEnum;
import com.puyu.mobile.sdi.bean.WifiLinkStateEnum;
import com.puyu.mobile.sdi.util.NumberUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
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
                //ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else if (e.state() == IdleState.READER_IDLE) {
                System.out.println("-------userEventTriggered READER_IDLE" + "  超时\n");

            } else if (e.state() == IdleState.ALL_IDLE) {
                // TODO: 配合发送协议数据
                System.out.println("-------userEventTriggered ALL_IDLE" + "  超时\n");
                //超时   开始发送指令
                LiveDataStateBean.getInstant().allIdleDeal();
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
            byte rw = cmdData.getByte(1);//是读还是写
            byte[] date = new byte[cmdData.readableBytes() - 4];
            cmdData.getBytes(4, date);
            if (cmd == ProtocolParams.CMD_DEVICE_ID) { //仪器ID
                if (rw == ProtocolParams.CMD_Ex_R_R) { //读取仪器ID 返回 12个字节
                    LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_DEVICE_ID);//读取仪器ID 获取
                    //获取数据
                    System.out.println("仪器ID:" + ByteBufUtil.hexDump(date));
                    LiveDataStateBean.getInstant().deviceIdLiveData.postValue(new RecDeviceId(new String(date).trim()));
                    LiveDataStateBean.getInstant().fragDisLoadDialog.postValue("获取成功");

                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //写入仪器ID 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_Ex_W_R_s) {
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_DEVICE_ID);//写入仪器ID 成功
                            LiveDataStateBean.getInstant().fragDisLoadDialog.postValue("设置成功");
                            //TODO 将新的仪器ID设置上去
                            LiveDataStateBean.getInstant().deviceIdLiveData.postValue(new RecDeviceId(LiveDataStateBean.getInstant().changeDeviceID.getValue().trim()));
                            //写入成功
                            System.out.println("仪器ID 写入成功:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_Ex_W_R_f) {
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_DEVICE_ID, true, "仪器ID写入失败");//写入仪器ID 失败
                            //写入失败
                            System.out.println("仪器ID 写入失败:" + date.length);
                        } else if (date[0] == ProtocolParams.CMD_Ex_W_R_e) {
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_DEVICE_ID, true, "仪器ID写入出错");//仪器ID写入 出错
                            //写入出错
                            System.out.println("仪器ID 写入出错:" + date.length);
                        }
                    }
                }
            } else if (cmd == ProtocolParams.CMD_DEVICE_Version) { //软件版本号读取
                if (rw == ProtocolParams.CMD_Ex_R_R) { //软件版本号读取 返回 32个字节
                    LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_DEVICE_Version);//软件版本号读取 获取
                    //获取数据
                    System.out.println("软件版本号读取:" + ByteBufUtil.hexDump(date));
                    LiveDataStateBean.getInstant().deviceVersion.postValue(new RecDeviceMCUVersion(new String(date).trim()));
                    LiveDataStateBean.getInstant().fragDisLoadDialog.postValue("获取成功");

                }

            } else if (cmd == ProtocolParams.CMD_DEVICE_Type) { //仪器类型

                if (rw == ProtocolParams.CMD_Ex_R_R) { //仪器类型 返回 1个字节 0x00:静态稀释仪
                    LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_DEVICE_Type);//仪器类型 获取
                    //获取数据
                    if (date.length == 1 && date[0] == 0x00) {//静态稀释仪
                        System.out.println("仪器类型 读取:" + ByteBufUtil.hexDump(date));
                        LiveDataStateBean.getInstant().deviceType.postValue(new RecDeviceType("静态稀释仪"));
                    }
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //写入仪器类型 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_Ex_W_R_s) {
                            //写入成功
                            System.out.println("写入仪器类型 写入成功:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_Ex_W_R_f) {
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_DEVICE_Type, true, "仪器类型写入失败");//仪器类型写入 失败
                            //写入失败
                            System.out.println("写入仪器类型 写入失败:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_Ex_W_R_e) {
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_DEVICE_Type, true, "仪器类型写入出错");//仪器类型写入 出错
                            //写入出错
                            System.out.println("写入仪器类型 写入出错:" + date.length);

                        }
                    }
                }
            } else if (cmd == ProtocolParams.CMD_ADD_Pressurize) { //加压方法设置
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //获取数据
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //加压方法设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_set_R_s) { //方法设置成功
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_ADD_Pressurize);//加压方法设置 写入成功
                            LiveDataStateBean.getInstant().mainActivityDisLoadDialog.postValue("设置成功");
                            //写入成功
                            System.out.println("加压方法设置 成功:" + date.length);
                            //数据库存储 打印标签操作
                            LiveDataStateBean.getInstant().saveDBLabel();
                        } else if (date[0] == ProtocolParams.CMD_set_R_f) { //方法设置失败
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_add_samp, false, "加压方法设置失败");//写入加压方法设置失败 失败
                            //写入失败
                            System.out.println("加压方法设置 失败:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_rinse) { //冲洗方法设置
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //获取数据
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //冲洗方法设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_set_R_s) { //方法设置成功
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_rinse);//冲洗方法设置 写入成功
                            LiveDataStateBean.getInstant().mainActivityDisLoadDialog.postValue("设置成功");
                            //写入成功
                            System.out.println("冲洗方法设置 成功:" + date.length);
                            //数据库存储 打印标签操作
                            LiveDataStateBean.getInstant().saveDBLabel();
                        } else if (date[0] == ProtocolParams.CMD_set_R_f) { //方法设置失败
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_add_samp, false, "冲洗方法设置失败");//写入冲洗方法设置失败 失败

                            //写入失败
                            System.out.println("冲洗方法设置 失败:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_add_samp) { //加样
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //获取数据
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //加样方法设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_set_R_s) { //方法设置成功
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_add_samp);//加压方法设置 写入成功
                            LiveDataStateBean.getInstant().mainActivityDisLoadDialog.postValue("设置成功");
                            //数据库存储 打印标签操作
                            LiveDataStateBean.getInstant().saveDBLabel();
                            //写入成功
                            System.out.println("加样方法 成功:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_set_R_f) { //方法设置失败
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_add_samp, false, "加样方法设置失败");//写入加样方法设置失败 失败
                            //写入失败
                            System.out.println("加样方法 失败:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_gas_config) { //配气方法设置
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //获取数据
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //配气方法设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_set_R_s) { //方法设置成功
                            //写入成功
                            System.out.println("配气方法设置 成功:" + date.length);
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_gas_config);//配气方法设置 写入成功
                            LiveDataStateBean.getInstant().mainActivityDisLoadDialog.postValue("设置成功");
                            //数据库存储 打印标签操作
                            LiveDataStateBean.getInstant().saveDBLabel();
                        } else if (date[0] == ProtocolParams.CMD_set_R_f) { //方法设置失败
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_gas_config, false, "配气方法设置失败");//写入配气方法设置失败 失败
                            //写入失败
                            System.out.println("配气方法设置 失败:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_gas_name_config) { //配气 气体名称设置
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //获取数据
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //配气 气体名称设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_Ex_W_R_s) {
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_gas_name_config);//气体名称设置 写入成功
                            //写入成功
                            System.out.println("配气 气体名称设置 写入成功:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_Ex_W_R_f) {
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_gas_name_config, false, "配气气体名称设置失败");//配气气体名称设置失败 失败
                            //写入失败
                            System.out.println("配气 气体名称设置 写入失败:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_Ex_W_R_e) {
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_gas_name_config, false, "配气气体名称设置出错");//配气气体名称设置出错 出错
                            //写入出错
                            System.out.println("配气 气体名称设置 写入出错:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_calibration) { //压力校准 设置
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //获取数据
                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //校准设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_set_R_s) { //方法设置成功
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_calibration);//压力校准 成功
                            LiveDataStateBean.getInstant().fragDisLoadDialog.postValue("压力校准成功");
                            //写入成功
                            System.out.println("校准 成功:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_set_R_f) { //方法设置失败
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_calibration, true, "压力校准失败");//压力校准 失败
                            //写入失败
                            System.out.println("校准 失败:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_pressure_up_low) { //压力上下限 设置
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    //读取压力上下限 返回 8 个字节 4字节(FP32)上限压力(0-50psia)4字节(FP32)下限压力(0-1psia)
                    //获取数据
                    LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_pressure_up_low);//压力上下限 获取
                    LiveDataStateBean.getInstant().fragDisLoadDialog.postValue("压力上下限获取成功");
                    if (date.length == 8) {
                        ByteBuf byteBuf = Unpooled.copiedBuffer(date);
                        float ul = byteBuf.readFloat();
                        System.out.println("压力上限:" + ul);
                        float ll = byteBuf.readFloat();
                        System.out.println("压力下限:" + ll);
                        byteBuf.release();
                        LiveDataStateBean.getInstant().pressureLimit.postValue(
                                new RecPressureLimit(NumberUtil.keepPrecision(ul, 2),
                                        NumberUtil.keepPrecision(ll, 2)));
                    }

                } else if (rw == ProtocolParams.CMD_Ex_W_R) { //压力设置 返回 1个字节
                    if (date.length == 1) {
                        if (date[0] == ProtocolParams.CMD_set_R_s) { //方法设置成功
                            LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_pressure_up_low);//压力上下限 设置成功
                            LiveDataStateBean.getInstant().fragDisLoadDialog.postValue("压力限值设置成功");
                            LiveDataStateBean.getInstant().pressureLimit.postValue(
                                    new RecPressureLimit(NumberUtil.parseFloat(LiveDataStateBean.getInstant().pressUp.getValue(), -1.0f)
                                            , NumberUtil.parseFloat(LiveDataStateBean.getInstant().pressLow.getValue(), -1.0f)));
                            //写入成功
                            System.out.println("压力上下限 设置成功:" + date.length);

                        } else if (date[0] == ProtocolParams.CMD_set_R_f) { //方法设置失败
                            LiveDataStateBean.getInstant().receiveFailData(ProtocolParams.CMD_pressure_up_low, true, "压力限值设置失败");//写入压力限值设置失败 失败
                            //写入失败
                            System.out.println("压力上下限 设置失败:" + date.length);

                        }
                    }

                }
            } else if (cmd == ProtocolParams.CMD_system_monitoring) { //系统监控
                if (rw == ProtocolParams.CMD_Ex_R_R) {
                    LiveDataStateBean.getInstant().idleSended = false;//已经收到
                    LiveDataStateBean.getInstant().receiveData(ProtocolParams.CMD_system_monitoring);//系统监控 获取
                    RecSystemMonitor monitor = new RecSystemMonitor();
                    //系统监控 返回 76+N个字节  //获取数据
                    if (date.length >= 76) {
                        //1字节(INT8U)系统状态 系统状态0x00:Normal  0x01:Alarm  0x02:Err
                        byte sysState = date[0];
                        if (sysState == 0x00) {
                            System.out.println("系统状态 Normal");
                            monitor.sysState = SysStateEnum.SysNormal;
                        } else if (sysState == 0x01) {
                            System.out.println("系统状态 Alarm");
                            monitor.sysState = SysStateEnum.SysAlarm;
                        } else if (sysState == 0x02) {
                            System.out.println("系统状态 Err");
                            monitor.sysState = SysStateEnum.SysErr;
                        }
                        //1字节(INT8U)当前运行流程 当前运行流程：
                        //0x00:空闲
                        //0x01:配气
                        //0x02:加压
                        //0x03:清洗
                        //0x04:加样
                        byte runProcess = date[1];
                        if (runProcess == 0x00) {
                            System.out.println("当前运行流程 空闲");
                        } else if (runProcess == 0x01) {
                            System.out.println("当前运行流程 配气");
                        } else if (runProcess == 0x02) {
                            System.out.println("当前运行流程 加压");
                        } else if (runProcess == 0x03) {
                            System.out.println("当前运行流程 清洗");
                        } else if (runProcess == 0x04) {
                            System.out.println("当前运行流程 加样");
                        }
                        monitor.runProcess = runProcess;
                        //1字节(INT8U)当前运行通道数:
                        //0x00:稀释气
                        //0x01:标气1
                        //0x02:标气2
                        //0x03:标气3
                        //0x04:标气4
                        //0x05:多级稀释气
                        //0x06:二级稀释气
                        byte runPassage = date[2];
                        if (runPassage == 0x00) {
                            System.out.println("当前运行通道数 稀释气");
                        } else if (runPassage == 0x01) {
                            System.out.println("当前运行通道数 标气1");
                        } else if (runPassage == 0x02) {
                            System.out.println("当前运行通道数 标气2");
                        } else if (runPassage == 0x03) {
                            System.out.println("当前运行通道数 标气3");
                        } else if (runPassage == 0x04) {
                            System.out.println("当前运行通道数 标气4");
                        } else if (runPassage == 0x05) {
                            System.out.println("当前运行通道数 多级稀释气");
                        } else if (runPassage == 0x06) {
                            System.out.println("当前运行通道数 二级稀释气");
                        }
                        monitor.runPassage = runPassage;

                        //4字节(FP32)当前压力(psi)
                        byte[] currentPress = new byte[]{date[3], date[4], date[5], date[6]};
                        float cp = ByteBuffer.wrap(currentPress).getFloat();
                        System.out.println("当前压力:" + cp);
                        monitor.currentPress = cp;

                        //4字节(FP32)目标压力(psi)
                        byte[] targetPress = new byte[]{date[7], date[8], date[9], date[10]};
                        float tp = ByteBuffer.wrap(targetPress).getFloat();
                        System.out.println("目标压力:" + tp);
                        monitor.targetPress =tp;

                        //4字节(FP32)平均压力(psi)
                        byte[] averagePress = new byte[]{date[11], date[12], date[13], date[14]};
                        float ap = ByteBuffer.wrap(averagePress).getFloat();
                        System.out.println("平均压力:" + ap);
                        monitor.averagePress = ap;

                        //4字节(FP32)环境温度(℃)
                        byte[] surroundTem = new byte[]{date[15], date[16], date[17], date[18]};
                        float st = ByteBuffer.wrap(surroundTem).getFloat();
                        System.out.println("环境温度:" + st);
                        monitor.surroundTem = st;

                        //4字节(FP32)稀释气配气误差
                        byte[] diluentDis = new byte[]{date[19], date[20], date[21], date[22]};
                        float ddis = ByteBuffer.wrap(diluentDis).getFloat();
                        System.out.println("稀释气配气误差:" + ddis);
                        monitor.diluentDis = ddis;

                        //4字节(FP32)标气通道1配气误差
                        byte[] standP1Dis = new byte[]{date[23], date[24], date[25], date[26]};
                        float sp1Dis = ByteBuffer.wrap(standP1Dis).getFloat();
                        System.out.println("标气通道1配气误差:" + sp1Dis);
                        monitor.standP1Dis = sp1Dis;

                        //4字节(FP32)标气通道2配气误差
                        byte[] standP2Dis = new byte[]{date[27], date[28], date[29], date[30]};
                        float sp2Dis = ByteBuffer.wrap(standP2Dis).getFloat();
                        System.out.println("标气通道2配气误差:" + sp2Dis);
                        monitor.standP2Dis = sp2Dis;

                        //4字节(FP32)标气通道3配气误差
                        byte[] standP3Dis = new byte[]{date[31], date[32], date[33], date[34]};
                        float sp3Dis = ByteBuffer.wrap(standP3Dis).getFloat();
                        System.out.println("标气通道3配气误差:" + sp3Dis);
                        monitor.standP3Dis = sp3Dis;

                        //4字节(FP32)标气通道4配气误差
                        byte[] standP4Dis = new byte[]{date[35], date[36], date[37], date[38]};
                        float sp4Dis = ByteBuffer.wrap(standP4Dis).getFloat();
                        System.out.println("标气通道4配气误差:" + sp4Dis);
                        monitor.standP4Dis = sp4Dis;

                        //4字节(FP32)多级稀释气配气误差
                        byte[] mulDiluentDis = new byte[]{date[39], date[40], date[41], date[42]};
                        float mddis = ByteBuffer.wrap(mulDiluentDis).getFloat();
                        System.out.println("多级稀释气配气误差:" + mddis);
                        monitor.mulDiluentDis = mddis;

                        //4字节(FP32)二级稀释气配气误差
                        byte[] diluent2Dis = new byte[]{date[43], date[44], date[45], date[46]};
                        float d2dis = ByteBuffer.wrap(diluent2Dis).getFloat();
                        System.out.println("二级稀释气配气误差:" + d2dis);
                        monitor.diluent2Dis = d2dis;

                        //4字节(FP32)稀释气配气运行时间(s)
                        byte[] diluentRunTime = new byte[]{date[47], date[48], date[49], date[50]};
                        float drt = ByteBuffer.wrap(diluentRunTime).getFloat();
                        System.out.println("稀释气配气运行时间:" + drt);
                        monitor.diluentRunTime = drt;

                        //4字节(FP32)标气通道1配气运行时间(s)
                        byte[] standP1RunTime = new byte[]{date[51], date[52], date[53], date[54]};
                        float sp1rt = ByteBuffer.wrap(standP1RunTime).getFloat();
                        System.out.println("标气通道1配气运行时间:" + sp1rt);
                        monitor.standP1RunTime = sp1rt;

                        //4字节(FP32)标气通道2配气运行时间(s)
                        byte[] standP2RunTime = new byte[]{date[55], date[56], date[57], date[58]};
                        float sp2rt = ByteBuffer.wrap(standP2RunTime).getFloat();
                        System.out.println("标气通道2配气运行时间:" + sp2rt);
                        monitor.standP2RunTime = sp2rt;

                        //4字节(FP32)标气通道3配气运行时间(s)
                        byte[] standP3RunTime = new byte[]{date[59], date[60], date[61], date[62]};
                        float sp3rt = ByteBuffer.wrap(standP3RunTime).getFloat();
                        System.out.println("标气通道3配气运行时间:" + sp3rt);
                        monitor.standP3RunTime = sp3rt;

                        //4字节(FP32)标气通道4配气运行时间(s)
                        byte[] standP4RunTime = new byte[]{date[63], date[64], date[65], date[66]};
                        float sp4rt = ByteBuffer.wrap(standP4RunTime).getFloat();
                        System.out.println("标气通道4配气运行时间:" + sp4rt);
                        monitor.standP4RunTime = sp4rt;

                        //4字节(FP32)多级稀释气配气运行时间(s)
                        byte[] mulDiluentRunTime = new byte[]{date[67], date[68], date[69], date[70]};
                        float mdrt = ByteBuffer.wrap(mulDiluentRunTime).getFloat();
                        System.out.println("多级稀释气配气运行时间:" + mdrt);
                        monitor.mulDiluentRunTime = mdrt;

                        //4字节(FP32)二级稀释气配气运行时间(s)
                        byte[] diluent2RunTime = new byte[]{date[71], date[72], date[73], date[74]};
                        float d2rt = ByteBuffer.wrap(diluent2RunTime).getFloat();
                        System.out.println("二级稀释气配气运行时间:" + d2rt);
                        monitor.diluent2RunTime = d2rt;

                        monitor.runAllTime = drt + sp1rt + sp2rt + sp3rt + sp4rt + mdrt + d2rt;


                        //1字节(INT8U)报警码个数N（0-10）
                        byte alarmNumber = date[75];
                        System.out.println("报警码个数N:" + alarmNumber);
                        monitor.alarmNumber = alarmNumber;

                        monitor.alarmCode = new byte[alarmNumber];
                        //第一个报警码:
                        for (int i = 0; i < alarmNumber; i++) {
                            System.out.println("第N个报警码N:" + i + " 是：" + ByteBufUtil.hexDump(date, 76 + i, 1));
                            monitor.alarmCode[i] = date[76 + i];
                        }
                        //....
                        //第N个报警码:
                        LiveDataStateBean.getInstant().systemMonitor.postValue(monitor);
                    }
                    System.out.println("系统监控:" + date.length);
                }
            }
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
        LiveDataStateBean.getInstant().wifiState.postValue(WifiLinkStateEnum.LinkDisConnect);

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