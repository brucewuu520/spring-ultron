package org.springultron.captcha.service.impl;

import org.springframework.util.StringUtils;
import org.springultron.captcha.Captcha;
import org.springultron.captcha.cache.CaptchaCache;
import org.springultron.captcha.config.CaptchaProperties;
import org.springultron.captcha.service.CaptchaService;

import java.io.OutputStream;

/**
 * 验证码服务
 *
 * @author brucewuu
 * @date 2021/4/13 下午12:06
 */
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaProperties properties;
    private final CaptchaCache captchaCache;
    private final Captcha captcha;

    public CaptchaServiceImpl(CaptchaProperties properties, CaptchaCache captchaCache) {
        this.properties = properties;
        this.captchaCache = captchaCache;
        this.captcha = new Captcha(properties.getCaptchaType());
    }

    @Override
    public void generate(String cacheKey, OutputStream outputStream) {
        String captchaCode = captcha.generate(() -> outputStream);
        captchaCache.put(properties.getCacheName(), cacheKey, captchaCode);
    }

    @Override
    public boolean validate(String cacheKey, String userInputCaptcha) {
        String captchaCode = captchaCache.getAndRemove(properties.getCacheName(), cacheKey);
        if (!StringUtils.hasText(captchaCode)) {
            return false;
        }
        return captcha.validate(captchaCode, userInputCaptcha);
    }
}
