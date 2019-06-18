package org.springultron.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springultron.core.config.UltronJavaTimeModule;
import org.springultron.core.exception.Exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * Jackson工具类
 *
 * @Auther: brucewuu
 * @Date: 2019-06-08 11:28
 * @Description: Jackson工具类
 */
public class Jackson {

    /**
     * 将对象序列化成json字符串
     *
     * @param value javaBean
     * @param <T>   T 泛型标记
     * @return jsonString json字符串
     */
    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toBytes(Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param json      json
     * @param valueType class
     * @param <T>       T 泛型标记
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
     * 将json反序列化成对象
     *
     * @param json          json
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    public static <T> T parse(String json, TypeReference<?> typeReference) {
        try {
            return getInstance().readValue(json, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param bytes     json bytes
     * @param valueType class
     * @param <T>       T 泛型标记
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
     * @param bytes         bytes
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    public static <T> T parse(byte[] bytes, TypeReference<?> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in        InputStream
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parse(InputStream in, Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in            InputStream
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    public static <T> T parse(InputStream in, TypeReference<?> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param json          json
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(String json, TypeReference<? extends List<T>> typeReference) {
        try {
            return getInstance().readValue(json, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成Map集合
     *
     * @param json 字符串
     * @return Map 集合
     */
    public static HashMap toMap(String json) {
        try {
            return getInstance().readValue(json, HashMap.class);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Map集合转对象
     *
     * @param fromValue Map 集合
     * @param valueType 对象class
     * @param <T>       T 泛型标记
     * @return 对象
     */
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

    private static ObjectMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;

        JacksonObjectMapper() {
            super();
            // 设置地点为中国
            super.setLocale(Locale.CHINA);
            // 去掉默认的时间戳格式
            super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // 设置为中国上海时区
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            // 序列化时，日期的统一格式
            super.setDateFormat(new SimpleDateFormat(DateUtils.PATTERN_DATE_TIME, Locale.CHINA));
            // 允许序列化空的POJO类
            super.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            // 忽略JSON字符串中不识别的属性
            super.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
            super.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            // 允许单引号（非标准）
            super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            super.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            // 日期格式化
            super.registerModule(new UltronJavaTimeModule());
            super.findAndRegisterModules();
        }

        @Override
        public ObjectMapper copy() {
            return super.copy();
        }
    }
}
