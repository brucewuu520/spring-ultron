package org.springultron.logging.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import org.springultron.logging.appender.ILoggingAppender;

import java.util.List;

/**
 * Logback configuration is achieved by configuration file and API.
 * When configuration file change is detected, the configuration is reset.
 * This listener ensures that the programmatic configuration is also re-applied after reset.
 *
 * @author brucewuu
 * @date 2021/4/9 上午10:09
 */
public class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
    private final List<ILoggingAppender> appenderList;

    public LogbackLoggerContextListener(List<ILoggingAppender> appenderList) {
        this.appenderList = appenderList;
    }

    @Override
    public boolean isResetResistant() {
        return true;
    }

    @Override
    public void onStart(LoggerContext context) {
        for (ILoggingAppender loggingAppender : appenderList) {
            loggingAppender.start(context);
        }
    }

    @Override
    public void onReset(LoggerContext context) {
        for (ILoggingAppender loggingAppender : appenderList) {
            loggingAppender.reset(context);
        }
    }

    @Override
    public void onStop(LoggerContext context) {

    }

    @Override
    public void onLevelChange(Logger logger, Level level) {

    }
}
