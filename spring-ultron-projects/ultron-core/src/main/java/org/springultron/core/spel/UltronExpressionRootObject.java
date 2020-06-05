package org.springultron.core.spel;

import java.lang.reflect.Method;

/**
 * ExpressionRootObject
 *
 * @author brucewuu
 * @date 2020/4/28 10:44
 */
public class UltronExpressionRootObject {
    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;

    private final Method targetMethod;

    public UltronExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod) {
        this.method = method;
        this.args = args;
        this.target = target;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }
}
