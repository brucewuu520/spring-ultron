package org.springultron.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.KotlinDetector;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springultron.core.jackson.Jackson;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

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
@AutoConfiguration(before = {DataRedisAutoConfiguration.class})
public class RedisConfiguration {

    /**
     * 自定义Redis value序列化方式
     *
     * @return RedisSerializer<Object>
     */
    @Bean
    @ConditionalOnClass({JsonMapper.class})
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer<Object> redisSerializer() {
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        JsonMapper.Builder jsonMapperBuilder = Jackson.getInstance().rebuild();
        // 配置宽松的类型验证器
        PolymorphicTypeValidator polymorphicTypeValidator = RedisJacksonPolymorphicTypeValidator.INSTANCE;
        if (KotlinDetector.isKotlinPresent()) {
            jsonMapperBuilder.activateDefaultTyping(polymorphicTypeValidator, DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.PROPERTY);
        } else {
            jsonMapperBuilder.activateDefaultTyping(polymorphicTypeValidator, DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
        return new GenericJacksonJsonRedisSerializer(jsonMapperBuilder.build());
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
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
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