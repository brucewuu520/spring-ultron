package org.springultron.boot.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * spring boot异步任务线程池配置
 *
 * @author brucewuu
 * @date 2019/11/11 14:31
 */
@ConfigurationProperties(prefix = "ultron.task")
public class UltronTaskProperties {
    /**
     * 核心线程数，默认：3
     */
    private int corePoolSize = 3;
    /**
     * 线程池最大数量，默认：300
     */
    private int maxPoolSize = 300;
    /**
     * 线程池队列容量，默认：30000
     */
    private int queueCapacity = 30000;
    /**
     * 空闲线程存活时间，默认：300秒
     */
    private Duration keepAlive = Duration.ofSeconds(300L);
    /**
     * 线程名称前缀
     */
    private String threadNamePrefix = "ultron";

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

    public Duration getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Duration keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
}