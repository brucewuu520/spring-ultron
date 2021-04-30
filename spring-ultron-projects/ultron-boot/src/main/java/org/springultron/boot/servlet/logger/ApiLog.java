package org.springultron.boot.servlet.logger;

import org.springultron.boot.enums.LogLevel;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义API请求日志注解
 *
 * @author brucewuu
 * @date 2019-06-17 17:54
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface ApiLog {
    /**
     * 日志描述信息
     *
     * @return 日志描述信息
     */
    String description() default "request api";

    /**
     * 日志打印等级
     */
    LogLevel level() default LogLevel.BODY;
}