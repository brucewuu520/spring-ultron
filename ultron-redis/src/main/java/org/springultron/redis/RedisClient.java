package org.springultron.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Set;
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

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, int expireTime) {
        stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setObject(String key, Object value, int timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
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
     * 设置缓存key过期时间，单位秒
     *
     * @param key     缓存key
     * @param timeout 过期时间
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout) {
        Boolean result = redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return null == result ? false : result;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key 缓存key
     * @return 是否存在
     */
    public boolean exists(String key) {
        Boolean isExist = redisTemplate.hasKey(key);
        return null == isExist ? false : isExist;
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存key数组
     */
    public void delete(String... keys) {
        if (null != keys && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(Arrays.asList(keys));
            }
        }
    }

    /**
     * 模糊匹配批量删除
     *
     * @param pattern 匹配的前缀
     */
    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
