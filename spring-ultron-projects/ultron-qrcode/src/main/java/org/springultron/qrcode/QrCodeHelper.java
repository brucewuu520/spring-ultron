package org.springultron.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springultron.qrcode.constants.MediaType;
import org.springultron.qrcode.encode.QrCodeEncoder;
import org.springultron.qrcode.gif.GifDecoder;
import org.springultron.qrcode.gif.GifHelper;
import org.springultron.qrcode.option.*;
import org.springultron.qrcode.tuple.ImmutablePair;
import org.springultron.qrcode.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 二维码操作
 *
 * @author brucewuu
 * @date 2020/6/10 11:01
 */
public final class QrCodeHelper {

    public static QrCodeHelper.Builder encode(String content) {
        return new QrCodeHelper.Builder(content);
    }

    private static String asBase64(QrCodeOptions qrCodeOptions) throws WriterException, IOException {
        if (qrCodeOptions.isGif()) {
            // 动态二维码生成
            try (ByteArrayOutputStream outputStream = asGif(qrCodeOptions)) {
                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            }
        }

        // 普通二维码，直接输出图
        BufferedImage bufferedImage = asBufferedImage(qrCodeOptions);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, qrCodeOptions.getPicFormat(), outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }
    }

    private static BufferedImage asBufferedImage(QrCodeOptions qrCodeOptions) throws WriterException {
        BitMatrixEx bitMatrix = QrCodeEncoder.encode(qrCodeOptions);
        return QrCodeEncoder.toBufferedImage(qrCodeOptions, bitMatrix);
    }

    private static boolean asFile(QrCodeOptions qrCodeOptions, String absFileName) throws WriterException, IOException {
        File file = new File(absFileName);
        FileWriteUtil.mkDir(file.getParentFile());

        if (qrCodeOptions.isGif()) {
            // 保存动态二维码
            try (ByteArrayOutputStream output = asGif(qrCodeOptions)) {
                FileOutputStream out = new FileOutputStream(file);
                out.write(output.toByteArray());
                out.flush();
                out.close();
            }

            return true;
        }

        BufferedImage bufferedImage = asBufferedImage(qrCodeOptions);
        if (!ImageIO.write(bufferedImage, qrCodeOptions.getPicFormat(), file)) {
            throw new IOException("save QrCode image to: " + absFileName + " error!");
        }

        return true;
    }

    private static ByteArrayOutputStream asGif(QrCodeOptions qrCodeOptions) throws WriterException {
        BitMatrixEx bitMatrix = QrCodeEncoder.encode(qrCodeOptions);
        List<ImmutablePair<BufferedImage, Integer>> list = QrCodeEncoder.toGifImages(qrCodeOptions, bitMatrix);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GifHelper.saveGif(list, outputStream);
        return outputStream;
    }

    public static class Builder {
        private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

        /**
         * The message to put into QrCode
         */
        private String content;

        /**
         * qrcode image width
         */
        private Integer width;

        /**
         * qrcode image height
         */
        private Integer height;

        /**
         * qrcode message's code, default UTF-8
         */
        private String code = "utf-8";

        /**
         * 0 - 4
         */
        private Integer padding;

        /**
         * error level, default H
         */
        private ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.H;

        /**
         * output qrcode image type, default png
         */
        private String picFormat = "png";

        private BgImageOptions.Builder bgImgOptions;
        private LogoOptions.Builder logoOptions;
        private DrawOptions.Builder drawOptions;
        private DetectOptions.Builder detectOptions;

        public Builder(String content) {
            this.content = content;
            // 背景图默认采用覆盖方式
            this.bgImgOptions = BgImageOptions.builder().bgImageStyle(BgImageOptions.BgImageStyle.OVERRIDE).opacity(0.85f);

            // 默认采用普通格式的logo， 无边框
            this.logoOptions = LogoOptions.builder().logoStyle(LogoOptions.LogoStyle.NORMAL).border(false).rate(12);

            // 绘制信息，默认黑白方块
            this.drawOptions = DrawOptions.builder().drawStyle(DrawOptions.DrawStyle.RECTANGLE).bgColor(Color.WHITE).preColor(Color.BLACK).transparencyFill(false).enableScale(false);

            // 探测图形
            this.detectOptions = DetectOptions.builder();
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder width(Integer width) {
            if (width != null && width <= 0) {
                throw new IllegalArgumentException("生成二维码的宽必须大于0");
            }
            this.width = width;
            return this;
        }

        public Integer getWidth() {
            return width == null ? (height == null ? 200 : height) : width;
        }

        public Builder height(Integer height) {
            if (height != null && height <= 0) {
                throw new IllegalArgumentException("生成功能二维码的搞必须大于0");
            }
            this.height = height;
            return this;
        }

        public Integer getHeight() {
            return height == null ? (width == null ? 200 : width) : height;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder padding(Integer padding) {
            this.padding = padding;
            return this;
        }

        public Integer getPadding() {
            if (padding == null) {
                return 1;
            }

            if (padding < 0) {
                return 0;
            }

            if (padding > 4) {
                return 4;
            }

            return padding;
        }

        public Builder errorCorrection(ErrorCorrectionLevel errorCorrection) {
            this.errorCorrection = errorCorrection;
            return this;
        }

        public Builder picFormat(String picFormat) {
            this.picFormat = picFormat;
            return this;
        }

        public Builder bgImgOptions(BgImageOptions.Builder bgImgOptions) {
            this.bgImgOptions = bgImgOptions;
            return this;
        }

        public Builder logoOptions(LogoOptions.Builder logoOptions) {
            this.logoOptions = logoOptions;
            return this;
        }

        public Builder drawOptions(DrawOptions.Builder drawOptions) {
            this.drawOptions = drawOptions;
            return this;
        }

        public Builder detectOptions(DetectOptions.Builder detectOptions) {
            this.detectOptions = detectOptions;
            return this;
        }

        /////////////// logo 相关配置 ///////////////

        public Builder logo(String logo) throws IOException {
            try {
                return logo(ImageUtils.getImageByPath(logo));
            } catch (IOException e) {
                throw new IOException("load logo error!", e);
            }
        }

        public Builder logo(InputStream is) throws IOException {
            try {
                return logo(ImageIO.read(is));
            } catch (IOException e) {
//                log.error("load backgroundImg error! e:{}", e);
                throw new IOException("load backgroundImg error!", e);
            }
        }

        public Builder logo(BufferedImage img) {
            logoOptions.logo(img);
            return this;
        }

        public Builder logoStyle(LogoOptions.LogoStyle logoStyle) {
            logoOptions.logoStyle(logoStyle);
            return this;
        }

        /**
         * logo 背景颜色
         *
         * @param color 颜色值
         */
        public Builder logoBgColor(Integer color) {
            if (color == null) {
                return this;
            }

            return logoBgColor(ColorUtils.int2color(color));
        }

        /**
         * logo 背景颜色
         *
         * @param color Color
         */
        public Builder logoBgColor(Color color) {
            logoOptions.border(true);
            logoOptions.borderColor(color);
            return this;
        }

        public Builder logoBorderBgColor(Integer color) {
            if (color == null) {
                return this;
            }
            return logoBorderBgColor(ColorUtils.int2color(color));
        }

        /**
         * logo 外层边框颜色
         */
        public Builder logoBorderBgColor(Color color) {
            logoOptions.border(true);
            logoOptions.outerBorderColor(color);
            return this;
        }

        public Builder logoBorder(boolean border) {
            logoOptions.border(border);
            return this;
        }

        public Builder logoRate(int rate) {
            logoOptions.rate(rate);
            return this;
        }

        /**
         * logo透明度
         *
         * @param opacity 透明度
         */
        public Builder logoOpacity(float opacity) {
            logoOptions.opacity(opacity);
            return this;
        }

        ///////////////// logo配置结束 ///////////////


        // ------------------------------------------


        /////////////// 背景 相关配置 ///////////////

        public Builder bgImage(String imgPath) throws IOException {
            try {
                return bgImage(FileReadUtils.getStreamByFileName(imgPath));
            } catch (IOException e) {
//                log.error("load backgroundImg error! e:{}", e);
                throw new IOException("load backgroundImg error!", e);
            }
        }

        public Builder bgImage(InputStream inputStream) throws IOException {
            try {
                ByteArrayInputStream target = IoUtil.toByteArrayInputStream(inputStream);
                MediaType media = MediaType.typeOfMagicNum(FileReadUtils.getMagicNum(target));
                if (media == MediaType.ImageGif) {
                    GifDecoder gifDecoder = new GifDecoder();
                    gifDecoder.read(target);
                    bgImgOptions.gifDecoder(gifDecoder);
                    return this;
                } else {
                    return bgImage(ImageIO.read(target));
                }
            } catch (IOException e) {
//                log.error("load backgroundImg error! e:{}", e);
                throw new IOException("load backgroundImg error!", e);
            }
        }

        public Builder bgImage(BufferedImage bufferedImage) {
            bgImgOptions.bgImage(bufferedImage);
            return this;
        }

        public Builder bgImageStyle(BgImageOptions.BgImageStyle bgImageStyle) {
            bgImgOptions.bgImageStyle(bgImageStyle);
            return this;
        }

        public Builder bgWidth(int width) {
            bgImgOptions.width(width);
            return this;
        }

        public Builder bgHeight(int height) {
            bgImgOptions.height(height);
            return this;
        }

        public Builder bgOpacity(float opacity) {
            bgImgOptions.opacity(opacity);
            return this;
        }

        public Builder bgStartX(int startX) {
            bgImgOptions.startX(startX);
            return this;
        }

        public Builder bgStartY(int startY) {
            bgImgOptions.startY(startY);
            return this;
        }

        /////////////// logo 配置结束 ///////////////

        // ------------------------------------------

        /////////////// 探测图形 相关配置 ///////////////
        public Builder detectImg(String detectImg) throws IOException {
            try {
                return detectImg(ImageUtils.getImageByPath(detectImg));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        public Builder detectImg(InputStream is) throws IOException {
            try {
                return detectImg(ImageIO.read(is));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        public Builder detectImg(BufferedImage detectImg) {
            detectOptions.detectImg(detectImg);
            return this;
        }

        /**
         * 左上角探测图形
         */
        public Builder detectImgLT(String detectImgLT) throws IOException {
            try {
                return detectImgLT(ImageUtils.getImageByPath(detectImgLT));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        /**
         * 左上角探测图形
         */
        public Builder detectImgLT(InputStream is) throws IOException {
            try {
                return detectImgLT(ImageIO.read(is));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        /**
         * 左上角探测图形
         */
        public Builder detectImgLT(BufferedImage detectImgLT) {
            detectOptions.detectImgLT(detectImgLT);
            return this;
        }

        /**
         * 右上角探测图形
         */
        public Builder detectImgRT(String detectImgRT) throws IOException {
            try {
                return detectImgRT(ImageUtils.getImageByPath(detectImgRT));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        /**
         * 右上角探测图形
         */
        public Builder detectImgRT(InputStream is) throws IOException {
            try {
                return detectImgRT(ImageIO.read(is));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        /**
         * 右上角探测图形
         */
        public Builder detectImgRT(BufferedImage detectImg) {
            detectOptions.detectImgRT(detectImg);
            return this;
        }

        /**
         * 左下角探测图形
         */
        public Builder detectImgLD(String detectImgLD) throws IOException {
            try {
                return detectImgLD(ImageUtils.getImageByPath(detectImgLD));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        /**
         * 左下角探测图形
         */
        public Builder detectImgLD(InputStream is) throws IOException {
            try {
                return detectImgLD(ImageIO.read(is));
            } catch (IOException e) {
//                log.error("load detectImage error! e:{}", e);
                throw new IOException("load detectImage error!", e);
            }
        }

        /**
         * 左下角探测图形
         */
        public Builder detectImgLD(BufferedImage detectImgLD) {
            detectOptions.detectImgLD(detectImgLD);
            return this;
        }

        public Builder detectOutColor(Integer outColor) {
            if (outColor == null) {
                return this;
            }

            return detectOutColor(ColorUtils.int2color(outColor));
        }

        public Builder detectOutColor(Color outColor) {
            detectOptions.outColor(outColor);
            return this;
        }

        public Builder detectInColor(Integer inColor) {
            if (inColor == null) {
                return this;
            }

            return detectInColor(ColorUtils.int2color(inColor));
        }

        public Builder detectInColor(Color inColor) {
            detectOptions.inColor(inColor);
            return this;
        }

        /////////////// 探测图形 配置结束 ///////////////

        // ------------------------------------------

        /////////////// 二维码绘制 相关配置 ///////////////

        public Builder drawStyle(String style) {
            return drawStyle(DrawOptions.DrawStyle.getDrawStyle(style));
        }


        public Builder drawStyle(DrawOptions.DrawStyle drawStyle) {
            drawOptions.drawStyle(drawStyle);
            return this;
        }

        public Builder transparencyFill(boolean fill) {
            drawOptions.transparencyFill(fill);
            return this;
        }

        public Builder drawPreColor(int color) {
            return drawPreColor(ColorUtils.int2color(color));
        }

        public Builder drawPreColor(Color color) {
            drawOptions.preColor(color);
            return this;
        }

        public Builder drawBgColor(int color) {
            return drawBgColor(ColorUtils.int2color(color));
        }

        public Builder drawBgColor(Color color) {
            drawOptions.bgColor(color);
            return this;
        }

        public Builder drawBgImg(String img) throws IOException {
            try {
                return drawBgImg(ImageUtils.getImageByPath(img));
            } catch (IOException e) {
//                log.error("load drawBgImg error! e:{}", e);
                throw new IOException("load drawBgImg error!", e);
            }
        }

        public Builder drawBgImg(InputStream img) throws IOException {
            try {
                return drawBgImg(ImageIO.read(img));
            } catch (IOException e) {
//                log.error("load drawBgImg error! e:{}", e);
                throw new IOException("load drawBgImg error!", e);
            }
        }

        public Builder drawBgImg(BufferedImage img) {
            drawOptions.bgImage(img);
            return this;
        }

        public Builder setDrawEnableScale(boolean enable) {
            drawOptions.enableScale(enable);
            return this;
        }

        public Builder setDrawImg(String img) throws IOException {
            try {
                return setDrawImg(ImageUtils.getImageByPath(img));
            } catch (IOException e) {
//                log.error("load draw img error! e: {}", e);
                throw new IOException("load draw img error!", e);
            }
        }

        public Builder setDrawImg(InputStream input) throws IOException {
            try {
                return setDrawImg(ImageIO.read(input));
            } catch (IOException e) {
//                log.error("load draw img error! e: {}", e);
                throw new IOException("load draw img error!", e);
            }
        }

        public Builder setDrawImg(BufferedImage img) {
            addImg(1, 1, img);
            return this;
        }

        public Builder addImg(int row, int col, BufferedImage img) {
            if (img == null) {
                return this;
            }
            drawOptions.enableScale(true);
            drawOptions.drawImage(row, col, img);
            return this;
        }

        public Builder addImg(int row, int col, String img) throws IOException {
            try {
                return addImg(row, col, ImageUtils.getImageByPath(img));
            } catch (IOException e) {
//                log.error("load draw size4img error! e: {}", e);
                throw new IOException("load draw row:" + row + ", col:" + col + " img error!", e);
            }
        }

        public Builder addImg(int row, int col, InputStream img) throws IOException {
            try {
                return addImg(row, col, ImageIO.read(img));
            } catch (IOException e) {
//                log.error("load draw size4img error! e: {}", e);
                throw new IOException("load draw row:" + row + ", col:" + col + " img error!", e);
            }
        }

        /////////////// 二维码绘制 配置结束 ///////////////

        private QrCodeOptions build() {
            if (content == null || content.length() == 0) {
                throw new IllegalArgumentException("二维码的内容不能为空!");
            }
            QrCodeOptions qrCodeOptions = new QrCodeOptions();
            qrCodeOptions.setContent(content);
            qrCodeOptions.setWidth(getWidth());
            qrCodeOptions.setHeight(getHeight());
            // 设置背景信息
            BgImageOptions bgImageOpt = bgImgOptions.build();
            if (bgImageOpt.getBgImage() == null && bgImageOpt.getGifDecoder() == null) {
                qrCodeOptions.setBgImageOptions(null);
            } else {
                qrCodeOptions.setBgImageOptions(bgImageOpt);
            }

            // 设置logo信息
            LogoOptions logoOpt = logoOptions.build();
            if (logoOpt.getLogo() == null) {
                qrCodeOptions.setLogoOptions(null);
            } else {
                qrCodeOptions.setLogoOptions(logoOpt);
            }

            // 绘制信息
            DrawOptions drawOpt = drawOptions.build();
            qrCodeOptions.setDrawOptions(drawOpt);

            // 设置detect绘制信息
            DetectOptions detectOpt = detectOptions.build();
            if (detectOpt.getOutColor() == null && detectOpt.getInColor() == null) {
                detectOpt.setInColor(drawOpt.getPreColor());
                detectOpt.setOutColor(drawOpt.getPreColor());
            } else if (detectOpt.getOutColor() == null) {
                detectOpt.setOutColor(detectOpt.getOutColor());
            } else if (detectOpt.getInColor() == null) {
                detectOpt.setInColor(detectOpt.getInColor());
            }
            qrCodeOptions.setDetectOptions(detectOpt);

            if (qrCodeOptions.getBgImageOptions() != null && qrCodeOptions.getBgImageOptions().getBgImageStyle() == BgImageOptions.BgImageStyle.PENETRATE) {
                // 透传，用背景图颜色进行绘制时
                drawOpt.setPreColor(ColorUtils.OPACITY);
                qrCodeOptions.getBgImageOptions().setOpacity(1);
                qrCodeOptions.getDetectOptions().setInColor(ColorUtils.OPACITY);
                qrCodeOptions.getDetectOptions().setOutColor(ColorUtils.OPACITY);
            }

            // 设置输出图片格式
            qrCodeOptions.setPicFormat(picFormat);

            // 设置精度参数
            Map<EncodeHintType, Object> hints = new HashMap<>(3);
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            hints.put(EncodeHintType.CHARACTER_SET, code);
            hints.put(EncodeHintType.MARGIN, getPadding());
            qrCodeOptions.setHints(hints);

            return qrCodeOptions;
        }

        public String asBase64() {
            try {
                return QrCodeHelper.asBase64(build());
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BufferedImage asBufferedImage() {
            try {
                return QrCodeHelper.asBufferedImage(build());
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
        }

        public ByteArrayOutputStream asStream() {
            try {
                QrCodeOptions options = build();
                if (options.isGif()) {
                    return QrCodeHelper.asGif(options);
                } else {
                    BufferedImage img = QrCodeHelper.asBufferedImage(options);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(img, options.getPicFormat(), outputStream);
                    return outputStream;
                }
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean asFile(String absFileName) {
            try {
                return QrCodeHelper.asFile(build(), absFileName);
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
