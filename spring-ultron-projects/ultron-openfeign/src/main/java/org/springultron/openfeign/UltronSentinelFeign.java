package org.springultron.openfeign;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 支持自动降级注入 重写 {@link com.alibaba.cloud.sentinel.feign.SentinelFeign}
 *
 * @author brucewuu
 * @date 2020/10/3 上午10:24
 */
public final class UltronSentinelFeign {

    private UltronSentinelFeign() {
    }

    public static UltronSentinelFeign.Builder builder() {
        return new UltronSentinelFeign.Builder();
    }

    public static final class Builder extends feign.Feign.Builder implements ApplicationContextAware {

        private Contract contract = new Contract.Default();
        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        public Builder() {
        }

        @Override
        public feign.Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UltronSentinelFeign.Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        @Override
        public Feign build() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @Override
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    // using reflect get fallback and fallbackFactory properties from
                    // FeignClientFactoryBean because FeignClientFactoryBean is a package
                    // level class, we can not use it in our package
                    Object feignClientFactoryBean = UltronSentinelFeign.Builder.this.applicationContext.getBean("&" + target.type().getName());
                    Class<?> fallback = (Class<?>) UltronSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "fallback");
                    Class<?> fallbackFactory = (Class<?>) UltronSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "fallbackFactory");
                    String beanName = (String) UltronSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "contextId");
                    if (!StringUtils.hasText(beanName)) {
                        beanName = (String) UltronSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "name");
                    }

                    if (Void.TYPE != fallback) {
                        Object fallbackInstance = this.getFromContext(beanName, "fallback", fallback, target.type());
                        return new UltronSentinelInvocationHandler(target, dispatch, new FallbackFactory.Default<>(fallbackInstance));
                    } else if (Void.TYPE != fallbackFactory) {
                        FallbackFactory<?> fallbackFactoryInstance = (FallbackFactory<?>) this.getFromContext(beanName, "fallbackFactory", fallbackFactory, FallbackFactory.class);
                        return new UltronSentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
                    } else {
                        return new UltronSentinelInvocationHandler(target, dispatch);
                    }
                }

                private Object getFromContext(String name, String type, Class<?> fallbackType, Class<?> targetType) {
                    Object fallbackInstance = UltronSentinelFeign.Builder.this.feignContext.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(String.format("No %s instance of type %s found for feign client %s", type, fallbackType, name));
                    } else if (!targetType.isAssignableFrom(fallbackType)) {
                        throw new IllegalStateException(String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s", type, fallbackType, targetType, name));
                    } else {
                        return fallbackInstance;
                    }
                }
            });
            super.contract(new SentinelContractHolder(this.contract));
            return super.build();
        }

        private Object getFieldValue(Object instance, String fieldName) {
            Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
            assert field != null;
            field.setAccessible(true);
            try {
                return field.get(instance);
            } catch (IllegalAccessException e) {
                // ignore
            }
            return null;
        }

        @Override
        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            this.feignContext = this.applicationContext.getBean(FeignContext.class);
        }
    }
}
