package org.springultron.logging.utils;

import net.logstash.logback.stacktrace.ShortenedThrowableConverter;

/**
 * Logstash utils
 *
 * @author brucewuu
 * @date 2021/4/9 下午2:39
 */
public class LogstashUtils {

    public static ShortenedThrowableConverter throwableConverter() {
        final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);
        return throwableConverter;
    }

}
