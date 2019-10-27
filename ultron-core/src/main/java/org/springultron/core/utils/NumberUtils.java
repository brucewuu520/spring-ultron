package org.springultron.core.utils;

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
            return Integer.valueOf(str);
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
            return Long.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 字符串转float
     *
     * @param str          字符串
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
            return Float.valueOf(str);
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
            return Double.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

}
