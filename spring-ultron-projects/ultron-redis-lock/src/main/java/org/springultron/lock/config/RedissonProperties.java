package org.springultron.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson Config
 *
 * @author brucewuu
 * @date 2020/4/27 21:30
 */
@ConfigurationProperties(
        prefix = "spring.redis.redisson"
)
public class RedissonProperties {
    private String config;

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
