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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Feign.Builder implements ApplicationContextAware {
        private Contract contract = new Contract.Default();
        private ApplicationContext applicationContext;
        private FeignClientFactory feignClientFactory;

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
        public Feign build() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @Override
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    GenericApplicationContext gctx = (GenericApplicationContext) UltronSentinelFeign.Builder.this.applicationContext;
                    BeanDefinition def = gctx.getBeanDefinition(target.type().getName());
                    Boolean isLazyInit = UltronSentinelFeign.Builder.this.applicationContext.getEnvironment().getProperty("spring.cloud.openfeign.lazy-attributes-resolution", Boolean.class, false);
                    FeignClientFactoryBean feignClientFactoryBean;
                    if (isLazyInit) {
                        feignClientFactoryBean = (FeignClientFactoryBean)def.getAttribute("feignClientsRegistrarFactoryBean");
                    } else {
                        feignClientFactoryBean = (FeignClientFactoryBean) UltronSentinelFeign.Builder.this.applicationContext.getBean("&" + target.type().getName());
                    }

                    assert feignClientFactoryBean != null;
                    Class<?> fallback = feignClientFactoryBean.getFallback();
                    Class<?> fallbackFactory = feignClientFactoryBean.getFallbackFactory();
                    String beanName = feignClientFactoryBean.getContextId();
                    if (!StringUtils.hasText(beanName)) {
                        beanName = (String) UltronSentinelFeign.Builder.this.getFieldValue(feignClientFactoryBean, "name");
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
                    Object fallbackInstance = UltronSentinelFeign.Builder.this.feignClientFactory.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(String.format("No %s instance of type %s found for feign client %s", type, fallbackType, name));
                    } else {
                        if (fallbackInstance instanceof FactoryBean<?> factoryBean) {
                            try {
                                fallbackInstance = factoryBean.getObject();
                            } catch (Exception var8) {
                                throw new IllegalStateException(type + " create fail", var8);
                            }
                            assert fallbackInstance != null;
                            fallbackType = fallbackInstance.getClass();
                        }

                        if (!targetType.isAssignableFrom(fallbackType)) {
                            throw new IllegalStateException(String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s", type, fallbackType, targetType, name));
                        } else {
                            return fallbackInstance;
                        }
                    }
                }
            });

            super.contract(new SentinelContractHolder(contract));
            return super.build();
        }

        private Object getFieldValue(Object instance, String fieldName) {
            Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);

            try {
                return field.get(instance);
            } catch (IllegalAccessException var5) {
                return null;
            }
        }

        @Override
        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            this.feignClientFactory = this.applicationContext.getBean(FeignClientFactory.class);
        }
    }
}
