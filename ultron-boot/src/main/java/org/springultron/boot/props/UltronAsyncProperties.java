package org.springultron.boot.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * spring boot异步任务线程池配置
 *
 * @author brucewuu
 * @date 2019/11/11 14:31
 */
@ConfigurationProperties(prefix = "ultron.async")
public class UltronAsyncProperties {
    /**
     * 核心线程数，默认：2
     */
    private int corePoolSize = 2;
    /**
     * 线程池最大数量，默认：100
     */
    private int maxPoolSize = 100;
    /**
     * 线程池队列容量，默认：10000
     */
    private int queueCapacity = 10000;
    /**
     * 空闲线程存活时间，默认：300秒
     */
    private int keepAliveSeconds = 300;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}