package org.springultron.core.utils;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;
import org.springultron.core.convert.UltronConversionService;

/**
 * 基于 Spring ConversionService 的类型转换
 *
 * @Auther: brucewuu
 * @Date: 2019-08-12 11:31
 * @Description:
 */
@SuppressWarnings("unchecked")
public class ConvertUtils {

    @Nullable
    public static <T> T convert(Object source, Class<T> targetType) {
        if (null == source)
            return null;
        if (ClassUtils.isAssignableValue(targetType, source)) {
            return (T) source;
        }
        GenericConversionService conversionService = UltronConversionService.getInstance();
        return conversionService.convert(source, targetType);
    }

    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        GenericConversionService conversionService = UltronConversionService.getInstance();
        return (T) conversionService.convert(source, targetType);
    }

    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (null == source) {
            return null;
        }
        GenericConversionService conversionService = UltronConversionService.getInstance();
        return (T) conversionService.convert(source, sourceType, targetType);
    }
}
