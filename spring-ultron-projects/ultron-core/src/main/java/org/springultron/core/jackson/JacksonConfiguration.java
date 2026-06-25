package org.springultron.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.json.JsonMapper;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson自动化配置
 *
 * @author brucewuu
 * @date 2019-06-01 18:25
 */
@ConditionalOnClass({JsonMapper.class})
@AutoConfiguration(before = {JacksonAutoConfiguration.class})
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
    public JsonMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.defaultLocale(locale == null ? Locale.getDefault() : locale)
                   .defaultTimeZone(timeZone == null ? TimeZone.getTimeZone(ZoneId.systemDefault()) : timeZone)
                   .defaultDateFormat(new SimpleDateFormat(dateFormat))
                   .changeDefaultPropertyInclusion(
                           (handler) -> handler.withValueInclusion(defaultPropertyInclusion)
                                               .withContentInclusion(defaultPropertyInclusion)
                   )
                   .addModule(new UltronJavaTimeModule());
        };
    }

}