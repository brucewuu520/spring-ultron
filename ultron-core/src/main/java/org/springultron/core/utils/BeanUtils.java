package org.springultron.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * 实体工具
 *
 * @author brucewuu
 * @date 2019-06-28 14:29
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
    private BeanUtils() {}
    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    public static <T> T newInstance(Class<T> clazz) {
        return instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名(包括完整包路径)
     * @param <T>      泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            //noinspection unchecked
            return (T) instantiateClass(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("class %s not found", clazzStr));
        }
    }

    /**
     * 将一个对象的属性copy给另外一个对象
     *
     * @param source 源对象
     * @param target 目标对象类
     * @param <T>    泛型标记
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> target) throws BeansException {
        T bean = BeanUtils.instantiateClass(target);
        BeanUtils.copyProperties(source, bean);
        return bean;
    }

    /**
     * 对象转 Map（性能不佳，慎用！）
     *
     * @param bean 对象
     * @return Map
     */
    public static Map<String, Object> toMap(Object bean) {
        //noinspection unchecked
        return BeanMap.create(bean);
    }

    /**
     * Map 转对象（不支持bean的链式调用）
     *
     * @param map   源数据
     * @param clazz 目标对象类
     * @param <T>   泛型标记
     * @return 目标对象
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> clazz) {
        T bean = BeanUtils.instantiateClass(clazz);
        BeanMap.create(bean).putAll(map);
        return bean;
    }
}
