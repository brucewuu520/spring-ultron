package org.springultron.cloud;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 负载均衡的WebClient(反应式、懒加载)
 * 使用方法：
 * <p>
 * 1、添加依赖：spring-cloud-starter-loadbalancer
 * 2、然后排除spring-cloud-netflix-ribbon依赖或者配置spring.cloud.loadbalancer.ribbon.enabled=false
 * netflix-ribbon已经停止维护，建议排除此依赖并依赖spring-cloud-starter-loadbalancer
 * </p>
 *
 * @author brucewuu
 * @date 2019-08-23 14:36
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({WebClient.Builder.class, ReactorLoadBalancerExchangeFilterFunction.class})
public class LbWebClientConfiguration {
    /**
     * 负载均衡的 WebClient
     *
     * @return lbWebClient
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("lbWebClient")
    @ConditionalOnMissingBean(name = {"lbWebClient"})
    public WebClient lbWebClientBuilder(WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        return webClientBuilder.filter(lbFunction).build();
    }
}