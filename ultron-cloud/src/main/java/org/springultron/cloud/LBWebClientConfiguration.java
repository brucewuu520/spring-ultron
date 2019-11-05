package org.springultron.cloud;

import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;

/**
 * 负载均衡的 WebClient
 *
 * @author brucewuu
 * @date 2019-08-23 14:36
 */
@Configuration(proxyBeanMethods = false)
public class LBWebClientConfiguration {

    @Resource
    private LoadBalancerExchangeFilterFunction lbFunction;

    /**
     * 负载均衡的 WebClient
     *
     * @return WebClient
     */
    @Bean("lbWebClient")
    public WebClient lbWebClient() {
        return WebClient.builder()
                .filter(lbFunction)
                .build();
    }
}