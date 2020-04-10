package org.springultron.boot.servlet.logger;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义API请求日志注解
 *
 * @author brucewuu
 * @date 2019-06-17 17:54
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface ApiLog {
    /**
     * 日志描述信息
     *
     * @return 日志描述信息
     */
    String description() default "request api";
}