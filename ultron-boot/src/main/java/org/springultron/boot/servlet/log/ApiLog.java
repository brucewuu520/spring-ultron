package org.springultron.boot.servlet.log;

import java.lang.annotation.*;

/**
 * 自定义API请求日志注解
 *
 * @Auther: brucewuu
 * @Date: 2019-06-17 17:54
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {

    /**
     * 日志描述信息
     *
     * @return 日志描述信息
     */
    String description() default "request api";
}
