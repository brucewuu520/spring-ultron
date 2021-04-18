package org.springultron.wechat;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springultron.core.utils.StringUtils;
import org.springultron.redis.RedisClient;
import org.springultron.wechat.props.WechatProperties;
import org.springultron.wechat.service.WxApiService;
import org.springultron.wechat.service.WxaApiService;
import org.springultron.wechat.service.impl.WxApiServiceImpl;
import org.springultron.wechat.service.impl.WxaApiServiceImpl;

import java.lang.annotation.*;
import java.util.Map;
import java.util.Objects;

/**
 * 微信配置
 *
 * @author brucewuu
 * @date 2021/3/30 上午10:00
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(RedisClient.class)
@EnableConfigurationProperties(WechatProperties.class)
public class WechatAutoConfiguration {

    /**
     * 公众号API
     */
    @Bean
    @ConditionOnConf(value = "WX")
    public WxApiService wxApiService(WechatProperties properties, RedisClient redisClient) {
        return new WxApiServiceImpl(properties, redisClient);
    }

    /**
     * 小程序API
     */
    @Bean
    @ConditionOnConf(value = "WXA")
    public WxaApiService wxaApiService(WechatProperties properties, RedisClient redisClient) {
        return new WxaApiServiceImpl(properties, redisClient);
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Conditional(ApiCondition.class)
    private @interface ConditionOnConf {
        /**
         * value
         *
         * @return value
         */
        String value();
    }

    private static class ApiCondition extends SpringBootCondition {

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionOnConf.class.getName());
            String value = Objects.toString(Objects.requireNonNull(attributes).get("value"));
            Environment environment = context.getEnvironment();
            if ("WX".equals(value)) {
                String appId = environment.getProperty("wechat.wx-conf.app-id");
                if (StringUtils.isNotBlank(appId)) {
                    return ConditionOutcome.match();
                }
            } else if ("WXA".equals(value)) {
                String appId = environment.getProperty("wechat.wxa-conf.app-id");
                if (StringUtils.isNotBlank(appId)) {
                    return ConditionOutcome.match();
                }
            }
            return ConditionOutcome.noMatch("not match.");
        }
    }
}
