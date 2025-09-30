package org.springultron.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson自动化配置
 *
 * @author brucewuu
 * @date 2019-06-01 18:25
 */
@AutoConfiguration
@ConditionalOnClass({ObjectMapper.class})
@AutoConfigureBefore({JacksonAutoConfiguration.class})
public class JacksonConfiguration {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Value("${spring.jackson.default-property-inclusion:NON_NULL}")
    private JsonInclude.Include defaultPropertyInclusion;

    @Value("${spring.jackson.locale}")
    private Locale locale;

    @Value("${spring.jackson.time-zone}")
    private TimeZone timeZone;

    @Primary
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(locale == null ? Locale.getDefault() : locale);
            builder.timeZone(timeZone == null ? TimeZone.getTimeZone(ZoneId.systemDefault()) : timeZone);
            builder.simpleDateFormat(dateFormat);
            // 序列化时默认过滤为null的字段
            builder.serializationInclusion(defaultPropertyInclusion);
            builder.modules(new UltronJavaTimeModule());
        };
    }
}