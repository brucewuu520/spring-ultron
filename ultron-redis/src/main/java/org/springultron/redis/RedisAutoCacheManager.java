package org.springultron.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;

/**
 * Redis Cache 扩展cache name自动化配置
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
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        if (!StringUtils.isEmpty(name) && name.contains("#")) {
            String[] array = name.split("#");
            if (array.length > 1) {
                name = array[0];
                long cacheAge = -1;
                try {
                    cacheAge = Long.parseLong(array[1]);
                } catch (final NumberFormatException ignored) {
                }
                if (null != cacheConfig) {
                    cacheConfig.entryTtl(Duration.ofSeconds(cacheAge));
                }
            }
        }
        return super.createRedisCache(name, cacheConfig);
    }
}
