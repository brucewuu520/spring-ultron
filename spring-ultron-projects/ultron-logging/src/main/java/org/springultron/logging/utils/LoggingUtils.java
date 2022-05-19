package org.springultron.logging.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * logging utils
 *
 * @author brucewuu
 * @date 2021/4/9 上午11:46
 */
public class LoggingUtils {
    public static final String DEFAULT_APP_NAME = "ultron-server";
    public static final String DEFAULT_LOG_DIR = "logs";

    public static final String CONSOLE_APPENDER_NAME = "CONSOLE";
    public static final String FILE_INFO_APPENDER_NAME = "INFO";
    public static final String FILE_ERROR_APPENDER_NAME = "ERROR";

    public static final String FILE_INFO_LOG = "info.log";
    public static final String FILE_ERROR_LOG = "error.log";

    /**
     * detach appender
     *
     * @param name appender name
     */
    public static void detachAppender(String name) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(name);
    }

}
