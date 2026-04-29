package org.springultron.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson Config
 *
 * @author brucewuu
 * @date 2020/4/27 21:30
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

    private String config;

    private String file;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
