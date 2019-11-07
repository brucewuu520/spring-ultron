package org.springultron.core.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.jackson.UltronJavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * Jackson工具类
 *
 * @author brucewuu
 * @date 2019-06-08 11:28
 */
public class Jackson {

    private Jackson() {}

    /**
     * 将对象序列化成json字符串
     *
     * @param value 对象
     * @return json字符串
     */
    public static String toJson(Object value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param value javaBean
     * @return json字符串
     */
    public static byte[] toBytes(Object value) {
        try {
            return getInstance().writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param json      json
     * @param valueType 类
     * @param <T>       泛型标记
     * @return Bean
     */
    public static <T> T parse(String json, Class<T> valueType) {
        try {
            return getInstance().readValue(json, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param bytes     json bytes
     * @param valueType 类
     * @param <T>       泛型标记
     * @return Bean
     */
    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param is        输入流
     * @param valueType 类
     * @param <T>       泛型标记
     * @return Bean
     */
    public static <T> T parse(InputStream is, Class<T> valueType) {
        try {
            return getInstance().readValue(is, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param json         json
     * @param valueTypeRef 泛型对象类型
     * @param <T>          泛型标记
     * @return Bean
     */
    public static <T> T parse(String json, TypeReference<T> valueTypeRef) {
        try {
            return getInstance().readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param bytes        bytes
     * @param valueTypeRef 泛型对象类型
     * @param <T>          泛型标记
     * @return Bean
     */
    public static <T> T parse(byte[] bytes, TypeReference<T> valueTypeRef) {
        try {
            return getInstance().readValue(bytes, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param is           输入流
     * @param valueTypeRef 泛型对象类型
     * @param <T>          泛型标记
     * @return Bean
     */
    public static <T> T parse(InputStream is, TypeReference<T> valueTypeRef) {
        try {
            return getInstance().readValue(is, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成Map
     *
     * @param json 字符串
     * @return Map 集合
     */
    public static Map<String, Object> parseMap(String json) {
        try {
            return getInstance().readValue(json, getMapType(String.class, Object.class));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成Map
     *
     * @param bytes bytes
     * @return Map 集合
     */
    public static Map<String, Object> parseMap(byte[] bytes) {
        try {
            return getInstance().readValue(bytes, getMapType(String.class, Object.class));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成Map
     *
     * @param is 输入流
     * @return Map 集合
     */
    public static Map<String, Object> parseMap(InputStream is) {
        try {
            return getInstance().readValue(is, getMapType(String.class, Object.class));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param json      json
     * @param valueType 类
     * @param <T>       泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(String json, Class<T> valueType) {
        try {
            return getInstance().readValue(json, getListType(valueType));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param bytes     bytes
     * @param valueType 类
     * @param <T>       泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, getListType(valueType));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param is        输入流
     * @param valueType 类
     * @param <T>       泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(InputStream is, Class<T> valueType) {
        try {
            return getInstance().readValue(is, getListType(valueType));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param json         json
     * @param valueTypeRef 泛型类型
     * @param <T>          泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(String json, TypeReference<? extends List<T>> valueTypeRef) {
        try {
            return getInstance().readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param bytes        bytes
     * @param valueTypeRef 泛型类型
     * @param <T>          泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(byte[] bytes, TypeReference<? extends List<T>> valueTypeRef) {
        try {
            return getInstance().readValue(bytes, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param is           输入流
     * @param valueTypeRef 泛型类型
     * @param <T>          泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(InputStream is, TypeReference<? extends List<T>> valueTypeRef) {
        try {
            return getInstance().readValue(is, valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Map转对象
     *
     * @param fromValue Map 集合
     * @param valueType 对象class
     * @param <T>       泛型标记
     * @return 对象
     * <p>
     * 性能不佳，建议使用
     * @see BeanUtils#toBean(Map, Class)
     */
    @Deprecated
    public static <T> T toPojo(Map fromValue, Class<T> valueType) {
        return getInstance().convertValue(fromValue, valueType);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param json json
     * @return {JsonNode}
     */
    public static JsonNode readTree(String json) {
        try {
            return getInstance().readTree(json);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in InputStream
     * @return {JsonNode}
     */
    public static JsonNode readTree(InputStream in) {
        try {
            return getInstance().readTree(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param bytes json字节数组
     * @return {JsonNode}
     */
    public static JsonNode readTree(byte[] bytes) {
        try {
            return getInstance().readTree(bytes);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return {JsonNode}
     */
    public static JsonNode readTree(JsonParser jsonParser) {
        try {
            return getInstance().readTree(jsonParser);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 封装 map type
     *
     * @param keyClass   key 类型
     * @param valueClass value 类型
     * @return MapType
     */
    private static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return getInstance().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * 封装 list type
     *
     * @param elementClass 集合值类型
     * @return CollectionLikeType
     */
    private static CollectionLikeType getListType(Class<?> elementClass) {
        return getInstance().getTypeFactory().constructCollectionLikeType(ArrayList.class, elementClass);
    }

    public static ObjectMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 3L;

        JacksonObjectMapper() {
            super();
            // 设置地点系统默认
            super.setLocale(Locale.getDefault());
            // 设置为系统默认时区
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            // 序列化时，Date日期的统一格式
            super.setDateFormat(new SimpleDateFormat(DateUtils.PATTERN_DATE_TIME, Locale.getDefault()));
            // 去掉默认的时间戳格式
            super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//            enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // 加上后会导致序列化后的字符串前面多了序列化对象地址(不是一个json字符串了)
            // 允许序列化空的POJO类
            super.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            super.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//            super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // 忽略JSON字符串中不识别的属性
            super.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
            super.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            // 允许单引号（非标准）
            super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            super.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            // java8日期格式化
            super.findAndRegisterModules();
            super.registerModule(new UltronJavaTimeModule());
        }

        @Override
        public ObjectMapper copy() {
            return super.copy();
        }
    }
}
