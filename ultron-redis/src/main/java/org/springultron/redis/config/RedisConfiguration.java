package org.springultron.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springultron.core.jackson.UltronJavaTimeModule;

/**
 * Redis配置
 * 配置序列化方式以及缓存管理器 @EnableCaching 可开启方法注解缓存
 *
 * @author brucewuu
 * @date 2019-05-31 14:26
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({RedisAutoConfiguration.class})
public class RedisConfiguration {

    @Bean
    @ConditionalOnClass({ObjectMapper.class})
    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 不反序列化为null的字段
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时去掉多余的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许单引号（非标准）
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略JSON字符串中不识别的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.findAndRegisterModules();
        // java8日期格式化
        objectMapper.registerModule(new UltronJavaTimeModule());
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

//    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        // 反序列化时去掉多余的字段
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        return jackson2JsonRedisSerializer;
//    }

    /**
     * 配置自定义redisTemplate
     *
     * @param redisConnectionFactory redis连接工厂
     * @return RedisTemplate
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // 禁用defaultSerializer
        template.setEnableDefaultSerializer(false);
        // 使用StringRedisSerializer.UTF_8来序列化和反序列化redis的key值
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(redisSerializer);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(redisSerializer);
        return template;
    }

//    @Primary
//    @Bean
//    public RedisCacheManager cacheManager(CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers, RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
//        CacheEnum[] cacheEnums = CacheEnum.values();
//        Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheEnums.length);
//        for (CacheEnum cacheEnum : cacheEnums) {
//            cacheConfigMap.put(cacheEnum.getCacheName(), redisConfig(cacheEnum.getExpireTime(), redisSerializer));
//        }
//        // 使用自定义的缓存配置初始化cacheManager
//        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
//                .cacheDefaults(redisConfig(600, redisSerializer)) // 默认策略，未配置的 key 会使用这个
//                .withInitialCacheConfigurations(cacheConfigMap)
//                .build();
//    }

//    private RedisCacheConfiguration redisConfig(long seconds, RedisSerializer<Object> redisSerializer) {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(seconds)) // 设置缓存过期时间，使用Duration设置
//                .disableCachingNullValues() // 不缓存空值
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
//    }
}