package org.springultron.redis;

/**
 * @author brucewuu
 * @date 2019/10/27 12:05
 */
public enum CacheEnum {
    /**
     * 验证码缓存 5分钟ttl
     */
    VERIFY_CODE("verifyCode", 300),
    /**
     * 数据库查询结果缓存 3小时ttl
     */
    DB_RESULT("dbResult", 3 * 60 * 60),
    /**
     * token缓存 7天ttl
     */
    TOKEN_CACHE("tokenCache", 7 * 24 * 60 * 60);

    /**
     * 缓存名称
     */
    private final String cacheName;
    /**
     * 缓存过期时间（单位:秒）
     */
    private final long expireTime;

    CacheEnum(String cacheName, long expireTime) {
        this.cacheName = cacheName;
        this.expireTime = expireTime;
    }

    public String getCacheName() {
        return cacheName;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
