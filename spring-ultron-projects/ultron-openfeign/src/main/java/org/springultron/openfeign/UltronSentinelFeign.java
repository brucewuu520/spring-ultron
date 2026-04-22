package org.springultron.openfeign;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClientFactory;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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

    private static final String FEIGN_LAZY_ATTR_RESOLUTION = "spring.cloud.openfeign.lazy-attributes-resolution";

    private UltronSentinelFeign() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Feign.Builder implements ApplicationContextAware {

        private Contract contract = new Contract.Default();

        private @Nullable ApplicationContext applicationContext;

        private @Nullable FeignClientFactory feignClientFactory;

        @Override
        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        @Override
        public Feign internalBuild() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @Override
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    ApplicationContext ctx = Builder.this.applicationContext;
                    FeignClientFactory factory = Builder.this.feignClientFactory;
                    if (ctx == null || factory == null) {
                        throw new IllegalStateException("ApplicationContext or FeignClientFactory not initialized");
                    }
                    GenericApplicationContext gctx = (GenericApplicationContext) ctx;
                    BeanDefinition def = gctx.getBeanDefinition(target.type().getName());
                    FeignClientFactoryBean feignClientFactoryBean;

                    // If you need the attributes to be resolved lazily, set the property value to true.
                    Boolean isLazyInit = ctx.getEnvironment().getProperty(FEIGN_LAZY_ATTR_RESOLUTION, Boolean.class, false);
                    if (isLazyInit) {
                        /*
                         * Due to the change of the initialization sequence,
                         * BeanFactory.getBean will cause a circular dependency. So
                         * FeignClientFactoryBean can only be obtained from BeanDefinition
                         */
                        feignClientFactoryBean = (FeignClientFactoryBean) def.getAttribute("feignClientsRegistrarFactoryBean");
                    } else {
                        feignClientFactoryBean = (FeignClientFactoryBean) ctx.getBean("&" + target.type().getName());
                    }
                    if (feignClientFactoryBean == null) {
                        throw new IllegalStateException("FeignClientFactoryBean not found for " + target.type().getName());
                    }
                    Class<?> fallback = feignClientFactoryBean.getFallback();
                    Class<?> fallbackFactory = feignClientFactoryBean.getFallbackFactory();
                    String beanName = feignClientFactoryBean.getContextId();
                    if (!StringUtils.hasText(beanName)) {
                        Object nameValue = getFieldValue(feignClientFactoryBean, "name");
                        beanName = nameValue != null ? (String) nameValue : "unknown";
                    }

                    Object fallbackInstance;
                    FallbackFactory<?> fallbackFactoryInstance;
                    // check fallback and fallbackFactory properties
                    if (void.class != fallback) {
                        fallbackInstance = getFromContext(beanName, "fallback", fallback, target.type());
                        return new UltronSentinelInvocationHandler(target, dispatch, new FallbackFactory.Default<>(fallbackInstance));
                    }
                    if (void.class != fallbackFactory) {
                        fallbackFactoryInstance = (FallbackFactory<?>) getFromContext(beanName, "fallbackFactory", fallbackFactory, FallbackFactory.class);
                        return new UltronSentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
                    }
                    return new UltronSentinelInvocationHandler(target, dispatch);
                }

                private Object getFromContext(String name, String type, Class<?> fallbackType, Class<?> targetType) {
                    FeignClientFactory factory = Builder.this.feignClientFactory;
                    if (factory == null) {
                        throw new IllegalStateException("FeignClientFactory not initialized");
                    }
                    Object fallbackInstance = factory.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(String.format("No %s instance of type %s found for feign client %s", type, fallbackType, name));
                    }
                    // when fallback is a FactoryBean, should determine the type of instance
                    if (fallbackInstance instanceof FactoryBean<?> factoryBean) {
                        try {
                            fallbackInstance = factoryBean.getObject();
                        }
                        catch (Exception e) {
                            throw new IllegalStateException(type + " create fail", e);
                        }
                        if (fallbackInstance == null) {
                            throw new IllegalStateException(type + " FactoryBean returned null");
                        }
                        fallbackType = fallbackInstance.getClass();
                    }

                    if (!targetType.isAssignableFrom(fallbackType)) {
                        throw new IllegalStateException(String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s", type, fallbackType, targetType, name));
                    } else {
                        return fallbackInstance;
                    }
                }
            });

            super.contract(new SentinelContractHolder(contract));
            return super.internalBuild();
        }

        private Object getFieldValue(Object instance, String fieldName) {
            Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
            if (field == null) {
                return null;
            }
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
            this.feignClientFactory = applicationContext.getBean(FeignClientFactory.class);
        }
    }
}
