package org.springultron.logging.trace;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 请求链路日志追踪自动化配置
 *
 * @author brucewuu
 * @date 2021/4/18 下午6:37
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class TraceIdAutoConfiguration {

    @Bean
    TraceIdRequestLoggingFilter traceIdRequestLoggingFilter() {
        return new TraceIdRequestLoggingFilter();
    }

}
