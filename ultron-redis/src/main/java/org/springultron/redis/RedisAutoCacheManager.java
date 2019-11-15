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
import java.util.Optional;

/**
 * Redis Cache扩展扩展cache name
 * 支持 # 号分隔 cache name 和 超时 ttl(单位秒)。
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
        System.err.println("createRedisCache:" + name);
        Optional.ofNullable(cacheConfig).ifPresent(config -> System.err.println(config.getTtl().getSeconds()));
        if (!StringUtils.isEmpty(name) && name.contains("#")) {
            String[] array = name.split("#");
            if (array.length > 1) {
                name = array[0];
                long cacheAge = 0;
                try {
                    cacheAge = Long.parseLong(array[1]);
                } catch (final NumberFormatException ignored) {
                }
                Duration ttl = Duration.ofSeconds(cacheAge);
                if (cacheConfig != null && cacheConfig.getTtl().compareTo(ttl) != 0) {
                    cacheConfig = cacheConfig.entryTtl(ttl);
                }
            }
        }
        if (cacheConfig == null) {
            System.err.println("---222:name:" + name);
        } else {
            System.err.println("---222:name:" + name + "---ttl:" + cacheConfig.getTtl().getSeconds());
        }
        return super.createRedisCache(name, cacheConfig);
    }
}