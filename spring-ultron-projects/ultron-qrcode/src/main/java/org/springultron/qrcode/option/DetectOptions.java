package org.springultron.qrcode.option;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 三个探测图形的样式选项
 *
 * @author brucewuu
 * @date 2020/6/4 20:28
 */
public class DetectOptions {
    /**
     * 外部颜色
     */
    private final Color outColor;
    /**
     * 内部颜色
     */
    private final Color inColor;
    /**
     * 默认探测图形，优先级高于颜色的定制（即存在图片时，用图片绘制探测图形）
     */
    private final BufferedImage detectImg;
    /**
     * 左上角的探测图形
     */
    private final BufferedImage detectImgLT;
    /**
     * 右上角的探测图形
     */
    private final BufferedImage detectImgRT;
    /**
     * 左下角的探测图形
     */
    private final BufferedImage detectImgLD;

    public static DetectOptions.Builder builder() {
        return new DetectOptions.Builder();
    }

    private DetectOptions(Color outColor, Color inColor, BufferedImage detectImg, BufferedImage detectImgLT, BufferedImage detectImgRT, BufferedImage detectImgLD) {
        this.outColor = outColor;
        this.inColor = inColor;
        this.detectImg = detectImg;
        this.detectImgLT = detectImgLT;
        this.detectImgRT = detectImgRT;
        this.detectImgLD = detectImgLD;
    }

    public BufferedImage chooseDetectedImg(DetectOptions.DetectLocation detectLocation) {
        switch (detectLocation) {
            case LD:
                return detectImgLD == null ? detectImg : detectImgLD;
            case LT:
                return detectImgLT == null ? detectImg : detectImgLT;
            case RT:
                return detectImgRT == null ? detectImg : detectImgRT;
            default:
                return null;
        }
    }

    public Color getOutColor() {
        return outColor;
    }

    public Color getInColor() {
        return inColor;
    }

    public BufferedImage getDetectImg() {
        return detectImg;
    }

    public BufferedImage getDetectImgLT() {
        return detectImgLT;
    }

    public BufferedImage getDetectImgRT() {
        return detectImgRT;
    }

    public BufferedImage getDetectImgLD() {
        return detectImgLD;
    }

    public static class Builder {
        private Color outColor;
        private Color inColor;
        private BufferedImage detectImg;
        private BufferedImage detectImgLT;
        private BufferedImage detectImgRT;
        private BufferedImage detectImgLD;

        public Builder outColor(Color outColor) {
            this.outColor = outColor;
            return this;
        }

        public Builder inColor(Color inColor) {
            this.inColor = inColor;
            return this;
        }

        public Builder detectImg(BufferedImage detectImg) {
            this.detectImg = detectImg;
            return this;
        }

        public Builder detectImgLT(BufferedImage detectImgLT) {
            this.detectImgLT = detectImgLT;
            return this;
        }

        public Builder detectImgRT(BufferedImage detectImgRT) {
            this.detectImgRT = detectImgRT;
            return this;
        }

        public Builder detectImgLD(BufferedImage detectImgLD) {
            this.detectImgLD = detectImgLD;
            return this;
        }

        public DetectOptions build() {
            return new DetectOptions(outColor, inColor, detectImg, detectImgLT, detectImgRT, detectImgLD);
        }
    }

    public enum DetectLocation {
        /**
         * 左上角
         */
        LT,
        /**
         * 左下角
         */
        LD,
        /**
         * 右上角
         */
        RT,
        /**
         * 无
         */
        NONE {
            @Override
            public boolean detectedArea() {
                return false;
            }
        };

        public boolean detectedArea() {
            return true;
        }
    }
}
