package org.springultron.core.constraints;

import org.springultron.core.utils.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * 手机号码校验规则
 *
 * @author brucewuu
 * @date 2019/9/2 14:19
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value).map(RegexUtils::matchPhone).orElse(Boolean.FALSE);
    }
}