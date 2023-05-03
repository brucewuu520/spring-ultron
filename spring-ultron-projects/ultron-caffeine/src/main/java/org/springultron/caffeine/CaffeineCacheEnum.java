package org.springultron.caffeine;

/**
 * caffeine cache 的默认缓存空间
 *
 * @author brucewuu
 * @date 2021/4/18 上午10:06
 */
public enum CaffeineCacheEnum {
    /**
     * 缓存过期时间5分钟
     */
    FIVE_MINUTES("FIVE_MINUTES", 5 * 60),
    /**
     * 缓存过期时间15分钟
     */
    FIFTEEN_MINUTES("FIFTEEN_MINUTES", 15 * 60),

    /**
     * 缓存过期时间30分钟
     */
    THIRTY_MINUTES("THIRTY_MINUTES", 30 * 60),

    /**
     * 缓存过期时间2小时
     */
    TWO_HOURS("TWO_HOURS", 2 * 60 * 60),

    /**
     * 缓存过期时间7天
     */
    A_WEEK("A_WEEK", 7 * 24 * 60 * 60);

    /**
     * 缓存空间名称
     */
    private final String name;
    /**
     * 缓存过期时间（单位：秒）
     */
    private final int ttl;

    CaffeineCacheEnum(String name, int ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    public String getName() {
        return name;
    }

    public int getTtl() {
        return ttl;
    }
}
