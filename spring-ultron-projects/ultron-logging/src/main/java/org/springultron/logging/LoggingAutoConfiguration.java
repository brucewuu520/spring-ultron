package org.springultron.logging;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springultron.logging.appender.ILoggingAppender;
import org.springultron.logging.listener.LogbackLoggerContextListener;
import org.springultron.logging.listener.LoggingStartedEventListener;
import org.springultron.logging.trace.TraceIdAutoConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * logging 日志配置
 *
 * @author brucewuu
 * @date 2021/4/9 上午9:53
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UltronLoggingProperties.class)
@Import({TraceIdAutoConfiguration.class, LoggingLogstashConfiguration.class})
class LoggingAutoConfiguration {

    @Bean
    LoggingStartedEventListener loggingStartedEventListener(UltronLoggingProperties properties) {
        return new LoggingStartedEventListener(properties);
    }

    @Bean
    LogbackLoggerContextListener logbackLoggerContextListener(ObjectProvider<List<ILoggingAppender>> appenderList) {
        return new LogbackLoggerContextListener(appenderList.getIfAvailable(Collections::emptyList));
    }

}
