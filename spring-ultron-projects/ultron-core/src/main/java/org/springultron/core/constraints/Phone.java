package org.springultron.core.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
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
    String message() default "手机号码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}