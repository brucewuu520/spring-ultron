package org.springultron.redis.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.core.publisher.Flux;

/**
 * 反应式Redis配置
 * <p>
 * 序列化策略：
 * 默认使用Jackson序列化Redis value，当没有依赖Jackson时使用jdk序列化
 * 用户可自定义注入redisSerializer 或 redisTemplate Bean来实现自定义配置
 * </p>
 *
 * @author brucewuu
 * @date 2019/11/26 18:26
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({RedisReactiveAutoConfiguration.class})
@ConditionalOnBean({ReactiveRedisConnectionFactory.class})
@ConditionalOnClass({ReactiveRedisTemplate.class, Flux.class})
public class ReactiveRedisConfiguration {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean(name = {"reactiveRedisTemplate"})
    @ConditionalOnBean({ReactiveRedisConnectionFactory.class})
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ObjectProvider<RedisSerializer<Object>> redisSerializer, ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        RedisSerializer<Object> valueSerializer = redisSerializer.getIfAvailable(JdkSerializationRedisSerializer::new);
        RedisSerializationContext.SerializationPair<String> keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
        RedisSerializationContext.SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext.newSerializationContext();
        builder.key(keySerializationPair).value(valueSerializationPair)
                .hashKey(keySerializationPair).hashValue(valueSerializationPair);
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, builder.build());
    }
}