package org.springultron.logging.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 请求链路日志追踪自动化配置
 *
 * @author brucewuu
 * @date 2021/4/17 下午5:16
 */
@Configuration(proxyBeanMethods = false)
class TraceIdAutoConfiguration {

    @Bean
    TraceIdRequestLoggingFilter traceIdRequestLoggingFilter() {
        return new TraceIdRequestLoggingFilter();
    }
}
