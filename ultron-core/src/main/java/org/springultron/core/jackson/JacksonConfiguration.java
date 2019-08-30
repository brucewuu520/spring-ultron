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
 * @Auther: brucewuu
 * @Date: 2019-06-01 18:25
 * @Description:
 */
@Configuration
@ConditionalOnClass({ObjectMapper.class})
@AutoConfigureBefore({JacksonAutoConfiguration.class})
public class JacksonConfiguration {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(dateFormat);
            // 序列化时过滤为null的字段
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            // 注意不处理 featuresToEnable 和 featuresToDisable 避免安全问题
            builder.modules(new UltronJavaTimeModule());
        };
    }
}
