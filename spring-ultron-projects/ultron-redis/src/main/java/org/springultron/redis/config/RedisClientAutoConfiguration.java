package org.springultron.redis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
    public RedisClient redisClient() {
        return new RedisClient();
    }

    /**
     * 反应式Redis操作客户端
     */
    @Bean
    @ConditionalOnBean(name = {"reactiveRedisTemplate", "reactiveStringRedisTemplate"})
    public ReactiveRedisClient reactiveRedisClient() {
        return new ReactiveRedisClient();
    }

}
