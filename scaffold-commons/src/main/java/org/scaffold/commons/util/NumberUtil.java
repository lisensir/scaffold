package org.scaffold.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author lisen 2016/11/20
 */
@SuppressWarnings("unused")
public class NumberUtil {

    private static  int SCALE = 2;

    /**
     * 设置系统默认的精度
     * @param defauleScale 默认精度
     */
    public static void setSCALE(int defauleScale) {
        SCALE =  defauleScale;
    }

    public static final RoundingMode ROUND_MODEL = RoundingMode.HALF_UP;

    /**
     * 指定数据精度，四舍五入
     *
     * @param num   操作数
     * @param scale 精度
     * @return Double
     */
    public static Double round(Number num, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("指定的数据精度要大于0");
        }

        BigDecimal b = new BigDecimal(num.doubleValue());
        b.setScale(scale, ROUND_MODEL);
        return b.doubleValue();
    }


    /**
     * 按照系统设置的默认精度四舍五入
     *
     * @param num 操作数
     */
    public static Double round(Number num) {
        BigDecimal b = new BigDecimal(num.doubleValue());
        b.setScale(SCALE, ROUND_MODEL);
        return b.doubleValue();
    }

    /**
     * 按指定精度格式化数值，如果位数不够补0，返回数值字符串
     *
     * @param num   数值
     * @param scale 精度
     * @return String
     */
    public static String format(Number num, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("指定的数据精度要大于0");
        }
        String var1 = "0";
        for(int i = 0; i < scale; i++) {
            if(i == 0) {
                var1 += ".0";
            } else {
                var1 += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(var1);
        return df.format(num);
    }

    /**
     * 按照系统设置的默认精度格式化数值
     * @param num 操作数
     * @return
     */
    public static String format(Number num) {
        String var1 = "0";
        for(int i = 0; i < SCALE; i++) {
            if(i == 0) {
                var1 += ".0";
            } else {
                var1 += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(var1);
        return df.format(num);
    }

    /**
     * 两个数值型数据相加
     *
     * @param addend1 加数1
     * @param addend2 加数2
     * @param scale   精度
     * @return Double
     */
    public static Double add(Number addend1, Number addend2, int scale) {
        Objects.requireNonNull(addend1, "加数不能为空");
        Objects.requireNonNull(addend2, "加数不能为空");
        BigDecimal first = BigDecimal.valueOf(addend1.doubleValue());
        BigDecimal second = BigDecimal.valueOf(addend2.doubleValue());

        return first.add(second).setScale(scale, ROUND_MODEL).doubleValue();
    }

    /**
     * 取绝对值
     * @param num 操作数
     * @return double
     */
    public static Double abs(Number num) {
        BigDecimal n = BigDecimal.valueOf(num.doubleValue());
        return n.abs().doubleValue();
    }

    /**
     * 两个数值型数据相加
     *
     * @param addend1 加数1
     * @param addend2 加数2
     * @return Double
     */
    public static Double add(Number addend1, Number addend2) {
        return add(addend1, addend2, SCALE);
    }


    /**
     * 两个数值相减
     *
     * @param minuend    被减数
     * @param subtractor 减数
     * @param scale      精度
     * @return Double
     */
    public static Double subtract(Number minuend, Number subtractor, int scale) {
        Objects.requireNonNull(minuend, "被减数不能为空");
        Objects.requireNonNull(subtractor, "减数不能为空");

        BigDecimal num1 = BigDecimal.valueOf(minuend.doubleValue());
        BigDecimal num2 = BigDecimal.valueOf(subtractor.doubleValue());

        return num1.subtract(num2).setScale(scale, ROUND_MODEL).doubleValue();
    }


    /**
     * 两个数值相减
     *
     * @param minuend    被减数
     * @param subtractor 减数
     * @return Double
     */
    public static Double subtract(Number minuend, Number subtractor) {
        return subtract(minuend, subtractor, SCALE);
    }


    /**
     * 取负数
     *
     * @param num 操作数
     * @return Double
     */
    public static Double minus(Number num) {
        if (num == null) return null;
        BigDecimal n = BigDecimal.valueOf(num.doubleValue());
        return n.multiply(new BigDecimal("-1")).doubleValue();
    }


    /**
     * 两数相乘
     *
     * @param num1  乘数
     * @param num2  乘数
     * @param scale 精度
     * @return Double
     */
    public static Double multiply(Number num1, Number num2, int scale) {
        Objects.requireNonNull(num1, "乘数不能为空");
        Objects.requireNonNull(num2, "乘数不能为空");

        BigDecimal n = BigDecimal.valueOf(num1.doubleValue());
        BigDecimal n2 = BigDecimal.valueOf(num2.doubleValue());

        return n.multiply(n2).setScale(scale, ROUND_MODEL).doubleValue();
    }


    /**
     * 两数相乘
     *
     * @param num1 乘数
     * @param num2 乘数
     * @return Double
     */
    public static Double multiply(Number num1, Number num2) {
        return multiply(num1, num2, SCALE);
    }


    /**
     * 两数相除
     *
     * @param num1  被除数
     * @param num2  除数
     * @param scale 精度
     * @return Double
     */
    public static Double divide(Number num1, Number num2, int scale) {
        Objects.requireNonNull(num1, "被除数不能为空");
        Objects.requireNonNull(num2, "除数不能为空");

        BigDecimal n1 = BigDecimal.valueOf(num1.doubleValue());
        BigDecimal n2 = BigDecimal.valueOf(num2.doubleValue());

        return n1.divide(n2, scale, ROUND_MODEL).doubleValue();
    }


    /**
     * 两数相除
     *
     * @param num1 被除数
     * @param num2 除数
     * @return Double
     */
    public static Double divide(Number num1, Number num2) {
        return divide(num1, num2, SCALE);
    }


    public static int getScale(Number number) {
        Objects.requireNonNull(number, "参数不能为空");
        int s = new BigDecimal(number.toString()).scale();
        return number instanceof Double && s > 5 ? s - 1 : s;
    }

    /**
     * 对字符串表示的数值格式化的指定精度，小数位多余的直接截取。
     * @param strNum 字符串表示的数据
     * @param precision 指定的数据精度
     * @return double
     */
    public static double formatToPrecision(String strNum, int precision) {
        String tmp = strNum;
        if(tmp.indexOf(".") > 0) {
            int bindx = tmp.indexOf(".");
            int eindx = tmp.length() > (bindx + precision+1) ? (bindx + precision+1) : tmp.length();
            tmp = tmp.substring(0, bindx) + tmp.substring(bindx, eindx);
        }
        return Double.parseDouble(tmp);
    }


    public static void main(String[] args) {
        Double d = 23.434D;
        System.out.println("---- minus = " + NumberUtil.minus(d));

        //double dd = 0.00001;
        double dd = 324.001;
        System.out.println("---- scale = " + NumberUtil.getScale(dd));

        BigDecimal db = BigDecimal.TEN.pow(NumberUtil.getScale(dd) > 4 ? NumberUtil.getScale(dd) - 1 : NumberUtil.getScale(dd));
        System.out.println("----- intValueExact = " + db);

        System.out.println("----- " + (dd * db.doubleValue()));

        Double mm = 123.5863D;
        System.out.println("---- mm " + NumberUtil.format(mm, 3));

        Double sd1 = Double.parseDouble("1.01234567890123");
        System.out.println("sd1 = " + NumberUtil.getScale(sd1));

        Double sd2 = Double.parseDouble("1.0123456789012");
        System.out.println("sd2 = " + NumberUtil.getScale(sd2));

        Double sd3 = Double.parseDouble("1.012345678901");
        System.out.println("sd3 = " + NumberUtil.getScale(sd3));

        Double sd4 = Double.parseDouble("1.01234567890");
        System.out.println("sd4 = " + NumberUtil.getScale(sd4));

        Double sd5 = Double.parseDouble("1.012");
        System.out.println("sd5 = " + NumberUtil.getScale(sd5));

        Double sd6 = Double.parseDouble("1.0123");
        System.out.println("sss6 = " + sd6);
        System.out.println("sd6 = " + NumberUtil.getScale(sd6));

        Double sd7 = Double.parseDouble("0.000001");
        System.out.println("sd7 = " + NumberUtil.getScale(sd7));

        Double sd8 = Double.parseDouble("0.00001");
        System.out.println("sd8 = " + NumberUtil.getScale(sd8));

        Double sd9 = Double.parseDouble("0.0000001");
        System.out.println("sss9 = " + sd9);
        System.out.println("sssss9 = " + new BigDecimal(sd9).toString());
        System.out.println("sd9 = " + NumberUtil.getScale(sd9));

        BigDecimal b = new BigDecimal("0.0000001");
        System.out.println("sd10 = " + b.scale());
        System.out.println("sdd10 = " + NumberUtil.getScale(b));


        Double format1 = 123.589863D;
        System.out.println("---- format1 " + NumberUtil.format(format1, 6));

        Double format2 = Double.parseDouble("0.876534");
        System.out.println("---- format2 " + NumberUtil.format(format2, 6));

        Double format3 = Double.parseDouble("0.876534");
        System.out.println("---- format3 " + format3.toString());
    }

}
