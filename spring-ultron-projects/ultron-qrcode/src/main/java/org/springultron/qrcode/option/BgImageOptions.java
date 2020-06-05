package org.springultron.qrcode.option;

import org.springultron.qrcode.gif.GifDecoder;

import java.awt.image.BufferedImage;

/**
 * 背景图绘制样式选项
 *
 * @author brucewuu
 * @date 2020/6/4 20:06
 */
public class BgImageOptions {
    /**
     * 背景图宽
     */
    private Integer width;
    /**
     * 背景图高
     */
    private Integer height;
    /**
     * 背景图
     */
    private BufferedImage bgImage;
    /**
     * 动态背景图
     */
    private GifDecoder gifDecoder;
    /**
     * 背景图样式
     */
    private BgImageStyle bgImageStyle;
    /**
     * if {@link #bgImageStyle} == BgImageStyle.OVERRIDE
     * 用于设置二维码的透明度
     */
    private float opacity;
    /**
     * if {@link #bgImageStyle} == BgImageStyle.FILL
     * 用于设置二维码的绘制在背景图上的x坐标
     */
    private int startX;
    /**
     * if {@link #bgImageStyle} ==  BgImageStyle.FILL
     * 用于设置二维码的绘制在背景图上的y坐标
     */
    private int startY;

    public int getWidth() {
        if (bgImageStyle == BgImageStyle.FILL && width == 0) {
            if (bgImage != null) {
                return bgImage.getWidth();
            } else {
                return gifDecoder.getFrame(0).getWidth();
            }
        }
        return width;
    }

    public int getHeight() {
        if (bgImageStyle == BgImageStyle.FILL && height == 0) {
            if (bgImage != null) {
                return bgImage.getHeight();
            } else {
                return gifDecoder.getFrame(0).getHeight();
            }
        }
        return height;
    }

    public BufferedImage getBgImage() {
        return bgImage;
    }

    public GifDecoder getGifDecoder() {
        return gifDecoder;
    }

    public BgImageStyle getBgImageStyle() {
        return bgImageStyle;
    }

    public float getOpacity() {
        return opacity;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    /**
     * 背景图样式
     */
    public enum BgImageStyle {
        /**
         * 设置二维码透明度，然后全覆盖背景图
         */
        OVERRIDE,
        /**
         * 将二维码填充在背景图的指定位置
         */
        FILL,
        /**
         * 背景图穿透显示, 即二维码主题色为透明，由背景图的颜色进行填充
         */
        PENETRATE;

        public static BgImageStyle getStyle(String name) {
            return "fill".equalsIgnoreCase(name) ? FILL : OVERRIDE;
        }
    }
}
