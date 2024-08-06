package org.springultron.cloud.servlet;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Sentinel 配置类
 *
 * @author brucewuu
 * @date 2019-08-11 11:22
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SentinelConfiguration {

    /**
     * 限流、熔断异常处理
     */
    @Bean
    public BlockExceptionHandler blockExceptionHandler() {
        return (request, response, msg, e) -> {
            // Return 429 (Too Many Requests) by default.
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        };
    }
}