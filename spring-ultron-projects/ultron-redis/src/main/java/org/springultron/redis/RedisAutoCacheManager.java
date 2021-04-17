package org.springultron.redis;

import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springultron.core.utils.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Redis Cache扩展扩展cache name
 * 支持 # 号分隔 cache name 和 超时 ttl(默认单位秒)。
 *
 * @author brucewuu
 * @date 2019/11/10 18:05
 */
public class RedisAutoCacheManager extends RedisCacheManager {

    public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    @NonNull
    @Override
    protected RedisCache createRedisCache(@NonNull String name, @Nullable RedisCacheConfiguration cacheConfig) {
        if (!StringUtils.isEmpty(name) && name.contains("#")) {
            String[] array = name.split("#");
            if (array.length > 1) {
                name = array[0].trim();
                // 转换时间，支持时间单位例如：300ms，默认单位秒
                Duration ttl = DurationStyle.detectAndParse(array[1].trim(), ChronoUnit.SECONDS);
                if (cacheConfig != null && cacheConfig.getTtl().compareTo(ttl) != 0) {
                    cacheConfig = cacheConfig.entryTtl(ttl);
                }
            }
        }
        return super.createRedisCache(name, cacheConfig);
    }
}