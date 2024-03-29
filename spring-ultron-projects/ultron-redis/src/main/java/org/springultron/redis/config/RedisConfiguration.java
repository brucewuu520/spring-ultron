package org.springultron.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springultron.core.jackson.UltronJavaTimeModule;

/**
 * Redis配置
 * <p>
 * 序列化策略：
 * 默认使用Jackson序列化Redis value，当没有依赖Jackson时使用jdk序列化
 * 用户可自定义注入redisSerializer 或 redisTemplate Bean来实现自定义配置
 * </p>
 *
 * @author brucewuu
 * @date 2019-05-31 14:26
 */
@AutoConfiguration(before = {RedisAutoConfiguration.class})
public class RedisConfiguration {

    /**
     * 自定义Redis value序列化方式
     *
     * @return RedisSerializer<Object>
     */
    @Bean
    @ConditionalOnClass({ObjectMapper.class})
    @ConditionalOnMissingBean(name = {"redisSerializer"})
    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 不反序列化为null的字段
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.findAndRegisterModules();
        // 配置java8日期序列化
        objectMapper.registerModule(new UltronJavaTimeModule());
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    /**
     * 配置自定义RedisTemplate
     *
     * @param redisConnectionFactory redis连接工厂
     * @param redisSerializer        value序列化方式
     * @return RedisTemplate<String, Object>
     */
    @Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectProvider<RedisSerializer<Object>> redisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // 使用StringRedisSerializer.UTF_8来序列化和反序列化redis的key值
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        redisSerializer.ifAvailable(serializer -> {
            template.setEnableDefaultSerializer(false);
            template.setDefaultSerializer(serializer);
            template.setValueSerializer(serializer);
            template.setHashValueSerializer(serializer);
        });
        return template;
    }
}