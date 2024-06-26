package org.springultron.core.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.lang.Nullable;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.utils.BeanUtils;
import org.springultron.core.utils.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
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

    private Jackson() {
    }

    /**
     * 判断是否可以序列化
     *
     * @param value 对象
     * @return 是否可以序列化
     */
    public static boolean canSerialize(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        return getInstance().canSerialize(value.getClass());
    }

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
        return parseMap(json, String.class, Object.class);
    }

    /**
     * 将json反序列化成Map
     *
     * @param bytes bytes
     * @return Map 集合
     */
    public static Map<String, Object> parseMap(byte[] bytes) {
        return parseMap(bytes, String.class, Object.class);
    }

    /**
     * 将json反序列化成Map
     *
     * @param is 输入流
     * @return Map 集合
     */
    public static Map<String, Object> parseMap(InputStream is) {
        return parseMap(is, String.class, Object.class);
    }

    /**
     * 将json反序列化成Map
     *
     * @param json       字符串
     * @param keyClass   键类型
     * @param valueClass 值类型
     * @param <K>        键泛型
     * @param <V>        值泛型
     * @return Map 集合
     */
    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyClass, Class<V> valueClass) {
        try {
            return getInstance().readValue(json, getMapType(keyClass, valueClass));
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成Map
     *
     * @param bytes      bytes
     * @param keyClass   键类型
     * @param valueClass 值类型
     * @param <K>        键泛型
     * @param <V>        值泛型
     * @return Map 集合
     */
    public static <K, V> Map<K, V> parseMap(byte[] bytes, Class<K> keyClass, Class<V> valueClass) {
        try {
            return getInstance().readValue(bytes, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成Map
     *
     * @param is         输入流
     * @param keyClass   键类型
     * @param valueClass 值类型
     * @param <K>        键泛型
     * @param <V>        值泛型
     * @return Map 集合
     */
    public static <K, V> Map<K, V> parseMap(InputStream is, Class<K> keyClass, Class<V> valueClass) {
        try {
            return getInstance().readValue(is, getMapType(keyClass, valueClass));
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
    public static <T> List<T> parseList(String json, Class<T> valueType) {
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
    public static <T> List<T> parseList(byte[] bytes, Class<T> valueType) {
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
    public static <T> List<T> parseList(InputStream is, Class<T> valueType) {
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
    public static <T> List<T> parseList(String json, TypeReference<? extends List<T>> valueTypeRef) {
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
    public static <T> List<T> parseList(byte[] bytes, TypeReference<? extends List<T>> valueTypeRef) {
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
    public static <T> List<T> parseList(InputStream is, TypeReference<? extends List<T>> valueTypeRef) {
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
    public static <T> T toPojo(Map<?, ?> fromValue, Class<T> valueType) {
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
        @Serial
        private static final long serialVersionUID = 3L;

        JacksonObjectMapper() {
            super();
            // 设置地点系统默认
            super.setLocale(Locale.getDefault());
            // 设置为系统默认时区
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            // 序列化时，Date日期的统一格式
            super.setDateFormat(new SimpleDateFormat(DateUtils.PATTERN_DATE_TIME, Locale.getDefault()));
            super.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            // 允许序列化空的POJO类
            super.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            super.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
            // 允许单引号（非标准）
            super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            // 忽略无法转换的对象
            super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            // 忽略JSON字符串中不识别的属性
            super.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // java8日期格式化
            super.findAndRegisterModules();
            super.registerModule(new UltronJavaTimeModule());
        }

        @Override
        public ObjectMapper copy() {
            return super.copy();
        }
    }

    /**
     * 创建 ObjectNode
     *
     * @return ObjectNode
     */
    public static ObjectNode createObjectNode() {
        return getInstance().createObjectNode();
    }

    /**
     * 创建 ArrayNode
     *
     * @return ArrayNode
     */
    public static ArrayNode createArrayNode() {
        return getInstance().createArrayNode();
    }

    public static String getString(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return null;
        }
        return n.asText();
    }

    public static String getString(JsonNode node, String fieldName, String defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.asText(defaultValue);
    }

    public static Integer getInteger(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return null;
        }
        return n.asInt();
    }

    public static int getIntValue(JsonNode node, String fieldName, int defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.asInt(defaultValue);
    }

    public static Long getLong(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return null;
        }
        return n.asLong();
    }

    public static long getLongValue(JsonNode node, String fieldName, long defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.asLong(defaultValue);
    }

    public static Float getFloat(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return null;
        }
        return n.floatValue();
    }

    public static float getFloatValue(JsonNode node, String fieldName, float defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.floatValue();
    }

    public static Boolean getBoolean(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return null;
        }
        return n.asBoolean();
    }

    public static boolean getBooleanValue(JsonNode node, String fieldName, boolean defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.asBoolean(defaultValue);
    }

    public static Double getDouble(JsonNode node, String fieldName) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return null;
        }
        return n.asDouble();
    }

    public static double getDoubleValue(JsonNode node, String fieldName, double defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.asDouble(defaultValue);
    }
}