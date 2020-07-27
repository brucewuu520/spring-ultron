package org.springultron.cloud.servlet;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerResponse;
import org.springultron.core.result.ResultStatus;

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
        return (request, response, e) -> ServerResponse.status(ResultStatus.FLOW_LIMITING.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultStatus.FLOW_LIMITING.getMessage());
    }

}