package org.springultron.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类
 *
 * @Auther: brucewuu
 * @Date: 2019-06-09 16:42
 * @Description:
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == SpringUtils.context) {
            SpringUtils.context = applicationContext;
        }
    }

    @Nullable
    public static <T> T getBean(Class<T> clazz) {
        if (null == clazz || null == context)
            return null;
        return context.getBean(clazz);
    }

    @Nullable
    public static <T> T getBean(String beanId) {
        if (StringUtils.isEmpty(beanId) || null == context)
            return null;
        // noinspection unchecked
        return (T) context.getBean(beanId);
    }

    @Nullable
    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (StringUtils.isEmpty(beanName) || null == clazz || null == context)
            return null;
        return context.getBean(beanName, clazz);
    }

    @Nullable
    public static ApplicationContext getContext() {
        if (null == context) {
            return null;
        }
        return context;
    }

    public static void publishEvent(ApplicationEvent event) {
        if (null == context) {
            return;
        }
        context.publishEvent(event);
    }
}
