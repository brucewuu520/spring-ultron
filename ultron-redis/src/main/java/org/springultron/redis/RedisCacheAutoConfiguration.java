package org.springultron.redis;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author brucewuu
 * @date 2019/11/22 15:40
 */
@Configuration
@ConditionalOnBean({CacheAspectSupport.class})
@AutoConfigureBefore({CacheAutoConfiguration.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
@EnableConfigurationProperties({CacheProperties.class})
public class RedisCacheAutoConfiguration {
    /**
     * 配置缓存管理器(替换默认的 RedisCacheManager)
     * 需手动配置 @EnableCaching开启缓存
     *
     * @param redisConnectionFactory redis连接工厂
     * @return CacheManager
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public RedisCacheManager cacheManager(CacheProperties cacheProperties, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, ObjectProvider<RedisSerializer<Object>> redisSerializer, RedisConnectionFactory redisConnectionFactory) {
        final RedisCacheConfiguration defaultCacheConfiguration = this.determineConfiguration(cacheProperties, redisCacheConfiguration, redisSerializer);
        /*** 自定义缓存空间配置不同的过期时间 ***/
        CacheEnum[] cacheEnums = CacheEnum.values();
        Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheEnums.length);
        for (CacheEnum cacheEnum : cacheEnums) {
            cacheConfigMap.put(cacheEnum.getCacheName(), defaultCacheConfiguration.entryTtl(Duration.ofSeconds(cacheEnum.getExpireTime())));
        }
        // 使用自定义的缓存配置初始化cacheManager
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(defaultCacheConfiguration) // 默认缓存配置策略
                .withInitialCacheConfigurations(cacheConfigMap)
                .build();
    }

    private RedisCacheConfiguration determineConfiguration(CacheProperties cacheProperties, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, ObjectProvider<RedisSerializer<Object>> redisSerializer) {
        return redisCacheConfiguration.getIfAvailable(() -> this.createConfiguration(cacheProperties, redisSerializer.getIfAvailable()));
    }

    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, RedisSerializer<Object> redisSerializer) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        if (redisSerializer != null) {
            config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        }

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }
}