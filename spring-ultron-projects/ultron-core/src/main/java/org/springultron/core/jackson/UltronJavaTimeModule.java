package org.springultron.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springultron.core.utils.DateUtils;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 1L;

    public UltronJavaTimeModule() {
        super(PackageVersion.VERSION);
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtils.DATE_TIME_FORMATTER));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtils.DATE_FORMATTER));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtils.TIME_FORMATTER));
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtils.DATE_TIME_FORMATTER));
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateUtils.DATE_FORMATTER));
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtils.TIME_FORMATTER));
    }
}
