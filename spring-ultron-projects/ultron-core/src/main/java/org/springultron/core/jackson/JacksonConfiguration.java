package org.springultron.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ObjectMapper.class})
@AutoConfigureBefore({JacksonAutoConfiguration.class})
public class JacksonConfiguration {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Value("${spring.jackson.default-property-inclusion:NON_NULL}")
    private JsonInclude.Include defaultPropertyInclusion;

    @Primary
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.getDefault());
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(dateFormat);
            // 序列化时过滤为null的字段
            builder.serializationInclusion(defaultPropertyInclusion);
            builder.modules(new UltronJavaTimeModule());
        };
    }
}