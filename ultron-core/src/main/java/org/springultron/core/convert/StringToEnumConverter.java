package org.springultron.core.convert;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;
import org.springultron.core.utils.ConvertUtils;
import org.springultron.core.utils.StringUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API 接收参数 Jackson String 转换为 Enum
 *
 * @author brucewuu
 * @date 2019-08-12 11:41
 */
public class StringToEnumConverter implements ConditionalGenericConverter {
    private static final ConcurrentHashMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = new ConcurrentHashMap<>(8);

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return true;
    }

    @Nullable
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Enum.class));
    }

    @Nullable
    @Override
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (StringUtils.isBlank((String) source)) {
            return null;
        }
        Class<?> targetClazz = targetType.getType();
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(targetClazz, StringToEnumConverter::getAnnotation);
        String value = ((String) source).trim();
        if (null == accessibleObject) {
            return valueOf(targetClazz, value);
        }
        try {
            return StringToEnumConverter.invoke(targetClazz, accessibleObject, value);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Nullable
    private static AccessibleObject getAnnotation(Class<?> clazz) {
        Set<AccessibleObject> accessibleObjects = new HashSet<>();
        Constructor<?>[] constructors = clazz.getConstructors();
        Collections.addAll(accessibleObjects, constructors);
        Method[] methods = clazz.getDeclaredMethods();
        Collections.addAll(accessibleObjects, methods);
        for (AccessibleObject accessibleObject : accessibleObjects) {
            JsonCreator jsonCreator = accessibleObject.getAnnotation(JsonCreator.class);
            if (null != jsonCreator && jsonCreator.mode() != JsonCreator.Mode.DISABLED) {
                accessibleObject.setAccessible(true);
                return accessibleObject;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T valueOf(Class<?> clazz, String value) {
        return Enum.valueOf((Class<T>) clazz, value);
    }

    @Nullable
    private static Object invoke(Class<?> clazz, AccessibleObject accessibleObject, String value) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (accessibleObject instanceof Constructor) {
            Constructor constructor = (Constructor) accessibleObject;
            Class<?> paramType = constructor.getParameterTypes()[0];
            Object object = ConvertUtils.convert(value, paramType);
            return constructor.newInstance(object);
        }
        if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            Class<?> paramType = method.getParameterTypes()[0];
            Object object = ConvertUtils.convert(value, paramType);
            return method.invoke(clazz, object);
        }
        return null;
    }
}
