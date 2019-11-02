package org.springultron.core.utils;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数字类型工具
 *
 * @author brucewuu
 * @date 2019-06-06 15:59
 */
public class NumberUtils {

    /**
     * 字符串转int
     *
     * <pre>
     *   NumberUtils.toInt(null) = -1
     *   NumberUtils.toInt("")   = -1
     *   NumberUtils.toInt("1")  = 1
     * </pre>
     *
     * @param str 字符串
     * @return 结果 默认值：0
     */
    public static int toInt(final String str) {
        return toInt(str, 0);
    }

    /**
     * 字符串转int
     *
     * <pre>
     *   NumberUtils.toInt(null, 0) = 0
     *   NumberUtils.toInt("", 0)   = 0
     *   NumberUtils.toInt("1", -1)  = 1
     * </pre>
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 结果
     */
    public static int toInt(final String str, final int defaultValue) {
        if (null == str || "".equals(str)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 字符串转long
     *
     * @param str 字符串
     * @return 结果 默认值 0L
     */
    public static long toLong(final String str) {
        return toLong(str, 0L);
    }

    /**
     * 字符串转long
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 结果
     */
    public static long toLong(final String str, final long defaultValue) {
        if (null == str || "".equals(str)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 字符串转float
     *
     * @param str 字符串
     * @return 结果 默认值 0.0f
     */
    public static float toFloat(final String str) {
        return toFloat(str, 0.0f);
    }

    /**
     * 字符串转long
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 结果
     */
    public static float toFloat(final String str, final float defaultValue) {
        if (null == str || "".equals(str)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 字符串转double
     *
     * @param str 字符串
     * @return 结果 默认值 0.0d
     */
    public static double toDouble(final String str) {
        return toDouble(str, 0.0d);
    }

    /**
     * 字符串转double
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 结果
     */
    public static double toDouble(final String str, final double defaultValue) {
        if (null == str || "".equals(str)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 数字转{@link BigDecimal}
     *
     * @param number 数字
     * @return {@link BigDecimal}
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(number.toString());
    }

    /**
     * 数字字符串转{@link BigDecimal}
     *
     * @param numberStr 数字字符串
     * @return {@link BigDecimal}
     */
    public static BigDecimal toBigDecimal(String numberStr) {
        return (null == numberStr) ? BigDecimal.ZERO : new BigDecimal(numberStr);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     */
    public static BigDecimal round(double v, int scale, RoundingMode roundingMode) {
        return round(Double.toString(v), scale, roundingMode);
    }

    /**
     * 保留固定位数小数
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
        Assert.isTrue(null != numberStr && !"".equals(numberStr), "numberStr must not be null, empty, or blank");
        if (scale < 0) {
            scale = 0;
        }
        return round(toBigDecimal(numberStr), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = 0;
        }
        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }

        return number.setScale(scale, roundingMode);
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     */
    public static BigDecimal roundHalfEven(Number number, int scale) {
        return roundHalfEven(toBigDecimal(number), scale);
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     */
    public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_EVEN);
    }
}
