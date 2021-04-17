package org.springultron.captcha.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springultron.captcha.config.CaptchaProperties;

import java.util.Objects;

/**
 * 基于Spring Cache的验证码缓存实现
 *
 * @author brucewuu
 * @date 2021/4/13 下午1:23
 */
public class SpringCacheCaptchaCache implements CaptchaCache, InitializingBean {
    private final CaptchaProperties properties;
    private final CacheManager cacheManager;
    private Cache captchaCache;

    public SpringCacheCaptchaCache(CaptchaProperties properties, CacheManager cacheManager) {
        this.properties = properties;
        this.cacheManager = cacheManager;
    }

    @Override
    public void put(String cacheName, String cacheKey, String value) {
        captchaCache.put(cacheKey, value);
    }

    @Override
    public String getAndRemove(String cacheName, String cacheKey) {
        final String value = captchaCache.get(cacheKey, String.class);
        if (value != null) {
            captchaCache.evict(cacheKey);
        }
        return value;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final String cacheName = properties.getCacheName();
        Cache cache = cacheManager.getCache(cacheName);
        this.captchaCache = Objects.requireNonNull(cache, "captcha spring cache name " + cacheName + " is null.");
    }
}
