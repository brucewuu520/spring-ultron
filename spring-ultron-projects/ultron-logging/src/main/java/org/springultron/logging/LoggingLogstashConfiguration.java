package org.springultron.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springultron.logging.appender.LoggingLogstashAppender;

/**
 * Logstash 配置
 *
 * @author brucewuu
 * @date 2021/4/9 下午4:08
 */
@Configuration(proxyBeanMethods = false)
@Conditional(LoggingCondition.class)
class LoggingLogstashConfiguration {

    LoggingLogstashConfiguration() {
    }

    @Bean
    LoggingLogstashAppender loggingLogstashAppender(UltronLoggingProperties properties, Environment environment) {
        return new LoggingLogstashAppender(properties, environment);
    }

}
