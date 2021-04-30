package org.springultron.captcha.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springultron.captcha.CaptchaType;

/**
 * 图形验证码配置项
 *
 * @author brucewuu
 * @date 2021/4/13 下午2:15
 */
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {
    /**
     * 验证码类型，默认：随机数
     */
    private CaptchaType captchaType = CaptchaType.RANDOM;

    /**
     * 验证码cache名，默认：captcha:cache#5m，配合 ultron-redis 5分钟缓存
     */
    private String cacheName = "captcha:cache#5m";

    public CaptchaType getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
}
