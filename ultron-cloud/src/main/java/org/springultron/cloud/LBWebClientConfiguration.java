package org.springultron.cloud;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 负载均衡的 WebClient
 *
 * @author brucewuu
 * @date 2019-08-23 14:36
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebClient.class})
@ConditionalOnBean({ReactorLoadBalancerExchangeFilterFunction.class})
public class LBWebClientConfiguration {
    /**
     * 负载均衡的 WebClient
     *
     * @return WebClient
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("lbWebClient")
    public WebClient lbWebClient(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        return WebClient.builder()
                .filter(lbFunction)
                .build();
    }
}