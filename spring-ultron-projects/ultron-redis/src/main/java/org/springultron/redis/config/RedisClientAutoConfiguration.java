package org.springultron.redis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springultron.redis.ReactiveRedisClient;
import org.springultron.redis.RedisClient;

/**
 * Redis操作客户端自动化配置(懒加载)
 *
 * @author brucewuu
 * @date 2019/11/26 18:14
 */
@Lazy
@Configuration(proxyBeanMethods = false)
public class RedisClientAutoConfiguration {

    /**
     * 阻塞式Redis操作客户端
     */
    @Bean
    public RedisClient redisClient(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate) {
        return new RedisClient(stringRedisTemplate, redisTemplate);
    }

    /**
     * 反应式Redis操作客户端
     */
    @Bean
    @ConditionalOnBean(name = {"reactiveStringRedisTemplate", "reactiveRedisTemplate"})
    public ReactiveRedisClient reactiveRedisClient(ReactiveStringRedisTemplate reactiveStringRedisTemplate, ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        return new ReactiveRedisClient(reactiveStringRedisTemplate, reactiveRedisTemplate);
    }

}
