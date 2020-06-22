package org.springultron.qrcode.util;

import org.springultron.qrcode.gif.GifDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理操作工具类
 *
 * @author brucewuu
 * @date 2020/6/5 11:02
 */
public final class ImageUtils {

    private ImageUtils() {
    }

    /**
     * 生成圆角图片
     *
     * @param image        原始图片
     * @param cornerRadius 圆角的弧度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @return BufferedImage
     */
    public static BufferedImage makeRoundCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }

    /**
     * 生成边框
     *
     * @param image        原图
     * @param cornerRadius 角度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @param color        边框颜色
     * @return BufferedImage
     */
    public static BufferedImage makeRoundBorder(BufferedImage image, int cornerRadius, Color color) {
        int size = image.getWidth() / 15;
        int w = image.getWidth() + size;
        int h = image.getHeight() + size;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color == null ? Color.WHITE : color);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2.drawImage(image, size >> 1, size >> 1, null);
        g2.dispose();

        return output;
    }

    /**
     * 根据路径获取图片
     *
     * @param path 本地路径/网络路径
     * @return 图片
     * @throws IOException IO异常
     */
    public static BufferedImage getImageByPath(String path) throws IOException {
        if (null == path || "".equals(path)) {
            return null;
        }

        InputStream stream = FileReadUtils.getStreamByFileName(path);
        return ImageIO.read(stream);
    }

    /**
     * 根据路径获取gif图片
     *
     * @param path 本地路径/网络路径
     * @return 图片
     * @throws IOException IO异常
     */
    public static GifDecoder getGifByPath(String path) throws IOException {
        if (null == path || "".equals(path)) {
            return null;
        }

        GifDecoder decoder = new GifDecoder();
        decoder.read(FileReadUtils.getStreamByFileName(path));
        return decoder;
    }

}
