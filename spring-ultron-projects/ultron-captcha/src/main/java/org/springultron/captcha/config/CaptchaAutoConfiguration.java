package org.springultron.captcha.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springultron.captcha.cache.CaptchaCache;
import org.springultron.captcha.cache.SpringCacheCaptchaCache;
import org.springultron.captcha.service.CaptchaService;
import org.springultron.captcha.service.impl.CaptchaServiceImpl;

/**
 * 验证码自动配置
 *
 * @author brucewuu
 * @date 2021/4/13 上午11:50
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(CaptchaRuntimeHintsRegistrar.class)
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CaptchaCache captchaCache(CaptchaProperties properties, CacheManager cacheManager) {
        return new SpringCacheCaptchaCache(properties, cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(CaptchaProperties properties, CaptchaCache captchaCache) {
        return new CaptchaServiceImpl(properties, captchaCache);
    }

}
