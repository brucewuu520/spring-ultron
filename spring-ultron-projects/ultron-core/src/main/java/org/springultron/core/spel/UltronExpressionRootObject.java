package org.springultron.core.spel;

import java.lang.reflect.Method;

/**
 * ExpressionRootObject
 *
 * @author brucewuu
 * @date 2020/4/28 10:44
 */
public record UltronExpressionRootObject(Method method, Object[] args,
                                         Object target, Class<?> targetClass,
                                         Method targetMethod) {
}
