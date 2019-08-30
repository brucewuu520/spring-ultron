package org.springultron.core.convert;

import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;
import org.springultron.core.utils.ConvertUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API 响应体 Jackson Enum 转换为 String
 *
 * @Auther: brucewuu
 * @Date: 2019-08-12 11:06
 * @Description:
 */
public class EnumToStringConverter implements ConditionalGenericConverter {
    private static final Logger log = LoggerFactory.getLogger(EnumToStringConverter.class);

    private static final ConcurrentHashMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = new ConcurrentHashMap<>(8);

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return true;
    }

    @Nullable
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairSet = new HashSet<>(3);
        pairSet.add(new ConvertiblePair(Enum.class, String.class));
        pairSet.add(new ConvertiblePair(Enum.class, Integer.class));
        pairSet.add(new ConvertiblePair(Enum.class, Long.class));
        return Collections.unmodifiableSet(pairSet);
    }

    @Nullable
    @Override
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (null == source) {
            return null;
        }
        Class<?> sourceClazz = sourceType.getType();
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(sourceClazz, EnumToStringConverter::getAnnotation);
        Class<?> targetClazz = targetType.getType();
        if (null == accessibleObject) {
            if (targetClazz == String.class) {
                return ((Enum) source).name();
            }
            int ordinal = ((Enum) source).ordinal();
            return ConvertUtils.convert(ordinal, targetClazz);
        }
        try {
            return EnumToStringConverter.invoke(sourceClazz, accessibleObject, source, targetClazz);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Nullable
    private static AccessibleObject getAnnotation(Class<?> clazz) {
        Set<AccessibleObject> accessibleObjectSet = new HashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        Collections.addAll(accessibleObjectSet, fields);
        Method[] methods = clazz.getDeclaredMethods();
        Collections.addAll(accessibleObjectSet, methods);
        for (AccessibleObject accessibleObject : accessibleObjectSet) {
            JsonValue jsonValue = accessibleObject.getAnnotation(JsonValue.class);
            if (null != jsonValue && jsonValue.value()) {
                accessibleObject.setAccessible(true);
                return accessibleObject;
            }
        }
        return null;
    }

    @Nullable
    private static Object invoke(Class<?> clazz, AccessibleObject accessibleObject, Object source, Class<?> targetClazz) throws IllegalAccessException, InvocationTargetException {
        Object value = null;
        if (accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            value = field.get(source);
        } else if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            Class<?> paramType = method.getParameterTypes()[0];
            Object object = ConvertUtils.convert(source, paramType);
            value = method.invoke(clazz, object);
        }
        if (null == value) {
            return null;
        }
        return ConvertUtils.convert(value, targetClazz);
    }
}
