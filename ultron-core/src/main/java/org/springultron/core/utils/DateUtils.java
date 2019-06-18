package org.springultron.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间日期转换工具
 */
public class DateUtils {

    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);

    /**
     * 日期转字符串
     *
     * @param date 日期时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(LocalDateTime date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符串转日期
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

}
