package org.springultron.logging.listener;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springultron.logging.UltronLoggingProperties;
import org.springultron.logging.utils.LoggingUtils;

/**
 * 应用启动完毕监听
 *
 * @author brucewuu
 * @date 2021/4/9 上午10:15
 */
public class LoggingStartedEventListener {
    private final UltronLoggingProperties properties;

    public LoggingStartedEventListener(UltronLoggingProperties properties) {
        this.properties = properties;
    }

    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart() {
        // 关闭控制台日志输出
        if (properties.isCloseConsole()) {
            LoggingUtils.detachAppender(LoggingUtils.CONSOLE_APPENDER_NAME);
        }
        // 关闭日志文件输出
        if (properties.isCloseFile()) {
            LoggingUtils.detachAppender(LoggingUtils.FILE_INFO_APPENDER_NAME);
            LoggingUtils.detachAppender(LoggingUtils.FILE_ERROR_APPENDER_NAME);
        }
    }
}
