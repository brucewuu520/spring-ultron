package org.springultron.redis.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springultron.redis.RedisAutoCacheManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Redis Cache 自动化配置，扩展cache name
 * 支持 # 号分隔 cache name 和 超时 ttl(单位秒)。
 * <p>
 * 示例：@CachePut(value = "user#300", key = "#id")
 *
 * @author brucewuu
 * @date 2019/11/10 17:34
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({RedisAutoConfiguration.class})
@EnableConfigurationProperties({CacheProperties.class})
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {

    /**
     * 自定义的缓存key的生成策略
     * 此方法将会根据类名+方法名+所有参数的值生成唯一的一个key,即使@Cacheable中的value属性一样，key也会不一样。
     * 若想使用这个key只需要将注解上keyGenerator的值设置为keyGenerator即可
     *
     * @return 自定义策略生成的key
     */
    @Bean
    @ConditionalOnMissingBean(name = {"keyGenerator"})
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 配置缓存管理器
     *
     * @param redisConnectionFactory redis连接工厂
     * @return CacheManager
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Primary
    @Bean
    public RedisCacheManager cacheManager(CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, RedisConnectionFactory redisConnectionFactory, @Autowired(required = false) RedisSerializer<Object> redisSerializer) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration cacheConfiguration = this.determineConfiguration(cacheProperties, redisCacheConfiguration, redisSerializer);
        final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheNames.forEach(cacheName -> initialCaches.put(cacheName, cacheConfiguration));
        }
        RedisAutoCacheManager redisCacheManager = new RedisAutoCacheManager(redisCacheWriter, cacheConfiguration, initialCaches, true);
        redisCacheManager.setTransactionAware(false);
        return cacheManagerCustomizers.customize(redisCacheManager);
    }

    private RedisCacheConfiguration determineConfiguration(CacheProperties cacheProperties, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, @Nullable RedisSerializer<Object> redisSerializer) {
        return redisCacheConfiguration.getIfAvailable(() -> this.createConfiguration(cacheProperties, redisSerializer));
    }

    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, @Nullable RedisSerializer<Object> redisSerializer) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        if (redisSerializer != null) {
            config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        }
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }
}