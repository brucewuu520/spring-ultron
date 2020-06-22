package org.springultron.qrcode.util;

import java.awt.*;

/**
 * 颜色转换工具类
 *
 * @author brucewuu
 * @date 2020/6/10 11:49
 */
public class ColorUtils {
    private ColorUtils() {
    }

    /**
     * 全透明颜色
     */
    public static Color OPACITY = ColorUtils.int2color(0x00FFFFFF);

    /**
     * int格式的颜色转为 awt 的Color对象
     *
     * @param color 0xffffffff  前两位为透明读， 三四位 R， 五六位 G， 七八位 B
     * @return Color
     */
    public static Color int2color(int color) {
        int a = (0xff000000 & color) >>> 24;
        int r = (0x00ff0000 & color) >> 16;
        int g = (0x0000ff00 & color) >> 8;
        int b = (0x000000ff & color);
        return new Color(r, g, b, a);
    }


    /**
     * 将Color对象转为html对应的颜色配置信息
     *
     * 如  Color.RED  ->  #f00
     */
    public static String int2htmlColor(int color) {
        int a = (0xff000000 & color) >>> 24;
        int r = (0x00ff0000 & color) >> 16;
        int g = (0x0000ff00 & color) >> 8;
        int b = (0x000000ff & color);
        return "#" + toHex(r) + toHex(g) + toHex(b) + toHex(a);
    }

    public static String toHex(int hex) {
        String str = Integer.toHexString(hex);
        if (str.length() == 1) {
            return "0" + str;
        }

        return str;
    }

}
