package org.springultron.logging.appender;

import ch.qos.logback.classic.LoggerContext;

/**
 * LoggingAppender 接口抽象
 *
 * @author brucewuu
 * @date 2021/4/9 下午1:44
 */
public interface ILoggingAppender {
    /**
     * 启动
     *
     * @param context LoggerContext
     */
    void start(LoggerContext context);

    /**
     * 重置
     *
     * @param context LoggerContext
     */
    void reset(LoggerContext context);
}
