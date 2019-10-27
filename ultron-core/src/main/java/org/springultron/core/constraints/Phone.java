package org.springultron.core.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号验证注解
 *
 * @author brucewuu
 * @date 2019/9/2 10:59
 */
@Documented
@Constraint(validatedBy = {PhoneValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Phone {
    String message() default "请输入有效的手机号码";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}