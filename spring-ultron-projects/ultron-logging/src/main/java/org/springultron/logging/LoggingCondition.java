package org.springultron.logging;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

/**
 * Logging配置触发条件
 *
 * @author brucewuu
 * @date 2021/4/9 下午3:50
 */
class LoggingCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        Boolean logstashEnabled = environment.getProperty(UltronLoggingProperties.Logstash.PREFIX + ".enabled", Boolean.class, Boolean.FALSE);
        if (!logstashEnabled) {
            return ConditionOutcome.noMatch("logstash is not enabled.");
        }
        if (!hasLogstashDependency(context.getClassLoader())) {
            return ConditionOutcome.noMatch("logstash is enabled, please dependencies logstash-logback-encoder.");
        }
        return ConditionOutcome.match();
    }

    private static boolean hasLogstashDependency(ClassLoader classLoader) {
        return ClassUtils.isPresent("net.logstash.logback.encoder.LogstashEncoder", classLoader);
    }
}
