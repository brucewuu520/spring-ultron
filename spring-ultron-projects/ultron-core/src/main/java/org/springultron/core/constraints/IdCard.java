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
 * 身份证号验证注解
 *
 * @author brucewuu
 * @date 2019/9/2 15:23
 */
@Documented
@Constraint(validatedBy = {IdCardValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface IdCard {
    String message() default "身份证号码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}