package org.springultron.redis;

import org.reactivestreams.Publisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

/**
 * 反应式Redis操作客户端
 *
 * @author brucewuu
 * @date 2019/11/26 18:17
 */
public class ReactiveRedisClient {
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public ReactiveRedisClient(ReactiveStringRedisTemplate reactiveStringRedisTemplate, ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public ReactiveStringRedisTemplate getReactiveStringRedisTemplate() {
        return reactiveStringRedisTemplate;
    }

    public ReactiveRedisTemplate<String, Object> getReactiveRedisTemplate() {
        return reactiveRedisTemplate;
    }

    /**
     * 生成缓存key，以双英文冒号隔开
     *
     * @param keys kes
     * @return key string
     */
    public static String genKey(String... keys) {
        return String.join("::", keys).toUpperCase();
    }

    /**
     * 设置缓存键、值为String类型
     *
     * @param key   缓存key
     * @param value 值
     */
    public Mono<Boolean> setString(String key, String value) {
        return reactiveStringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键、值为String类型
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     */
    public Mono<Boolean> setString(String key, String value, Duration timeout) {
        return reactiveStringRedisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public Mono<Boolean> setStringIfAbsent(String key, String value) {
        return reactiveStringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     * @return 是否设置成功
     */
    public Mono<Boolean> setStringIfAbsent(String key, String value, Duration timeout) {
        return reactiveStringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    /**
     * 如果key存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public Mono<Boolean> setStringIfPresent(String key, String value) {
        return reactiveStringRedisTemplate.opsForValue().setIfPresent(key, value);
    }

    /**
     * 如果key存在，则设置当前key缓存成功；否则失败
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     * @return 是否设置成功
     */
    public Mono<Boolean> setStringIfPresent(String key, String value, Duration timeout) {
        return reactiveStringRedisTemplate.opsForValue().setIfPresent(key, value, timeout);
    }

    /**
     * 获取key的缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    public Mono<String> getString(String key) {
        return reactiveStringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     *
     * @param key      缓存key
     * @param newValue 新值
     * @return 旧值
     */
    public Mono<String> getStringAndSet(String key, String newValue) {
        return reactiveStringRedisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 设置缓存键为String、值为Object类型
     *
     * @param key   缓存key
     * @param value 值
     */
    public Mono<Boolean> set(String key, Object value) {
        return reactiveRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键为String、值为Object类型
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     */
    public Mono<Boolean> set(String key, Object value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public Mono<Boolean> setIfAbsent(String key, Object value) {
        return reactiveRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 如果key不存在，则设置当前key缓存成功；否则失败
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     * @return 是否设置成功
     */
    public Mono<Boolean> setIfAbsent(String key, Object value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    /**
     * 如果key存在，则设置当前key缓存成功；否则失败
     *
     * @param key   缓存key
     * @param value 值
     * @return 是否设置成功
     */
    public Mono<Boolean> setIfPresent(String key, Object value) {
        return reactiveRedisTemplate.opsForValue().setIfPresent(key, value);
    }

    /**
     * 如果key存在，则设置当前key缓存成功；否则失败
     *
     * @param key     缓存key
     * @param value   值
     * @param timeout 缓存过期时间
     * @return 是否设置成功
     */
    public Mono<Boolean> setIfPresent(String key, Object value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue().setIfPresent(key, value, timeout);
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     * @param <V> 缓存泛型
     * @return 缓存值
     */
    public <V> Mono<V> get(String key) {
        //noinspection unchecked
        return (Mono<V>) reactiveRedisTemplate.opsForValue().get(key);
    }

    /**
     * 对key设置newValue这个值，并且返回key原来的旧值
     *
     * @param key      缓存 key
     * @param newValue 新值
     * @param <V>      缓存值泛型
     * @return 旧值
     */
    public <V> Mono<V> getAndSet(String key, Object newValue) {
        //noinspection unchecked
        return (Mono<V>) reactiveRedisTemplate.opsForValue().getAndSet(key, newValue);
    }

    /**
     * 自增操作，每次+1
     *
     * @param key 缓存 key
     * @return 自增后的值
     */
    public Mono<Long> increment(String key) {
        return reactiveStringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增操作
     *
     * @param key   缓存 key
     * @param delta 增量
     * @return 自增后的值
     */
    public Mono<Long> increment(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        return reactiveStringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自减操作，每次-1
     *
     * @param key 缓存 key
     * @return 自减后的值
     */
    public Mono<Long> decrement(String key) {
        return reactiveStringRedisTemplate.opsForValue().decrement(key);
    }

    /**
     * 自减操作
     *
     * @param key   缓存 key
     * @param delta 减量
     * @return 自减后的值
     */
    public Mono<Long> decrement(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递减因子必须大于0");
        }
        return reactiveStringRedisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 判断Hash链表中是否存在缓存项item
     *
     * @param key  缓存 key
     * @param item item 项
     * @return 存在:true 不存在:false
     */
    public Mono<Boolean> hasKey(String key, String item) {
        return reactiveRedisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * HashGet
     *
     * @param key 缓存 key
     * @return Hash数据结构
     */
    public Flux<Map.Entry<Object, Object>> hget(String key) {
        return reactiveRedisTemplate.opsForHash().entries(key);
    }

    /**
     * HashGet
     *
     * @param key  缓存 key
     * @param item item 项
     * @param <V>  值泛型
     * @return Hash数据结构中item项值
     */
    public <V> Mono<V> hget(String key, String item) {
        //noinspection unchecked
        return (Mono<V>) reactiveRedisTemplate.opsForHash().get(key, item);
    }

    /**
     * HashSet
     *
     * @param key 缓存 key
     * @param map 对应多个键值
     */
    public Mono<Boolean> hset(String key, Map<String, Object> map) {
        return reactiveRedisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * HashSet
     *
     * @param key     缓存 key
     * @param map     对应多个键值
     * @param timeout 过期时长
     */
    public Mono<Boolean> hset(final String key, Map<String, Object> map, Duration timeout) {
        return reactiveRedisTemplate.opsForHash().putAll(key, map)
                .filter(success -> success)
                .flatMap(success -> expire(key, timeout));
    }

    /**
     * 向一张Hash链表中放入数据,如果不存在将自动创建
     *
     * @param key   缓存 key
     * @param item  项
     * @param value 值
     */
    public Mono<Boolean> hset(String key, String item, Object value) {
        return reactiveRedisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 删除整个Hash链链表
     *
     * @param key 键 不能为null
     */
    public Mono<Boolean> hdel(String key) {
        return reactiveRedisTemplate.opsForHash().delete(key);
    }

    /**
     * 删除Hash链表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public Mono<Long> hdel(String key, Object... item) {
        return reactiveRedisTemplate.opsForHash().remove(key, item);
    }

    /**
     * 返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。
     *
     * @param key 缓存 key
     * @return 缓存集合长度
     */
    public Mono<Long> size(String key) {
        return reactiveRedisTemplate.opsForList().size(key);
    }

    /**
     * 返回list指定下标的值
     *
     * @param key   缓存 key
     * @param index list下标，从0开始
     * @param <V>   值泛型
     * @return 缓存值
     */
    public <V> Mono<V> get(String key, long index) {
        //noinspection unchecked
        return (Mono<V>) reactiveRedisTemplate.opsForList().index(key, index);
    }

    /**
     * 返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素
     *
     * @param key   缓存 key
     * @param start 开始索引
     * @param end   结束索引
     * @return 值结合 start=0 end=-1可返还列表所有数据
     */
    public <V> Flux<V> range(String key, long start, long end) {
        //noinspection unchecked
        return (Flux<V>) reactiveRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表（从左边插入）
     *
     * @param key   缓存 key
     * @param value 值
     * @param <V>   值泛型
     * @return 列表长度
     */
    public <V> Mono<Long> leftPush(String key, V value) {
        return reactiveRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 批量把一个数组插入到列表中（头部），并返回列表长度
     *
     * @param key    缓存 key
     * @param values 值数组
     * @return 列表长度
     */
    public Mono<Long> leftPushAll(String key, Object... values) {
        return reactiveRedisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 批量把一个集合插入到列表中（头部），并返回列表长度
     *
     * @param key        缓存 key
     * @param collection 值集合
     * @return 列表长度
     */
    public Mono<Long> leftPushAll(String key, Collection collection) {
        //noinspection unchecked
        return reactiveRedisTemplate.opsForList().leftPushAll(key, collection);
    }

    /**
     * 如果换成列表存在，则插入列表头部（从左边插入）
     *
     * @param key   缓存 key
     * @param value 值
     * @param <V>   值泛型
     * @return 列表长度
     */
    public <V> Mono<Long> leftPushIfPresent(String key, V value) {
        return reactiveRedisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 将所有指定的值插入存储在键的列表的尾部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）
     *
     * @param key   缓存 key
     * @param value 值
     * @param <V>   值泛型
     * @return 列表长度
     */
    public <V> Mono<Long> rightPush(String key, V value) {
        return reactiveRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 批量把一个数组插入到列表中（尾部），并返回列表长度
     *
     * @param key    缓存 key
     * @param values 值数组
     * @return 列表长度
     */
    public Mono<Long> rightPushAll(String key, Object... values) {
        return reactiveRedisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 批量把一个集合插入到列表中（尾部），并返回列表长度
     *
     * @param key        缓存 key
     * @param collection 值集合
     * @return 列表长度
     */
    public Mono<Long> rightPushAll(String key, Collection collection) {
        //noinspection unchecked
        return reactiveRedisTemplate.opsForList().rightPushAll(key, collection);
    }

    /**
     * 如果换成列表存在，则插入列尾部部（从右边插入）
     *
     * @param key   缓存 key
     * @param value 值
     * @param <V>   值泛型
     * @return 列表长度
     */
    public <V> Mono<Long> rightPushIfPresent(String key, V value) {
        return reactiveRedisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 缓存 key
     * @return Duration时间
     */
    public Mono<Duration> getExpire(String key) {
        return reactiveRedisTemplate.getExpire(key);
    }

    /**
     * 设置缓存过期时间，单位秒
     * 如果已过期或key不存在返回false
     *
     * @param key     缓存 key
     * @param timeout 过期时长
     * @return 是否设置成功
     */
    public Mono<Boolean> expire(String key, Duration timeout) {
        return reactiveRedisTemplate.expire(key, timeout);
    }

    /**
     * 设置缓存过期日期
     *
     * @param key      缓存 key
     * @param expireAt 过期日期
     * @return 是否设置成功
     */
    public Mono<Boolean> expireAt(String key, Instant expireAt) {
        return reactiveRedisTemplate.expireAt(key, expireAt);
    }

    /**
     * 设置缓存永久有效
     *
     * @param key 缓存 key
     * @return 是否设置成功
     */
    public Mono<Boolean> persist(String key) {
        return reactiveRedisTemplate.persist(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 缓存 key
     * @return 是否存在
     */
    public Mono<Boolean> exists(String key) {
        return reactiveRedisTemplate.hasKey(key);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存 key数组
     */
    public Mono<Long> delete(String... keys) {
        return reactiveRedisTemplate.delete(keys);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存 key流
     */
    public Mono<Long> delete(Publisher<String> keys) {
        return reactiveRedisTemplate.delete(keys);
    }

    /**
     * 模糊匹配批量删除
     *
     * @param pattern 匹配的前缀
     */
    public Mono<Long> deleteByPattern(String pattern) {
        return reactiveRedisTemplate.delete(reactiveRedisTemplate.keys(pattern));
    }
}
