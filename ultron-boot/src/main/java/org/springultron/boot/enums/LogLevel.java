package org.springultron.boot.enums;

/**
 * 自定义日志等级
 *
 * @Auther: brucewuu
 * @Date: 2019-06-17 19:56
 * @Description:
 */
public enum LogLevel {
    /**
     * No logs.
     */
    NONE,
    /**
     * Logs request and response lines.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * }</pre>
     */
    BASIC,
    /**
     * Logs request and response lines and their respective headers.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * }</pre>
     */
    HEADERS,
    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * }</pre>
     */
    BODY;

    /**
     * 请求日志配置前缀
     */
    public static final String ULTRON_LOG_PROPS_PREFIX = "ultron.logger";
    /**
     * 请求日志是否启用
     */
    public static final String ULTRON_LOG_ENABLE = "ultron.logger.enable";
}
