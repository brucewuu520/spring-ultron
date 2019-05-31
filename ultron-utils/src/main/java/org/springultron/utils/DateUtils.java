package org.springultron.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    /**
     * 日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(LocalDateTime date) {
        return date.format(FORMATTER);
    }

    public static String formatDateTime(LocalDateTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 字符串转日期
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateTime() {
        return LocalDateTime.now().format(FORMATTER);
    }

}
