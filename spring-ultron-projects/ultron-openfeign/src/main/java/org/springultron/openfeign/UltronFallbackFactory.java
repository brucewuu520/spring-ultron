package org.springultron.openfeign;

import feign.Target;
import feign.hystrix.FallbackFactory;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 默认 Fallback
 *
 * @author brucewuu
 * @date 2020/7/25 21:22
 */
public class UltronFallbackFactory<T> implements FallbackFactory<T> {

    private final Target target;

    public UltronFallbackFactory(Target target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new UltronFeignFallback<>(targetType, targetName, cause));
        return (T) enhancer.create();
    }
}
