package org.springultron.redis.config;

import tools.jackson.databind.DatabindContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * Redis Jackson Polymorphic Type Validator
 *
 * @author brucewuu
 * @date 2026/6/17 14:46
 */
public class RedisJacksonPolymorphicTypeValidator extends PolymorphicTypeValidator {

    public static final PolymorphicTypeValidator INSTANCE = new RedisJacksonPolymorphicTypeValidator();

    @Override
    public PolymorphicTypeValidator.Validity validateBaseType(DatabindContext context, JavaType baseType) {
        return PolymorphicTypeValidator.Validity.INDETERMINATE;
    }

    @Override
    public PolymorphicTypeValidator.Validity validateSubClassName(DatabindContext context, JavaType baseType, String subClassName) {
        return PolymorphicTypeValidator.Validity.ALLOWED;
    }

    @Override
    public PolymorphicTypeValidator.Validity validateSubType(DatabindContext context, JavaType baseType, JavaType subType) {
        return PolymorphicTypeValidator.Validity.ALLOWED;
    }
}
