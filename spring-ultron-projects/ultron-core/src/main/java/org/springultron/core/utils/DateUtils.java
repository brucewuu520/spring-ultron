package org.springultron.core.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * Java8时间日期转换工具
 *
 * @author brucewuu
 * @date @date 2019-06-10 16:30
 */
public class DateUtils {

    private DateUtils() {
    }

    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_TIME);

    /**
     * 格式化日期时间
     *
     * @param dateTime 日期时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    /**
     * 格式化日期时间
     *
     * @param dateTime 日期时间
     * @param pattern  格式化参数
     * @return 日期时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }

    /**
     * 字符串转日期时间
     *
     * @param dateTime 日期时间字符串: yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        if (StringUtils.isEmpty(dateTime)) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    /**
     * 字符串转日期时间
     *
     * @param dateTime 日期时间字符串: 符合pattern格式
     * @param pattern  格式化参数
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime, String pattern) {
        if (StringUtils.isEmpty(dateTime)) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateTime() {
        return DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 格式化日期
     *
     * @param localDate 日期
     * @return yyyy-MM-dd
     */
    public static String formatDate(LocalDate localDate) {
        return DATE_FORMATTER.format(localDate);
    }

    /**
     * 格式化日期
     *
     * @param localDate 日期
     * @param pattern   格式化参数
     * @return 日期字符串
     */
    public static String formatDate(LocalDate localDate, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    /**
     * 字符串转日期
     *
     * @param date 日期字符串: yyyy-MM-dd
     * @return LocalDate
     */
    public static LocalDate parseDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    /**
     * 字符串转日期
     *
     * @param date    日期字符串: 符合pattern格式
     * @param pattern 格式化参数
     * @return LocalDate
     */
    public static LocalDate parseDate(String date, String pattern) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化时间
     *
     * @param localTime 时间
     * @return HH:mm:ss
     */
    public static String formatTime(LocalTime localTime) {
        return TIME_FORMATTER.format(localTime);
    }

    /**
     * 格式化时间
     *
     * @param localTime 时间
     * @param pattern   格式化参数
     * @return 时间字符串
     */
    public static String formatTime(LocalTime localTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localTime);
    }

    /**
     * 字符串转时间
     *
     * @param time 时间字符串: HH:mm:ss
     * @return LocalTime
     */
    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, TIME_FORMATTER);
    }

    /**
     * 时间字符串转时间
     *
     * @param time    时间字符串: 符合pattern格式
     * @param pattern 格式化参数
     * @return LocalTime
     */
    public static LocalTime parseTime(String time, String pattern) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).parse(dateStr, query);
    }

    /**
     * Instant 转日期时间
     *
     * @param instant Instant
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 转换为java 8 日期时间
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 转换为java 8 日期时间
     *
     * @param milliseconds 毫秒数
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(final long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * 日期时间转 Instant
     *
     * @param dateTime 日期时间
     * @return Instant
     */
    public static Instant toInstant(final LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * LocalDateTime 转换为毫秒数
     *
     * @param dateTime LocalDateTime
     * @return 毫秒数
     */
    public static long toMilliseconds(final LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * LocalDate 转换为毫秒数
     *
     * @param localDate LocalDate
     * @return 毫秒数
     */
    public static long toMilliseconds(final LocalDate localDate) {
        return toMilliseconds(localDate.atStartOfDay());
    }

    /**
     * 计算两个时间（秒，纳秒）间隔
     *
     * @param startInclusive 开始时间
     * @param endExclusive   结束时间
     * @return Duration
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 计算两个日期（年月日）间隔的天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return long
     */
    public static long between(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 计算两个日期（年月日）间隔
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param unit      单位 {@link ChronoUnit}
     * @return long
     */
    public static long between(LocalDate startDate, LocalDate endDate, TemporalUnit unit) {
        return startDate.until(endDate, unit);
    }

    /**
     * 友好的显示时间
     *
     * @param dateTime 时间
     */
    public static String friendlyTime(LocalDateTime dateTime) {
        final long days = between(dateTime.toLocalDate(), LocalDate.now());
        final LocalTime localTime = dateTime.toLocalTime();
        if (days == 0) { // 今天
             Duration duration = between(localTime, LocalTime.now());
             long minutes = duration.toMinutes();
             if (minutes <= 1) {
                 return "刚刚";
             }
             if (minutes < 60) {
                 return minutes + "分钟前";
             }
             return "今天" + formatTime(localTime, "HH:mm");
        } else if (days == 1) {
            return "昨天" + formatTime(localTime, "HH:mm");
        }
        return formatDateTime(dateTime, "yyyy-MM-dd HH:mm");
    }

    public static String getWeek(LocalDate date) {
        return getWeek(date, null);
    }

    public static String getWeek(LocalDate date, String prefix) {
        final int week = date.getDayOfWeek().getValue();
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "一";
                break;
            case 2:
                weekStr = "二";
                break;
            case 3:
                weekStr = "三";
                break;
            case 4:
                weekStr = "四";
                break;
            case 5:
                weekStr = "五";
                break;
            case 6:
                weekStr = "六";
                break;
            case 7:
                weekStr = "日";
                break;
            default:
                break;
        }
        return (StringUtils.isEmpty(prefix) ? "星期" : prefix) + weekStr;
    }

}
