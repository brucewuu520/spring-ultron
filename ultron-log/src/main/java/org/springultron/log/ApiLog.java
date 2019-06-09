package org.springultron.log;

import java.lang.annotation.*;

/**
 * 自定义API日志注解
 *
 * @Auther: brucewuu
 * @Date: 2019-06-08 11:17
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
    String description() default "";
}
