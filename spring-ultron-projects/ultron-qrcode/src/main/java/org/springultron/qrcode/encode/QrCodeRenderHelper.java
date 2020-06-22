package org.springultron.qrcode.encode;

import com.google.zxing.qrcode.encoder.ByteMatrix;
import org.springultron.qrcode.option.*;
import org.springultron.qrcode.tuple.ImmutablePair;
import org.springultron.qrcode.util.GraphicUtils;
import org.springultron.qrcode.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 二维码渲染辅助类
 * 主要用于绘制背景，logo，定位点，二维码信息
 */
public class QrCodeRenderHelper {

    /**
     * 绘制logo图片
     *
     * @param qrImg       二维码图
     * @param logoOptions logo配置选项
     */
    public static BufferedImage drawLogo(BufferedImage qrImg, LogoOptions logoOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 获取logo图片
        BufferedImage logoImg = logoOptions.getLogo();

        // 绘制圆角图片
        int radius = 0;
        if (logoOptions.getLogoStyle() == LogoOptions.LogoStyle.ROUND) {
            radius = logoImg.getWidth() >> 2;
            logoImg = ImageUtils.makeRoundCorner(logoImg, radius);
        }

        // 绘制边框
        if (logoOptions.isBorder()) {
            if (logoOptions.getOuterBorderColor() != null) {
                logoImg = ImageUtils.makeRoundBorder(logoImg, radius, logoOptions.getOuterBorderColor());
            }

            logoImg = ImageUtils.makeRoundBorder(logoImg, radius, logoOptions.getBorderColor());
        }

        // logo的宽高
        int logoRate = logoOptions.getRate();
        int logoWidth = Math.min(logoImg.getWidth(), (qrWidth << 1) / logoRate);
        int logoHeight = Math.min(logoImg.getHeight(), (qrHeight << 1) / logoRate);

        int logoOffsetX = (qrWidth - logoWidth) >> 1;
        int logoOffsetY = (qrHeight - logoHeight) >> 1;

        // 插入LOGO
        Graphics2D qrImgGraphic = qrImg.createGraphics();

        if (logoOptions.getOpacity() != null) {
            qrImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, logoOptions.getOpacity()));
        }
        qrImgGraphic.drawImage(logoImg, logoOffsetX, logoOffsetY, logoWidth, logoHeight, null);
        qrImgGraphic.dispose();

        logoImg.flush();

        return qrImg;
    }

    /**
     * 绘制背景图
     *
     * @param qrImg          二维码图
     * @param bgImageOptions 背景图配置选项
     * @return BufferedImage
     */
    public static BufferedImage drawBackground(BufferedImage qrImg, BgImageOptions bgImageOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 背景的图宽高不应该小于原图
        int bgW = Math.max(bgImageOptions.getWidth(), qrWidth);
        int bgH = Math.max(bgImageOptions.getHeight(), qrHeight);

        // 背景图缩放
        BufferedImage bgImg = bgImageOptions.getBgImage();
        if (bgImg.getWidth() != bgW || bgImg.getHeight() != bgH) {
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
            temp.getGraphics().drawImage(bgImg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH), 0, 0, null);
            bgImg = temp;
        }

        Graphics2D bgImgGraphic = bgImg.createGraphics();
        if (bgImageOptions.getBgImageStyle() == BgImageOptions.BgImageStyle.FILL) {
            // 选择一块区域进行填充
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            bgImgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            bgImgGraphic.drawImage(qrImg, bgImageOptions.getStartX(), bgImageOptions.getStartY(), qrWidth, qrHeight, null);
        } else {
            // 全覆盖方式
            int bgOffsetX = (bgW - qrWidth) >> 1;
            int bgOffsetY = (bgH - qrHeight) >> 1;
            // 设置透明度， 避免看不到背景
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, bgImageOptions.getOpacity()));
            bgImgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            bgImgGraphic.drawImage(qrImg, bgOffsetX, bgOffsetY, qrWidth, qrHeight, null);
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        }
        bgImgGraphic.dispose();

        bgImg.flush();

        return bgImg;
    }

    /**
     * 动态背景图绘制
     *
     * @param qrImg          二维码图
     * @param bgImageOptions 背景图配置选项
     */
    public static List<ImmutablePair<BufferedImage, Integer>> drawGifBackground(BufferedImage qrImg, BgImageOptions bgImageOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();
        // 背景的图宽高不应该小于原图
        int bgW = Math.max(bgImageOptions.getWidth(), qrWidth);
        int bgH = Math.max(bgImageOptions.getHeight(), qrHeight);

        // 覆盖方式
        boolean fillMode = bgImageOptions.getBgImageStyle() == BgImageOptions.BgImageStyle.FILL;
        int bgOffsetX = fillMode ? bgImageOptions.getStartX() : (bgW - qrWidth) >> 1;
        int bgOffsetY = fillMode ? bgImageOptions.getStartY() : (bgH - qrHeight) >> 1;

        int gifImgLen = bgImageOptions.getGifDecoder().getFrameCount();
        List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(gifImgLen);
        // 背景图缩放
        for (int index = 0, len = bgImageOptions.getGifDecoder().getFrameCount(); index < len; index++) {
            BufferedImage bgImg = bgImageOptions.getGifDecoder().getFrame(index);
            // fixme 当背景图为png时，最终透明的地方会是黑色，这里兼容处理成白色
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_RGB);
            temp.getGraphics().setColor(Color.WHITE);
            temp.getGraphics().fillRect(0, 0, bgW, bgH);
            temp.getGraphics().drawImage(bgImg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH), 0, 0, null);
            bgImg = temp;

            Graphics2D bgGraphic = bgImg.createGraphics();
            if (bgImageOptions.getBgImageStyle() == BgImageOptions.BgImageStyle.FILL) {
                // 选择一块区域进行填充
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                bgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                bgGraphic.drawImage(qrImg, bgOffsetX, bgOffsetY, qrWidth, qrHeight, null);
            } else {
                // 全覆盖模式, 设置透明度， 避免看不到背景
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, bgImageOptions.getOpacity()));
                bgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                bgGraphic.drawImage(qrImg, bgOffsetX, bgOffsetY, qrWidth, qrHeight, null);
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            }
            bgGraphic.dispose();
            bgImg.flush();

            result.add(ImmutablePair.of(bgImg, bgImageOptions.getGifDecoder().getDelay(index)));
        }

        return result;
    }

    /**
     * 根据二维码矩阵，生成对应的二维码推片
     *
     * @param qrCodeOptions 二维码配置选项
     * @param bitMatrix     矩阵信息
     * @return BufferedImage
     */
    public static BufferedImage drawQrInfo(QrCodeOptions qrCodeOptions, BitMatrixEx bitMatrix) {
        int qrWidth = bitMatrix.getWidth();
        int qrHeight = bitMatrix.getHeight();
        int infoSize = bitMatrix.getMultiple();
        BufferedImage qrImg = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_ARGB);


        // 绘制的背景色
        Color bgColor = qrCodeOptions.getDrawOptions().getBgColor();
        // 绘制前置色
        Color preColor = qrCodeOptions.getDrawOptions().getPreColor();

        // 探测图形外圈的颜色
        Color detectOutColor = qrCodeOptions.getDetectOptions().getOutColor();
        // 探测图形内圈的颜色
        Color detectInnerColor = qrCodeOptions.getDetectOptions().getInColor();

        if (detectInnerColor != null || detectOutColor != null) {
            if (detectInnerColor == null) {
                detectInnerColor = detectOutColor;
            } else if (detectOutColor == null) {
                detectOutColor = detectInnerColor;
            }
        }

        int leftPadding = bitMatrix.getLeftPadding();
        int topPadding = bitMatrix.getTopPadding();

        Graphics2D g2 = GraphicUtils.getG2d(qrImg);
        if (!qrCodeOptions.getDrawOptions().isTransparencyFill()) {
            // 当二维码中的透明区域，不填充时，如下设置，可以让图片中的透明度覆盖背景色
            g2.setComposite(AlphaComposite.Src);
        }

        // 直接背景铺满整个图
        g2.setColor(bgColor);
        g2.fillRect(0, 0, qrWidth, qrHeight);

        // 探测图形的大小
        int detectCornerSize = bitMatrix.getByteMatrix().get(0, 5) == 1 ? 7 : 5;

        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();

        DrawOptions.DrawStyle drawStyle = qrCodeOptions.getDrawOptions().getDrawStyle();
        DetectOptions.DetectLocation detectLocation;
        for (int x = 0; x < matrixW; x++) {
            for (int y = 0; y < matrixH; y++) {
                detectLocation = inDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize);
                if (bitMatrix.getByteMatrix().get(x, y) == 0) {
                    // 探测图形内部的元素与二维码的01点图绘制逻辑分开
                    // 绘制二维码中不在探测图形内部的0点图
                    if (!detectLocation.detectedArea()) {
                        drawQrDotBgImg(qrCodeOptions, g2, leftPadding, topPadding, infoSize, x, y);
                    }
                    continue;
                }

                if (detectLocation.detectedArea()) {
                    // 绘制三个位置探测图形
                    drawDetectImg(qrCodeOptions, g2, bitMatrix, matrixW, matrixH, leftPadding, topPadding, infoSize,
                            detectCornerSize, x, y, detectOutColor, detectInnerColor, detectLocation);
                } else {
                    g2.setColor(preColor);
                    // 绘制二维码的1点图
                    drawQrDotImg(qrCodeOptions, drawStyle, g2, bitMatrix, leftPadding, topPadding, infoSize, x, y);
                }
            }
        }

        g2.dispose();

        return qrImg;
    }

    /**
     * 判断 (x,y) 对应的点是否处于二维码矩阵的探测图形内
     *
     * @param x                目标点的x坐标
     * @param y                目标点的y坐标
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param detectCornerSize 探测图形的大小
     * @return DetectLocation
     */
    private static DetectOptions.DetectLocation inDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        if (x < detectCornerSize && y < detectCornerSize) {
            // 左上角
            return DetectOptions.DetectLocation.LT;
        }

        if (x < detectCornerSize && y >= matrixH - detectCornerSize) {
            // 左下角
            return DetectOptions.DetectLocation.LD;
        }

        if (x >= matrixW - detectCornerSize && y < detectCornerSize) {
            // 右上角
            return DetectOptions.DetectLocation.RT;
        }

        return DetectOptions.DetectLocation.NONE;
    }

    /**
     * 判断 (x,y) 对应的点是否为二维码举证探测图形中外面的框, 这个方法的调用必须在确认(x,y)对应的点在探测图形内
     *
     * @param x                目标点的x坐标
     * @param y                目标点的y坐标
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param detectCornerSize 探测图形的大小
     */
    private static boolean inOuterDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        // 外层的框
        return x == 0 || x == detectCornerSize - 1 || x == matrixW - 1 || x == matrixW - detectCornerSize || y == 0 ||
                y == detectCornerSize - 1 || y == matrixH - 1 || y == matrixH - detectCornerSize;
    }

    /**
     * 绘制探测图形
     *
     * @param qrCodeOptions    绘制参数
     * @param g2               二维码画布
     * @param bitMatrix        二维码矩阵
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param leftPadding      二维码左边留白距离
     * @param topPadding       二维码上边留白距离
     * @param infoSize         二维码矩阵中一个点对应的像素大小
     * @param detectCornerSize 探测图形大小
     * @param x                目标点x坐标
     * @param y                目标点y坐标
     * @param detectOutColor   探测图形外边圈的颜色
     * @param detectInnerColor 探测图形内部圈的颜色
     */
    private static void drawDetectImg(QrCodeOptions qrCodeOptions, Graphics2D g2, BitMatrixEx bitMatrix,
                                      int matrixW, int matrixH, int leftPadding, int topPadding, int infoSize,
                                      int detectCornerSize, int x, int y, Color detectOutColor, Color detectInnerColor,
                                      DetectOptions.DetectLocation detectLocation) {
        BufferedImage detectedImg = qrCodeOptions.getDetectOptions().chooseDetectedImg(detectLocation);
        if (detectedImg != null) {
            // 使用探测图形的图片来渲染
            g2.drawImage(detectedImg, leftPadding + x * infoSize, topPadding + y * infoSize,
                    infoSize * detectCornerSize, infoSize * detectCornerSize, null);

            // 图片直接渲染完毕之后，将其他探测图形的点设置为0，表示不需要再次渲染
            for (int addX = 0; addX < detectCornerSize; addX++) {
                for (int addY = 0; addY < detectCornerSize; addY++) {
                    bitMatrix.getByteMatrix().set(x + addX, y + addY, 0);
                }
            }
            return;
        }

        if (inOuterDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize)) {
            // 外层的框
            g2.setColor(detectOutColor);
        } else {
            // 内层的框
            g2.setColor(detectInnerColor);
        }

        g2.fillRect(leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize);
    }

    private static void drawQrDotBgImg(QrCodeOptions qrCodeOptions, Graphics2D g2, int leftPadding, int topPadding, int infoSize, int x, int y) {
        if (qrCodeOptions.getDrawOptions().getBgImage() == null) {
            return;
        }

        // 绘制二维码背景图
        g2.drawImage(qrCodeOptions.getDrawOptions().getBgImage(), leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize, null);
    }

    /**
     * 绘制二维码中的像素点图形
     *
     * @param qrCodeOptions 绘制参数
     * @param drawStyle     绘制的图形样式
     * @param g2            二维码画布
     * @param bitMatrix     二维码矩阵
     * @param leftPadding   二维码左边留白距离
     * @param topPadding    二维码上边留白距离
     * @param infoSize      二维码矩阵中一个点对应的像素大小
     * @param x             目标点x坐标
     * @param y             目标点y坐标
     */
    private static void drawQrDotImg(QrCodeOptions qrCodeOptions, DrawOptions.DrawStyle drawStyle,
                                     Graphics2D g2, BitMatrixEx bitMatrix, int leftPadding, int topPadding,
                                     int infoSize, int x, int y) {
        if (drawStyle != DrawOptions.DrawStyle.IMAGE) {
            drawGeometricFigure(qrCodeOptions, drawStyle, g2, bitMatrix, leftPadding, topPadding, infoSize, x, y);
        } else {
            drawSpecialImg(qrCodeOptions, drawStyle, g2, bitMatrix, leftPadding, topPadding, infoSize, x, y);
        }
    }

    /**
     * 绘制自定义的几种几何图形
     *
     * @param qrCodeOptions 绘制参数
     * @param drawStyle     绘制的图形样式
     * @param g2            二维码画布
     * @param bitMatrix     二维码矩阵
     * @param leftPadding   二维码左边留白距离
     * @param topPadding    二维码上边留白距离
     * @param infoSize      二维码矩阵中一个点对应的像素大小
     * @param x             目标点x坐标
     * @param y             目标点y坐标
     */
    private static void drawGeometricFigure(QrCodeOptions qrCodeOptions, DrawOptions.DrawStyle drawStyle, Graphics2D g2, BitMatrixEx bitMatrix, int leftPadding, int topPadding, int infoSize, int x, int y) {
        if (!qrCodeOptions.getDrawOptions().isEnableScale()) {
            // 用几何图形进行填充时，如果不支持多个像素点渲染一个几何图形时，直接返回即可
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize, qrCodeOptions.getDrawOptions().getImage(1, 1));
            return;
        }

        // todo 针对非图片的二维码扩展，先保留原来的扩展逻辑，后续优化
        boolean row2 = x + 1 < bitMatrix.getByteMatrix().getWidth() && bitMatrix.getByteMatrix().get(x + 1, y) == 1;
        boolean col2 = y + 1 < bitMatrix.getByteMatrix().getHeight() && bitMatrix.getByteMatrix().get(x, y + 1) == 1;

        if (row2 && col2 && bitMatrix.getByteMatrix().get(x + 1, y + 1) == 1 &&
                drawStyle.expand(DrawOptions.ExpandType.SIZE4)) {
            // 四个相等
            bitMatrix.getByteMatrix().set(x + 1, y, 0);
            bitMatrix.getByteMatrix().set(x + 1, y + 1, 0);
            bitMatrix.getByteMatrix().set(x, y + 1, 0);
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize << 1, infoSize << 1, qrCodeOptions.getDrawOptions().getImage(2, 2));
        } else if (row2 && drawStyle.expand(DrawOptions.ExpandType.ROW2)) {
            // 横向相同
            bitMatrix.getByteMatrix().set(x + 1, y, 0);
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize << 1, infoSize, qrCodeOptions.getDrawOptions().getImage(2, 1));
        } else if (col2 && drawStyle.expand(DrawOptions.ExpandType.COL2)) {
            // 列的两个
            bitMatrix.getByteMatrix().set(x, y + 1, 0);
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize << 1, qrCodeOptions.getDrawOptions().getImage(1, 2));
        } else {
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize, qrCodeOptions.getDrawOptions().getImage(1, 1));
        }
    }

    /**
     * 绘制指定的图片
     *
     * @param qrCodeOptions 绘制参数
     * @param drawStyle     绘制的图形样式
     * @param g2            二维码画布
     * @param bitMatrix     二维码矩阵
     * @param leftPadding   二维码左边留白距离
     * @param topPadding    二维码上边留白距离
     * @param infoSize      二维码矩阵中一个点对应的像素大小
     * @param x             目标点x坐标
     * @param y             目标点y坐标
     */
    private static void drawSpecialImg(QrCodeOptions qrCodeOptions, DrawOptions.DrawStyle drawStyle, Graphics2D g2, BitMatrixEx bitMatrix, int leftPadding, int topPadding, int infoSize, int x, int y) {
        // 针对图片扩展的方式，支持更加灵活的填充方式
        int maxRow = getMaxRow(bitMatrix.getByteMatrix(), x, y);
        int maxCol = getMaxCol(bitMatrix.getByteMatrix(), x, y);
        List<DotSize> availableSize = getAvailableSize(bitMatrix.getByteMatrix(), x, y, maxRow, maxCol);
        // 获取对应的图片
        BufferedImage drawImg;
        for (DotSize dotSize : availableSize) {
            drawImg = qrCodeOptions.getDrawOptions().getImageMap().get(dotSize);
            if (drawImg == null) {
                continue;
            }

            // 开始绘制，并将已经绘制过得地方置空
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, dotSize.getCol() * infoSize, dotSize.getRow() * infoSize, drawImg);
            for (int col = 0; col < dotSize.getCol(); col++) {
                for (int row = 0; row < dotSize.getRow(); row++) {
                    bitMatrix.getByteMatrix().set(x + col, y + row, 0);
                }
            }
            return;
        }

        // 如果上面全部没有满足，则使用兜底的绘制
        drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize, qrCodeOptions.getDrawOptions().getImage(1, 1));
    }

    /**
     * 获取矩阵中从(x,y)出发最大连续为1的行数
     *
     * @param bitMatrix 矩阵
     * @param x         起始点x
     * @param y         起始点y
     */
    private static int getMaxRow(ByteMatrix bitMatrix, int x, int y) {
        int cnt = 1;
        while (++y < bitMatrix.getHeight()) {
            if (bitMatrix.get(x, y) == 0) {
                break;
            }
            ++cnt;
        }
        return cnt;
    }

    /**
     * 获取矩阵中从(x,y)出发最大连续为1的列数
     *
     * @param bitMatrix 矩阵
     * @param x         起始点x
     * @param y         起始点y
     */
    private static int getMaxCol(ByteMatrix bitMatrix, int x, int y) {
        int cnt = 1;
        while (++x < bitMatrix.getWidth()) {
            if (bitMatrix.get(x, y) == 0) {
                break;
            }
            ++cnt;
        }
        return cnt;
    }

    private static List<DotSize> getAvailableSize(ByteMatrix bitMatrix, int x, int y, int maxRow, int maxCol) {
        if (maxRow == 1) {
            return Collections.singletonList(DotSize.create(1, maxCol));
        }

        if (maxCol == 1) {
            return Collections.singletonList(DotSize.create(maxRow, 1));
        }

        List<DotSize> container = new ArrayList<>();

        int col = 1;
        int lastRow = maxRow;
        while (col < maxCol) {
            int offset = 0;
            int row = 1;
            while (++offset < lastRow) {
                if (bitMatrix.get(x + col, y + offset) == 0) {
                    break;
                }
                ++row;
            }
            ++col;
            lastRow = row;
            container.add(new DotSize(row, col));
        }

        container.sort((o1, o2) -> o2.size() - o1.size());
        return container;
    }
}
