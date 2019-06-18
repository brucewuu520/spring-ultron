package org.springultron.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作客户端
 *
 * @Auther: brucewuu
 * @Date: 2019-05-31 14:26
 * @Description:
 */
@Component
public class RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisClient(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 设置缓存键、值为String类型
     *
     * @param key   缓存key
     * @param value 值
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键、值为String类型
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     */
    public void set(String key, String value, Duration timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public boolean setIfAbsent(String key, String value) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().setIfAbsent(key, value)).orElse(Boolean.FALSE);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     * @return 是否设置成功
     */
    public boolean setIfAbsent(String key, String value, Duration timeout) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout)).orElse(Boolean.FALSE);
    }

    /**
     * 获取key的缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    @Nullable
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     *
     * @param key      缓存key
     * @param newValue 新值
     * @return 旧值
     */
    @Nullable
    public String getAndSet(String key, String newValue) {
        return stringRedisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 设置缓存键为String、值为Object类型
     *
     * @param key   缓存key
     * @param value 值
     */
    public void setObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键为String、值为Object类型
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     */
    public void setObject(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public boolean setObjectIfAbsent(String key, String value) {
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key, value)).orElse(Boolean.FALSE);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     * @return 是否设置成功
     */
    public boolean setObjectIfAbsent(String key, String value, Duration timeout) {
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key, value, timeout)).orElse(Boolean.FALSE);
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     * @param <V> 缓存泛型
     * @return 缓存值
     */
    @Nullable
    public <V> V getObject(String key) {
        //noinspection unchecked
        return (V) redisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     *
     * @param key      缓存key
     * @param newValue 新值
     * @param <V>      缓存泛型
     * @return 旧值
     */
    @Nullable
    public <V> V getObjectAndSet(String key, Object newValue) {
        //noinspection unchecked
        return (V) redisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 自增操作
     *
     * @param key   缓存key
     * @param delta 增量
     * @return
     */
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自增操作
     *
     * @param key   缓存key
     * @param delta 减量
     * @return
     */
    public Long decrement(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。
     *
     * @param key 缓存key
     * @return 缓存集合长度
     */
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素
     *
     * @param key   缓存key
     * @param start 开始索引
     * @param end   结束索引
     * @param <V>   值泛型
     * @return 值结合 start=0 end=-1可返还列表所有数据
     */
    public <V> List<V> range(String key, long start, long end) {
        //noinspection unchecked
        return (List<V>) redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @param key   缓存key
     * @param value 值
     * @param <V>   值泛型
     * @return 列表长度
     */
    public <V> Long leftPush(String key, V value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 批量把一个数组插入到列表中（头部），并返回列表长度
     *
     * @param key    缓存key
     * @param values 值数组
     * @param <V>    值泛型
     * @return 列表长度
     */
    public <V> Long leftPushAll(String key, V... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 批量把一个集合插入到列表中（头部），并返回列表长度
     *
     * @param key        缓存key
     * @param collection 值集合
     * @param <V>        值泛型
     * @return 列表长度
     */
    public <V> Long leftPushAll(String key, Collection<V> collection) {
        return redisTemplate.opsForList().leftPushAll(key, collection);
    }

    /**
     * 将所有指定的值插入存储在键的列表的尾部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）
     *
     * @param key   缓存key
     * @param value 值
     * @param <V>   值泛型
     * @return 列表长度
     */
    public <V> Long rightPush(String key, V value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 批量把一个数组插入到列表中（尾部），并返回列表长度
     *
     * @param key    缓存key
     * @param values 值数组
     * @param <V>    值泛型
     * @return 列表长度
     */
    public <V> Long rightPushAll(String key, V... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 批量把一个集合插入到列表中（尾部），并返回列表长度
     *
     * @param key        缓存key
     * @param collection 值集合
     * @param <V>        值泛型
     * @return 列表长度
     */
    public <V> Long rightPushAll(String key, Collection<V> collection) {
        return redisTemplate.opsForList().rightPushAll(key, collection);
    }

    /**
     * 设置缓存key过期时间，单位秒
     * 如果已过期或key不存在返回false
     *
     * @param key     缓存key
     * @param timeout 过期时间
     * @return 是否设置成功
     */
    public boolean expire(String key, Duration timeout) {
        if (TimeoutUtils.hasMillis(timeout)) {
            return Optional.ofNullable(redisTemplate.expire(key, timeout.toMillis(), TimeUnit.MILLISECONDS)).orElse(Boolean.FALSE);
        } else {
            return Optional.ofNullable(redisTemplate.expire(key, timeout.getSeconds(), TimeUnit.SECONDS)).orElse(Boolean.FALSE);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key 缓存key
     * @return 是否存在
     */
    public boolean exists(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(Boolean.FALSE);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存key数组
     */
    public boolean delete(String... keys) {
        final int length = null == keys ? 0 : keys.length;
        if (length == 0)
            return true;
        Boolean result = null;
        if (length == 1) {
            result = redisTemplate.delete(keys[0]);
        } else {
            Long l = redisTemplate.delete(Arrays.asList(keys));
            if (null != l && l > 0)
                result = true;
        }
        return null == result ? false : result;
    }

    /**
     * 模糊匹配批量删除
     *
     * @param pattern 匹配的前缀
     */
    public boolean deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (CollectionUtils.isEmpty(keys))
            return true;
        return Optional.ofNullable(redisTemplate.delete(keys))
                .map(result -> result > 0)
                .orElse(Boolean.FALSE);
    }
}