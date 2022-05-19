package org.springultron.logging;

import ch.qos.logback.core.util.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * logging 日志配置项
 *
 * @author brucewuu
 * @date 2021/4/9 上午10:30
 */
@ConfigurationProperties(prefix = UltronLoggingProperties.PREFIX)
public class UltronLoggingProperties {
    public static final String PREFIX = "ultron.logging";

    /**
     * 是否关闭控制台日志输出
     * 默认开启
     */
    private boolean closeConsole = false;

    /**
     * 是否关闭日期文件输出
     * 默认开启
     */
    private boolean closeFile = false;

    /**
     * 是否开启traceId
     * 默认不开启
     * <p>
     * 配置文件加入以下配置:
     * logging.pattern.console="%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr([%X{traceId}]){yellow} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n"
     * logging.pattern.file="%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] [%X{traceId}] %-40.40logger{39} : %m%n"
     * </p>
     */
    private boolean enableTraceId = false;

    /**
     * Logstash配置
     */
    @NestedConfigurationProperty
    private Logstash logstash = new Logstash();

    public boolean isCloseConsole() {
        return closeConsole;
    }

    public void setCloseConsole(boolean closeConsole) {
        this.closeConsole = closeConsole;
    }

    public boolean isCloseFile() {
        return closeFile;
    }

    public void setCloseFile(boolean closeFile) {
        this.closeFile = closeFile;
    }

    public boolean isEnableTraceId() {
        return enableTraceId;
    }

    public void setEnableTraceId(boolean enableTraceId) {
        this.enableTraceId = enableTraceId;
    }

    public Logstash getLogstash() {
        return logstash;
    }

    public void setLogstash(Logstash logstash) {
        this.logstash = logstash;
    }

    /**
     * Logstash 配置项
     */
    public static class Logstash {
        public static final String PREFIX = UltronLoggingProperties.PREFIX + ".logstash";

        /**
         * 是否开启 logstash 日志收集
         * 默认关闭
         */
        private boolean enabled = false;

        /**
         * logstash输出地址，多个用逗号隔开
         * 默认 localhost:4560
         */
        private String destinations = "localhost:4560";

        /**
         * logstash 队列大小
         * 默认: 512
         */
        private int ringBufferSize = 512;

        /**
         * 写缓冲区大小，为0则禁用缓冲区
         * 默认: 8192
         */
        private int writeBufferSize = 8192;

        /**
         * keepAlive时长
         */
        private Duration keepAliveDuration;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDestinations() {
            return destinations;
        }

        public void setDestinations(String destinations) {
            this.destinations = destinations;
        }

        public int getRingBufferSize() {
            return ringBufferSize;
        }

        public void setRingBufferSize(int ringBufferSize) {
            this.ringBufferSize = ringBufferSize;
        }

        public int getWriteBufferSize() {
            return writeBufferSize;
        }

        public void setWriteBufferSize(int writeBufferSize) {
            this.writeBufferSize = writeBufferSize;
        }

        public Duration getKeepAliveDuration() {
            return keepAliveDuration;
        }

        public void setKeepAliveDuration(Duration keepAliveDuration) {
            this.keepAliveDuration = keepAliveDuration;
        }
    }
}
