package org.springultron.http;

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * Slf4j 打印日志
 *
 * @Auther: brucewuu
 * @Date: 2019-06-30 16:23
 * @Description:
 */
public class Slf4jLogger implements HttpLoggingInterceptor.Logger {
    private static final Logger log = LoggerFactory.getLogger("HttpRequest");

    static final HttpLoggingInterceptor.Logger LOGGER = new Slf4jLogger();

    @Override
    public void log(@NonNull String message) {
        log.info(message);
    }
}
