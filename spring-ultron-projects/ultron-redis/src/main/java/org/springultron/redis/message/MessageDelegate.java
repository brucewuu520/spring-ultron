package org.springultron.redis.message;

/**
 * Redis 消息队列监听代理
 *
 * @author brucewuu
 * @date 2021/4/22 上午11:15
 */
public interface MessageDelegate<T> {
    /**
     * handle message
     *
     * @param message 消息体
     * @param channel 频道
     */
    void handleMessage(T message, String channel);
}
