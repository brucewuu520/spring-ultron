package org.springultron.cloud;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 负载均衡的WebClient(反应式)
 * <p>
 * 需要排除spring-cloud-netflix-ribbon依赖或者配置spring.cloud.loadbalancer.ribbon.enabled=false
 * netflix-ribbon已经停止维护，建议排除此依赖并依赖spring-cloud-starter-loadbalancer
 * </p>
 *
 * @author brucewuu
 * @date 2019-08-23 14:36
 */
@Configuration
@ConditionalOnBean({ReactorLoadBalancerExchangeFilterFunction.class})
public class LbWebClientConfiguration {

    private final WebClient.Builder webClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public LbWebClientConfiguration(WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.webClientBuilder = webClientBuilder;
        this.lbFunction = lbFunction;
    }

    /**
     * 负载均衡的 WebClient
     *
     * @return WebClient
     */
    @Lazy
    @Bean("lbWebClientBuilder")
    @Scope("prototype")
    public WebClient.Builder lbWebClientBuilder() {
        return webClientBuilder.clone().filter(lbFunction);
    }
}