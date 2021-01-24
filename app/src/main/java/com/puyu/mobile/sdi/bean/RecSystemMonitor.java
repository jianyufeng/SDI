package com.puyu.mobile.sdi.bean;

/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2021/1/7 10:37
 * desc   :
 * version: 1.0
 */
public class RecSystemMonitor {
    public SysStateEnum sysState;//系统状态
    public byte runProcess;//当前运行流程
    public byte runPassage;//当前运行通道数
    public float currentPress = 0.25f;//当前压力
    public float targetPress;//目标压力
    public float averagePress;//平均压力
    public float surroundTem;//环境温度

    public float diluentDis;//稀释气配气误差
    public float standP1Dis;//标气通道1配气误差
    public float standP2Dis;//标气通道2配气误差
    public float standP3Dis;//标气通道3配气误差
    public float standP4Dis;//标气通道4配气误差
    public float mulDiluentDis;//多级稀释气配气误差
    public float diluent2Dis;//二级稀释气配气误差

    public float diluentRunTime;//稀释气配气运行时间
    public float standP1RunTime;//标气通道1配气运行时间
    public float standP2RunTime;//标气通道2配气运行时间
    public float standP3RunTime;//标气通道3配气运行时间
    public float standP4RunTime;//标气通道4配气运行时间
    public float mulDiluentRunTime;//多级稀释气配气运行时间
    public float diluent2RunTime;//二级稀释气配气运行时间


    public byte alarmNumber;//报警码个数N
    public byte[] alarmCode;//报警码

    public RecSystemMonitor() {
    }
}
