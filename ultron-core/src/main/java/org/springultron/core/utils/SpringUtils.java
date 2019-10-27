package org.springultron.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类
 *
 * @author brucewuu
 * @date 2019-06-09 16:42
 */
@Lazy
@Component
public class SpringUtils implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringUtils.context = applicationContext;
    }

    @Nullable
    public static <T> T getBean(Class<T> clazz) {
        if (null == clazz || null == context)
            return null;
        return context.getBean(clazz);
    }

    @Nullable
    public static <T> T getBean(String beanId) {
        if (Strings.isEmpty(beanId) || null == context)
            return null;
        //noinspection unchecked
        return (T) context.getBean(beanId);
    }

    @Nullable
    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (Strings.isEmpty(beanName) || null == clazz || null == context)
            return null;
        return context.getBean(beanName, clazz);
    }

    public static ApplicationContext getContext() {
        if (null == context) {
            throw new RuntimeException("ApplicationContext can not be null");
        }
        return context;
    }

    public static void publishEvent(ApplicationEvent event) {
        if (null == context) {
            throw new RuntimeException("ApplicationContext is null");
        }
        context.publishEvent(event);
    }

    @Override
    public void destroy() throws Exception {
        SpringUtils.context = null;
    }
}
