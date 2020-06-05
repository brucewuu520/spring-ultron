package org.springultron.qrcode.option;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码信息(即传统二维码中的黑色方块) 绘制选项
 *
 * @author brucewuu
 * @date 2020/6/4 19:31
 */
public class DrawOptions {
    /**
     * 背景颜色
     */
    private final Color bgColor;
    /**
     * 绘制前置颜色
     */
    private final Color preColor;
    /**
     * 绘制样式
     */
    private final DrawStyle drawStyle;
    /**
     * 绘制的背景图片
     */
    private final BufferedImage bgImage;
    /**
     * true 时表示支持对相邻的着色点进行合并处理 （即用一个大图来绘制相邻的两个着色点）
     * 说明： 三角形样式关闭该选项，因为留白过多，对识别有影响
     */
    private final boolean enableScale;
    /**
     * 透明度填充，如绘制二维码的图片中存在透明区域，
     * 若这个参数为true，则会用bgColor填充透明的区域；若为false，则透明区域依旧是透明的
     */
    private final boolean transparencyFill;
    /**
     * 渲染图
     */
    private final Map<DotSize, BufferedImage> imageMap;

    private DrawOptions(Color bgColor, Color preColor, DrawStyle drawStyle, BufferedImage bgImage, boolean enableScale, boolean transparencyFill, Map<DotSize, BufferedImage> imageMap) {
        this.bgColor = bgColor;
        this.preColor = preColor;
        this.drawStyle = drawStyle;
        this.bgImage = bgImage;
        this.enableScale = enableScale;
        this.transparencyFill = transparencyFill;
        this.imageMap = imageMap;
    }

    public static DrawOptions.Builder builder() {
        return new DrawOptions.Builder();
    }

    public Color getBgColor() {
        return bgColor;
    }

    public Color getPreColor() {
        return preColor;
    }

    public DrawStyle getDrawStyle() {
        return drawStyle;
    }

    public BufferedImage getBgImage() {
        return bgImage;
    }

    public boolean isEnableScale() {
        return enableScale;
    }

    public boolean isTransparencyFill() {
        return transparencyFill;
    }

    public Map<DotSize, BufferedImage> getImageMap() {
        return imageMap;
    }

    public BufferedImage getImage(int row, int col) {
        return imageMap.get(DotSize.create(row, col));
    }

    public static class Builder {
        private Color bgColor;
        private Color preColor;
        private DrawStyle drawStyle;
        private BufferedImage bgImage;
        private boolean enableScale;
        private boolean transparencyFill;
        private Map<DotSize, BufferedImage> imageMap;

        public Builder bgColor(Color bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder preColor(Color preColor) {
            this.preColor = preColor;
            return this;
        }

        public Builder drawStyle(DrawStyle drawStyle) {
            this.drawStyle = drawStyle;
            return this;
        }

        public Builder bgImage(BufferedImage bgImage) {
            this.bgImage = bgImage;
            return this;
        }

        public Builder enableScale(boolean enableScale) {
            this.enableScale = enableScale;
            return this;
        }

        public Builder transparencyFill(boolean transparencyFill) {
            this.transparencyFill = transparencyFill;
            return this;
        }

        public Builder imageMap(Map<DotSize, BufferedImage> imageMap) {
            this.imageMap = imageMap;
            return this;
        }

        public Builder drawImage(int row, int col, BufferedImage image) {
            if (imageMap == null) {
                imageMap = new HashMap<>(3);
            }
            imageMap.put(new DotSize(row, col), image);
            return this;
        }

        public DrawOptions build() {
            return new DrawOptions(bgColor, preColor, drawStyle, bgImage, enableScale, transparencyFill, imageMap);
        }
    }

    /**
     * 绘制二维码信息的样式
     */
    public enum DrawStyle {
        RECTANGLE { // 矩形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img) {
                g2d.fillRect(x, y, w, h);
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return true;
            }
        },
        CIRCULAR { // 圆形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img) {
                g2d.fill(new Ellipse2D.Float(x, y, w, h));
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return expandType == ExpandType.SIZE4;
            }
        },
        TRIANGLE { // 三角形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img) {
                int[] px = {x, x + (w >> 1), x + w};
                int[] py = {y + w, y, y + w};
                g2d.fillPolygon(px, py, 3);
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return false;
            }
        },
        PENTAGON { // 五边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img) {
                int cell4 = size >> 2;
                int cell2 = size >> 1;
                int[] px = {x + cell4, x + size - cell4, x + size, x + cell2, x};
                int[] py = {y, y, y + cell2, y + size, y + cell2};
                g2d.fillPolygon(px, py, 5);
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return expandType == ExpandType.SIZE4;
            }
        },
        HEXAGON { // 六边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img) {
                int add = size >> 2;
                int[] px = {x + add, x + size - add, x + size, x + size - add, x + add, x};
                int[] py = {y, y, y + add + add, y + size, y + size, y + add + add};
                g2d.fillPolygon(px, py, 6);
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return expandType == ExpandType.SIZE4;
            }
        },
        OCTAGON { // 八边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img) {
                int add = size / 3;
                int[] px = {x + add, x + size - add, x + size, x + size, x + size - add, x + add, x, x};
                int[] py = {y, y, y + add, y + size - add, y + size, y + size, y + size - add, y + add};
                g2d.fillPolygon(px, py, 8);
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return expandType == ExpandType.SIZE4;
            }
        },
        IMAGE { // 自定义图片

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img) {
                g2d.drawImage(img, x, y, w, h, null);
            }

            @Override
            public boolean expand(ExpandType expandType) {
                return true;
            }
        };

        private static Map<String, DrawStyle> map;

        static {
            map = new HashMap<>(10);
            for (DrawStyle style : DrawStyle.values()) {
                map.put(style.name(), style);
            }
        }

        public static DrawStyle getDrawStyle(String name) {
            if (name == null || "".equals(name)) { // 默认返回矩形
                return RECTANGLE;
            }

            DrawStyle style = map.get(name.toUpperCase());
            return style == null ? RECTANGLE : style;
        }

        public abstract void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img);

        /**
         * 返回是否支持绘制自定义图形的扩展
         */
        public abstract boolean expand(ExpandType expandType);
    }

    public enum ExpandType {
        ROW2, COL2, SIZE4
    }
}
