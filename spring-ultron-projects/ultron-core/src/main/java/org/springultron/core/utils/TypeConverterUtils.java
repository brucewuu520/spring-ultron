package org.springultron.core.utils;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;

/**
 * 对象类型转换
 *
 * @author brucewuu
 * @date 2021/4/17 上午11:54
 */
public class TypeConverterUtils {
    private static final TypeConverter TYPE_CONVERTER = new SimpleTypeConverter();

    /**
     * 对象值转换
     *
     * @param value        值
     * @param requiredType 需转换对象类型
     * @param <T>          泛型
     */
    @Nullable
    public static <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType) {
        return TYPE_CONVERTER.convertIfNecessary(value, requiredType);
    }

    /**
     * 对象值转换
     *
     * @param value        值
     * @param requiredType 需转换对象类型
     * @param <T>          泛型
     */
    @Nullable
    public static <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable MethodParameter methodParameter) {
        return TYPE_CONVERTER.convertIfNecessary(value, requiredType, methodParameter);
    }

    /**
     * 对象值转换
     *
     * @param value        值
     * @param requiredType 需转换对象类型
     * @param <T>          泛型
     */
    @Nullable
    public static <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable Field field) {
        return TYPE_CONVERTER.convertIfNecessary(value, requiredType, field);
    }

    /**
     * 对象值转换
     *
     * @param value          值
     * @param requiredType   需转换对象类型
     * @param typeDescriptor TypeDescriptor
     * @param <T>            泛型
     */
    public static <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) {
        return TYPE_CONVERTER.convertIfNecessary(value, requiredType, typeDescriptor);
    }
}
