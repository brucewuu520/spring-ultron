package org.springultron.captcha;

import org.springframework.core.io.ClassPathResource;
import org.springultron.captcha.draw.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 * 图片验证码
 *
 * @author brucewuu
 * @date 2021/4/13 下午1:58
 */
public class Captcha {
    /**
     * 默认的验证码大小，暂时不支持外部设置，因为字体大小是写死的，后期会加自动计算
     */
    private static final int WIDTH = 130, HEIGHT = 48;
    private static final String[] FONT_NAMES = new String[]{"001.ttf", "002.ttf", "003.ttf", "004.ttf"};
    private BackgroundDraw backgroundDraw;
    private CaptchaDraw captchaDraw;
    private InterferenceDraw interferenceDraw;
    private Random random;
    private final Font[] fonts;

    public Captcha() {
        this(CaptchaType.RANDOM);
    }

    public Captcha(CaptchaType type) {
        this(type.getCaptchaDraw());
    }

    public Captcha(CaptchaDraw captchaDraw) {
        this(SmallCharsBackgroundDraw.INSTANCE, captchaDraw, CurveInterferenceDraw.INSTANCE, new SecureRandom());
    }

    public Captcha(BackgroundDraw backgroundDraw, CaptchaDraw captchaDraw, InterferenceDraw interferenceDraw, Random random) {
        this.backgroundDraw = backgroundDraw;
        this.captchaDraw = captchaDraw;
        this.interferenceDraw = interferenceDraw;
        this.random = random;
        this.fonts = loadFonts();
    }

    public void setBackgroundDraw(BackgroundDraw backgroundDraw) {
        this.backgroundDraw = Objects.requireNonNull(backgroundDraw, "BackgroundDraw is null.");
    }

    public void setCaptchaDraw(CaptchaDraw captchaDraw) {
        this.captchaDraw = Objects.requireNonNull(captchaDraw, "CaptchaDraw is null.");
    }

    public void setInterferenceDraw(InterferenceDraw interferenceDraw) {
        this.interferenceDraw = Objects.requireNonNull(interferenceDraw, "InterferenceDraw is null.");
    }

    public void setRandom(Random random) {
        this.random = Objects.requireNonNull(random, "Random is null.");
    }

    /**
     * 生成验证码
     */
    public String generate(Supplier<OutputStream> supplier) {
        // 初始化画布
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = initGraphics(image);
        // 背景层
        backgroundDraw.draw(graphics, WIDTH, HEIGHT, fonts, random);
        // 验证码层
        String captcha = captchaDraw.draw(graphics, WIDTH, HEIGHT, fonts, random);
        // 干扰层
        interferenceDraw.draw(graphics, WIDTH, HEIGHT, fonts, random);
        try (OutputStream os = supplier.get()) {
            ImageIO.write(image, "JPEG", os);
            return captcha;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            graphics.dispose();
        }
    }

    /**
     * 校验验证码
     *
     * @param captchaCode      验证码
     * @param userInputCaptcha 用户输入的字符串
     * @return 是否成功
     */
    public boolean validate(String captchaCode, String userInputCaptcha) {
        return captchaDraw.validate(captchaCode, userInputCaptcha);
    }

    private static Graphics2D initGraphics(BufferedImage image) {
        // 获取图形上下文
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 图形抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return graphics;
    }

    private static Font[] loadFonts() {
        List<Font> fontList = new ArrayList<>();
        for (String fontName : FONT_NAMES) {
            String path = "fonts/" + fontName;
            fontList.add(loadFont(new ClassPathResource(path)));
        }
        return fontList.toArray(new Font[0]);
    }

    private static Font loadFont(ClassPathResource resource) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, resource.getInputStream());
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
