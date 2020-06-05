package org.springultron.qrcode.option;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * logo 样式选项
 *
 * @author brucewuu
 * @date 2020/6/4 20:16
 */
public class LogoOptions {
    /**
     * logo 图片
     */
    private final BufferedImage logo;
    /**
     * logo 样式
     */
    private final LogoStyle logoStyle;
    /**
     * logo 占二维码的比例
     */
    private final int rate;
    /**
     * true 表示有边框，
     * false 表示无边框
     */
    private final boolean border;
    /**
     * 边框颜色
     */
    private final Color borderColor;
    /**
     * 外围边框颜色
     */
    private final Color outerBorderColor;
    /**
     * 用于设置logo的透明度
     */
    private final Float opacity;

    public static LogoOptions.Builder builder() {
        return new LogoOptions.Builder();
    }

    private LogoOptions(BufferedImage logo, LogoStyle logoStyle, int rate, boolean border, Color borderColor, Color outerBorderColor, Float opacity) {
        this.logo = logo;
        this.logoStyle = logoStyle;
        this.rate = rate;
        this.border = border;
        this.borderColor = borderColor;
        this.outerBorderColor = outerBorderColor;
        this.opacity = opacity;
    }

    public BufferedImage getLogo() {
        return logo;
    }

    public LogoStyle getLogoStyle() {
        return logoStyle;
    }

    public int getRate() {
        return rate;
    }

    public boolean isBorder() {
        return border;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getOuterBorderColor() {
        return outerBorderColor;
    }

    public Float getOpacity() {
        return opacity;
    }

    public static class Builder {
        private BufferedImage logo;
        private LogoStyle logoStyle;
        private int rate;
        private boolean border;
        private Color borderColor;
        private Color outerBorderColor;
        private Float opacity;

        public Builder logo(BufferedImage logo) {
            this.logo = logo;
            return this;
        }

        public Builder logoStyle(LogoStyle logoStyle) {
            this.logoStyle = logoStyle;
            return this;
        }

        public Builder rate(int rate) {
            this.rate = rate;
            return this;
        }

        public Builder border(boolean border) {
            this.border = border;
            return this;
        }

        public Builder borderColor(Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder outerBorderColor(Color outerBorderColor) {
            this.outerBorderColor = outerBorderColor;
            return this;
        }

        public Builder opacity(Float opacity) {
            this.opacity = opacity;
            return this;
        }

        public LogoOptions build() {
            return new LogoOptions(logo, logoStyle, rate, border, borderColor, outerBorderColor, opacity);
        }
    }

    /**
     * logo的样式
     */
    public enum LogoStyle {
        ROUND, NORMAL;

        public static LogoStyle getStyle(String name) {
            return "round".equalsIgnoreCase(name) ? ROUND : NORMAL;
        }
    }
}
