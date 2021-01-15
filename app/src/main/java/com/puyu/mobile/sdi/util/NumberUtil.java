package com.puyu.mobile.sdi.util;


import java.math.BigDecimal;
import java.util.regex.Pattern;


/**
 * author : 简玉锋
 * e-mail : yufeng_jian@fpi-inc.com
 * date   : 2019/7/9 13:41
 * desc   :
 * version: 1.0
 */


public class NumberUtil {

    public static float keepPrecision(float number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 检查是否是数字格式  数据返回
     *
     * @return 数字   ""不是数字
     */
    public static String checkInputIsNumber(String value) {
        String number = "";
        if (value.contains(".")) {
            try {
//                number = Float.valueOf(value) + "";
                BigDecimal b = new BigDecimal(value);
                number = b.toString();
            } catch (NumberFormatException e) {
            }
        } else {
            try {
                number = Long.valueOf(value) + "";
            } catch (NumberFormatException e) {
            }
        }
        return number;
    }

    /**
     * 判断当前值是否为整数
     *
     * @param value
     * @return
     */
    public static boolean isInteger(String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(value).matches();
    }

    /**
     * 判断当前值是否为数字(包括小数)
     *
     * @param value
     * @return
     */
    public static boolean isDigit(String value) {
        if (StringUtil.isEmpty(value)) {
            return false;
        }
        String mstr = value.toString();
        Pattern pattern1 = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern2 = Pattern.compile("-?[0-9]+.?[0-9]*");
        return pattern1.matcher(mstr).matches() || pattern2.matcher(mstr).matches();
    }

    /**
     * 将数字格式化输出
     *
     * @param value     需要格式化的值
     * @param precision 精度(小数点后的位数)
     * @return
     */
    public static String format(String value, Integer precision) {
        Double number = 0.0;
        if (NumberUtil.isDigit(value)) {
            number = Double.valueOf(value);
        }
        precision = (precision == null || precision < 0) ? 2 : precision;
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP)
                .toString();
    }

    /**
     * 将数字格式化输出，默认两位小叔
     *
     * @param value
     * @return
     */
    public static String format(String value) {
        return NumberUtil.format(value, 2);
    }

    /**
     * 将值转成Integer型，如果不是整数，则返回0
     *
     * @param value
     * @param replace 如果为0或者null，替换值
     * @return
     */
    public static Integer parseInteger(String value, Integer replace) {
        if (!NumberUtil.isInteger(value)) {
            return replace;
        }
        return Integer.valueOf(value);
    }

    /**
     * 将值转成Integer型，如果不是整数，则返回0
     *
     * @param value
     * @return
     */
    public static Integer parseInteger(String value) {
        return NumberUtil.parseInteger(value, 0);
    }

    /**
     * 将值转成Long型
     *
     * @param value
     * @param replace 如果为0或者null，替换值
     * @return
     */
    public static Long parseLong(String value, Long replace) {
        if (!NumberUtil.isInteger(value)) {
            return replace;
        }
        return Long.valueOf(value);
    }

    /**
     * 将值转成Long型，如果不是整数，则返回0
     *
     * @param value
     * @return
     */
    public static Long parseLong(String value) {
        return NumberUtil.parseLong(value, 0L);
    }

    /**
     * 将值转成Double型
     *
     * @param value
     * @param replace replace 如果为0或者null，替换值
     * @return
     */
    public static Double parseDouble(String value, Double replace) {
        if (NumberUtil.isDigit(value)) {
            return Double.valueOf(value);
        } else {
            return replace;
        }
    }

    /**
     * 将值转成Double型
     *
     * @param value
     * @param replace replace 如果为0或者null，替换值
     * @return
     */
    public static Float parseFloat(String value, Float replace) {
        if (NumberUtil.isDigit(value)) {
            return Float.valueOf(value);
        } else {
            return replace;
        }
    }

    /**
     * 将值转成Double型，如果不是整数，则返回0
     *
     * @param value
     * @return
     */
    public static Double parseDouble(String value) {
        return NumberUtil.parseDouble(value, 0.0);
    }

    /**
     * 将值转成Double型，如果不是整数，则返回0
     *
     * @param value
     * @return
     */
    public static Float parseFloat(String value) {
        return NumberUtil.parseFloat(value, 0.0f);
    }

    /**
     * 将char型数据转成字节数组
     *
     * @param value
     * @return
     */
    public static byte[] toBytes(char value) {
        byte[] bt = new byte[2];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将short型数据转成字节数组
     *
     * @param value
     * @return
     */
    public static byte[] toBytes(short value) {
        byte[] bt = new byte[2];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将int型数据转成字节数组
     *
     * @param value
     * @return
     */
    public static byte[] toBytes(int value) {
        byte[] bt = new byte[4];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将long型数据转成字节数组
     *
     * @param value
     * @return
     */
    public static byte[] toBytes(long value) {
        byte[] bt = new byte[8];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将short型数据插入到指定索引的字节数组中
     *
     * @param index  索引
     * @param values 字节数组
     * @param value  需要插入的值
     */
    public static void insert(int index, byte[] values, short value) {
        byte[] bt = NumberUtil.toBytes(value);
        System.arraycopy(bt, 0, values, index, 2);
    }

    /**
     * 将int型数据插入到指定索引的字节数组中
     *
     * @param index  索引
     * @param values 字节数组
     * @param value  需要插入的值
     */
    public static void insert(int index, byte[] values, int value) {
        byte[] bt = NumberUtil.toBytes(value);
        System.arraycopy(bt, 0, values, index, 4);
    }

    /**
     * 将long型数据插入到指定索引的字节数组中
     *
     * @param index  索引
     * @param values 字节数组
     * @param value  需要插入的值
     */
    public static void insert(int index, byte[] values, long value) {
        byte[] bt = NumberUtil.toBytes(value);
        System.arraycopy(bt, 0, values, index, 8);
    }

    /**
     * 将字节转换成整型
     *
     * @param value 字节类型值
     * @return
     */
    public static int byteToInt(byte value) {
        if (value < 0) {
            return value + 256;
        }
        return value;
    }


}

