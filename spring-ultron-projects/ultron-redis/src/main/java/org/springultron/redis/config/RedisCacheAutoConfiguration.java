package org.springultron.redis.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.boot.cache.autoconfigure.CacheManagerCustomizer;
import org.springframework.boot.cache.autoconfigure.CacheManagerCustomizers;
import org.springframework.boot.cache.autoconfigure.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springultron.core.pool.StringPool;
import org.springultron.redis.RedisAutoCacheManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis Cache 自动化配置替换系统默认的cacheManager
 * 扩展cache name 支持 # 号分隔 cache name 和 超时 ttl(单位秒)。
 *
 * <p>
 * 示例：@CachePut(value = "user#300", key = "#id")
 * </p>
 *
 * <p>
 * 需手动开启@EnableCaching注解
 * </p>
 * {@link CacheAutoConfiguration}
 *
 * @author brucewuu
 * @date 2019/11/10 17:34
 */
@AutoConfiguration(before = {CacheAutoConfiguration.class}, after = {DataRedisAutoConfiguration.class})
@ConditionalOnBean({CacheAspectSupport.class})
@EnableConfigurationProperties({CacheProperties.class})
class RedisCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().toList());
    }

    /**
     * 配置缓存管理器，替换系统默认的cacheManager
     * {@link org.springframework.boot.cache.autoconfigure.RedisCacheConfiguration}
     */
    @Primary
    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, ObjectProvider<RedisSerializer<Object>> redisSerializer) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration cacheConfiguration = this.determineConfiguration(cacheProperties, redisCacheConfiguration, redisSerializer);
        List<String> cacheNames = cacheProperties.getCacheNames();
        final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>(cacheNames.size());
        if (!cacheNames.isEmpty()) {
            cacheNames.forEach(cacheName -> initialCaches.put(cacheName, cacheConfiguration));
        }

        CacheStatisticsCollector statisticsCollector = CacheStatisticsCollector.none();
        if (cacheProperties.getRedis().isEnableStatistics()) {
            statisticsCollector = CacheStatisticsCollector.create();
        }
        if (!statisticsCollector.equals(CacheStatisticsCollector.none())) {
            redisCacheWriter = redisCacheWriter.withStatisticsCollector(statisticsCollector);
        }

        RedisAutoCacheManager redisCacheManager = new RedisAutoCacheManager(redisCacheWriter, cacheConfiguration, true, initialCaches);
        redisCacheManager.setTransactionAware(false);
        return cacheManagerCustomizers.customize(redisCacheManager);
    }

    private RedisCacheConfiguration determineConfiguration(CacheProperties cacheProperties, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, ObjectProvider<RedisSerializer<Object>> redisSerializer) {
        return redisCacheConfiguration.getIfAvailable(() -> this.createConfiguration(cacheProperties, redisSerializer.getIfAvailable()));
    }

    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, @Nullable RedisSerializer<Object> redisSerializer) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        // 设置默认缓存key分割符号为 “:”，如果已经带 “:” 则不设置。
        config = config.computePrefixWith(name -> name.endsWith(StringPool.COLON) ? name : name + StringPool.COLON);

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
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }
}