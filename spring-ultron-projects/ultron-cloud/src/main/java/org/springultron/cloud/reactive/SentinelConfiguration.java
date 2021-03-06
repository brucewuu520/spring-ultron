package org.springultron.cloud.reactive;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Sentinel 配置类
 *
 * @author brucewuu
 * @date 2019-08-11 11:22
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class SentinelConfiguration {

    /**
     * 限流、熔断异常处理
     */
    @Bean
    public BlockRequestHandler blockRequestHandler() {
        // Return 429 (Too Many Requests) by default.
        return (exchange, throwable) -> ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()));
    }
}