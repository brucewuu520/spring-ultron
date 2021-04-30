package org.springultron.captcha;

import org.springultron.captcha.draw.CaptchaDraw;
import org.springultron.captcha.draw.MathCaptchaDraw;
import org.springultron.captcha.draw.RandomCaptchaDraw;

/**
 * 验证码类型枚举
 *
 * @author brucewuu
 * @date 2021/4/13 上午11:50
 */
public enum CaptchaType {
    /**
     * 随机字符串
     */
    RANDOM(new RandomCaptchaDraw()),

    /**
     * 算术
     */
    MATH(new MathCaptchaDraw());

    private final CaptchaDraw captchaDraw;

    CaptchaType(CaptchaDraw captchaDraw) {
        this.captchaDraw = captchaDraw;
    }

    public CaptchaDraw getCaptchaDraw() {
        return captchaDraw;
    }
}
