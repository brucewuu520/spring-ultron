package org.springultron.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springultron.core.utils.BeanUtils;
import org.springultron.core.utils.DateUtils;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.type.CollectionLikeType;
import tools.jackson.databind.type.MapType;

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

    private Jackson() {
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param value 对象
     * @return json字符串
     */
    public static String toJson(Object value) {
        return getInstance().writeValueAsString(value);
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param value javaBean
     * @return json字符串
     */
    public static byte[] toBytes(Object value) {
        return getInstance().writeValueAsBytes(value);
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
        return getInstance().readValue(json, valueType);
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
        return getInstance().readValue(bytes, valueType);
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
        return getInstance().readValue(is, valueType);
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
        return getInstance().readValue(json, valueTypeRef);
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
        return getInstance().readValue(bytes, valueTypeRef);
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
        return getInstance().readValue(is, valueTypeRef);
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
        return getInstance().readValue(json, getMapType(keyClass, valueClass));
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
        return getInstance().readValue(bytes, getMapType(keyClass, valueClass));
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
        return getInstance().readValue(is, getMapType(keyClass, valueClass));
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
        return getInstance().readValue(json, getListType(valueType));
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
        return getInstance().readValue(bytes, getListType(valueType));
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
        return getInstance().readValue(is, getListType(valueType));
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
        return getInstance().readValue(json, valueTypeRef);
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
        return getInstance().readValue(bytes, valueTypeRef);
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
        return getInstance().readValue(is, valueTypeRef);
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
        return getInstance().readTree(json);

    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in InputStream
     * @return {JsonNode}
     */
    public static JsonNode readTree(InputStream in) {
        return getInstance().readTree(in);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param bytes json字节数组
     * @return {JsonNode}
     */
    public static JsonNode readTree(byte[] bytes) {
        return getInstance().readTree(bytes);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return {JsonNode}
     */
    public static JsonNode readTree(JsonParser jsonParser) {
        return getInstance().readTree(jsonParser);
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
        return n.asString();
    }

    public static String getString(JsonNode node, String fieldName, String defaultValue) {
        JsonNode n = node.get(fieldName);
        if (n == null || n.isNull()) {
            return defaultValue;
        }
        return n.asString(defaultValue);
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

    public static JsonMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final JsonMapper INSTANCE = JsonMapper.builder()
                                                             .defaultLocale(Locale.getDefault())
                                                             .defaultTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                                                             .defaultDateFormat(new SimpleDateFormat(DateUtils.PATTERN_DATE_TIME, Locale.getDefault()))
                                                             .changeDefaultPropertyInclusion(
                                                                     (handler) -> handler.withValueInclusion(JsonInclude.Include.NON_NULL)
                                                                                         .withContentInclusion(JsonInclude.Include.NON_NULL)
                                                             )
                                                             // 可解析反斜杠引用的所有字符
                                                             .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                                                             // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
                                                             .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                                                             // 单引号
                                                             .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true)
                                                             // 忽略json字符串中不识别的属性
                                                             .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                             // 忽略json字符串中不识别的属性
                                                             .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                                                             .findAndAddModules()
                                                             .addModule(new UltronJavaTimeModule())
                                                             .build();
    }
}