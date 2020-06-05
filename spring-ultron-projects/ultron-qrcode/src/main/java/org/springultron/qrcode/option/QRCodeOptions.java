package org.springultron.qrcode.option;

import com.google.zxing.EncodeHintType;

import java.util.Map;

/**
 * 二维码生成的配置选项
 *
 * @author brucewuu
 * @date 2020/6/4 19:28
 */
public class QRCodeOptions {
    /**
     * 塞入二维码的内容
     */
    private String content;
    /**
     * 生成二维码的宽
     */
    private Integer width;
    /**
     * 生成二维码的高
     */
    private Integer height;
    /**
     * 二维码信息(即传统二维码中的黑色方块) 绘制选项
     */
    private DrawOptions drawOptions;
    /**
     * 背景图样式选项
     */
    private BgImageOptions bgImageOptions;
    /**
     * logo 样式选项
     */
    private LogoOptions logoOptions;
    /**
     * 三个探测图形的样式选项
     * todo 后续可以考虑三个都可以自配置
     */
    private DetectOptions detectOptions;
    /**
     *  编码类型
     */
    private Map<EncodeHintType, Object> hints;
    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picFormat;

    /**
     * true 表示生成的是动图
     */
    public boolean isGif() {
        return bgImageOptions != null && bgImageOptions.getGifDecoder() != null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public DrawOptions getDrawOptions() {
        return drawOptions;
    }

    public void setDrawOptions(DrawOptions drawOptions) {
        this.drawOptions = drawOptions;
    }

    public BgImageOptions getBgImageOptions() {
        return bgImageOptions;
    }

    public void setBgImageOptions(BgImageOptions bgImageOptions) {
        this.bgImageOptions = bgImageOptions;
    }

    public LogoOptions getLogoOptions() {
        return logoOptions;
    }

    public void setLogoOptions(LogoOptions logoOptions) {
        this.logoOptions = logoOptions;
    }

    public DetectOptions getDetectOptions() {
        return detectOptions;
    }

    public void setDetectOptions(DetectOptions detectOptions) {
        this.detectOptions = detectOptions;
    }

    public Map<EncodeHintType, Object> getHints() {
        return hints;
    }

    public void setHints(Map<EncodeHintType, Object> hints) {
        this.hints = hints;
    }

    public String getPicFormat() {
        return picFormat;
    }

    public void setPicFormat(String picFormat) {
        this.picFormat = picFormat;
    }
}
