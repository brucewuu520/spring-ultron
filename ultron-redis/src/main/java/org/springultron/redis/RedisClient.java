package org.springultron.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作客户端
 *
 * @author brucewuu
 * @date 2019-05-31 14:26
 */
@Component
public class RedisClient {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisClient(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置缓存键、值为String类型
     *
     * @param key   缓存key
     * @param value 值
     */
    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键、值为String类型
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     */
    public void setString(String key, String value, Duration timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public boolean setStringIfAbsent(String key, String value) {
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
    public boolean setStringIfAbsent(String key, String value, Duration timeout) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout)).orElse(Boolean.FALSE);
    }

    /**
     * 获取key的缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     *
     * @param key      缓存key
     * @param newValue 新值
     * @return 旧值
     */
    public String getStringAndSet(String key, String newValue) {
        return stringRedisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 设置缓存键为String、值为Object类型
     *
     * @param key   缓存key
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键为String、值为Object类型
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     */
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public boolean setIfAbsent(String key, String value) {
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
    public boolean setIfAbsent(String key, String value, Duration timeout) {
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key, value, timeout)).orElse(Boolean.FALSE);
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     * @param <V> 缓存泛型
     * @return 缓存值
     */
    public <V> V get(String key) {
        //noinspection unchecked
        return (V) redisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     *
     * @param key      缓存 key
     * @param newValue 新值
     * @param <V>      缓存值泛型
     * @return 旧值
     */
    public <V> V getAndSet(String key, Object newValue) {
        //noinspection unchecked
        return (V) redisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 自增操作
     *
     * @param key   缓存 key
     * @param delta 增量
     * @return 自增后的值
     */
    public Long increment(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自减操作
     *
     * @param key   缓存 key
     * @param delta 减量
     * @return 自减后的值
     */
    public Long decrement(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 判断Hash表中是否存在缓存项item
     *
     * @param key  缓存 key
     * @param item item 项
     * @return 存在:true 不存在:false
     */
    public boolean hasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * HashGet
     *
     * @param key 缓存 key
     * @return Hash数据结构
     */
    public Map<Object, Object> hget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashGet
     *
     * @param key  缓存 key
     * @param item item 项
     * @param <V>  值泛型
     * @return Hash数据结构中item项值
     */
    public <V> V hget(String key, String item) {
        //noinspection unchecked
        return (V) redisTemplate.opsForHash().get(key, item);
    }

    /**
     * HashSet
     *
     * @param key 缓存 key
     * @param map 对应多个键值
     */
    public void hset(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * HashSet
     *
     * @param key     缓存 key
     * @param map     对应多个键值
     * @param timeout 过期时长
     */
    public boolean hset(final String key, Map<String, Object> map, Duration timeout) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, timeout);
    }

    /**
     * 向一张Hash表中放入数据,如果不存在将创建
     *
     * @param key   缓存 key
     * @param item  项
     * @param value 值
     */
    public void hset(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 删除Hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public boolean hdel(String key, Object... item) {
        return Optional.of(redisTemplate.opsForHash().delete(key, item)).map(l -> l > 0).orElse(Boolean.FALSE);
    }

    /**
     * 返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。
     *
     * @param key 缓存 key
     * @return 缓存集合长度
     */
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 返回list指定下标的值
     *
     * @param key   缓存 key
     * @param index list下标，从0开始
     * @param <V>   值泛型
     * @return 缓存值
     */
    public <V> V get(String key, long index) {
        //noinspection unchecked
        return (V) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素
     *
     * @param key   缓存 key
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
     * @param key   缓存 key
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
     * @param key    缓存 key
     * @param values 值数组
     * @return 列表长度
     */
    public Long leftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 批量把一个集合插入到列表中（头部），并返回列表长度
     *
     * @param key        缓存 key
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
     * @param key   缓存 key
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
     * @param key    缓存 key
     * @param values 值数组
     * @return 列表长度
     */
    public Long rightPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 批量把一个集合插入到列表中（尾部），并返回列表长度
     *
     * @param key        缓存 key
     * @param collection 值集合
     * @param <V>        值泛型
     * @return 列表长度
     */
    public <V> Long rightPushAll(String key, Collection<V> collection) {
        return redisTemplate.opsForList().rightPushAll(key, collection);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 缓存 key
     * @return 时间(毫秒) 返回0代表为永久有效, 返回-2代表key不存在或已过期
     */
    public long getExpire(String key) {
        return Optional.ofNullable(redisTemplate.getExpire(key)).orElse(-2L);
    }

    /**
     * 设置缓存过期时间，单位秒
     * 如果已过期或key不存在返回false
     *
     * @param key     缓存 key
     * @param timeout 过期时长
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
     * 设置缓存过期日期
     *
     * @param key  缓存 key
     * @param date 过期日期
     * @return 是否设置成功
     */
    public boolean expireAt(String key, Date date) {
        return Optional.ofNullable(redisTemplate.expireAt(key, date)).orElse(Boolean.FALSE);
    }

    /**
     * 设置缓存永久有效
     *
     * @param key 缓存 key
     * @return 是否设置成功
     */
    public boolean persist(String key) {
        return Optional.ofNullable(redisTemplate.persist(key)).orElse(Boolean.FALSE);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key 缓存 key
     * @return 是否存在
     */
    public boolean exists(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(Boolean.FALSE);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存 key数组
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
        return Optional.ofNullable(redisTemplate.delete(keys)).map(l -> l > 0).orElse(Boolean.FALSE);
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 生成 缓存key，以英文冒号隔开
     *
     * @param objs 参数
     * @return key string
     */
    public static String getKey(Object... objs) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : objs) {
            builder.append(obj).append(":");
        }
        return builder.substring(0, builder.length() - 1);
    }
}