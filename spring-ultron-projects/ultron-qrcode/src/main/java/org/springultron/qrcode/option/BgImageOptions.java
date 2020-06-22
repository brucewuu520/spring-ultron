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
    private final Integer width;
    /**
     * 背景图高
     */
    private final Integer height;
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

    public static BgImageOptions.Builder builder() {
        return new BgImageOptions.Builder();
    }

    private BgImageOptions(Integer width, Integer height, BufferedImage bgImage, GifDecoder gifDecoder, BgImageStyle bgImageStyle, float opacity, int startX, int startY) {
        this.width = width;
        this.height = height;
        this.bgImage = bgImage;
        this.gifDecoder = gifDecoder;
        this.bgImageStyle = bgImageStyle;
        this.opacity = opacity;
        this.startX = startX;
        this.startY = startY;
    }

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

    public void setBgImage(BufferedImage bgImage) {
        this.bgImage = bgImage;
    }

    public GifDecoder getGifDecoder() {
        return gifDecoder;
    }

    public void setGifDecoder(GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
    }

    public BgImageStyle getBgImageStyle() {
        return bgImageStyle;
    }

    public void setBgImageStyle(BgImageStyle bgImageStyle) {
        this.bgImageStyle = bgImageStyle;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public static class Builder {
        private Integer width;
        private Integer height;
        private BufferedImage bgImage;
        private GifDecoder gifDecoder;
        private BgImageStyle bgImageStyle;
        private float opacity;
        private int startX;
        private int startY;

        public Builder width(Integer width) {
            this.width = width;
            return this;
        }

        public Builder height(Integer height) {
            this.height = height;
            return this;
        }

        public Builder bgImage(BufferedImage bgImage) {
            this.bgImage = bgImage;
            return this;
        }

        public Builder gifDecoder(GifDecoder gifDecoder) {
            this.gifDecoder = gifDecoder;
            return this;
        }

        public Builder bgImageStyle(BgImageStyle bgImageStyle) {
            this.bgImageStyle = bgImageStyle;
            return this;
        }

        public Builder opacity(float opacity) {
            this.opacity = opacity;
            return this;
        }

        public Builder startX(int startX) {
            this.startX = startX;
            return this;
        }

        public Builder startY(int startY) {
            this.startY = startY;
            return this;
        }

        public BgImageOptions build() {
            return new BgImageOptions(width, height, bgImage, gifDecoder, bgImageStyle, opacity, startX, startY);
        }
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
