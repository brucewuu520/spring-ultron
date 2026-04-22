package org.springultron.openfeign;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import feign.Feign;
import feign.Retryer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Sentinel Feign Config
 *
 * @author brucewuu
 * @date 2020/10/3 下午6:00
 */
@AutoConfiguration(before = {SentinelFeignAutoConfiguration.class})
public class UltronSentinelFeignAutoConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"feign.sentinel.enabled"})
    public Feign.Builder feignSentinelBuilder(Retryer retryer) {
        return UltronSentinelFeign.builder().retryer(retryer);
    }
}
