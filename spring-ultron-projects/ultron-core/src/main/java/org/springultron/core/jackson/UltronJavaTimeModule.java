package org.springultron.core.jackson;

import org.springultron.core.utils.DateUtils;
import tools.jackson.core.json.PackageVersion;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * java 8 时间默认序列化
 *
 * @author brucewuu
 * @date 2019-06-13 17:25
 */
public final class UltronJavaTimeModule extends SimpleModule {

    public UltronJavaTimeModule() {
        super("time", PackageVersion.VERSION);
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateUtils.DATE_FORMATTER));
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtils.TIME_FORMATTER));
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtils.DATE_TIME_FORMATTER));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtils.DATE_FORMATTER));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtils.TIME_FORMATTER));
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtils.DATE_TIME_FORMATTER));
    }
}
