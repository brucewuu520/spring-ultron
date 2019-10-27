package org.springultron.boot.servlet.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springultron.core.convert.EnumToStringConverter;
import org.springultron.core.convert.StringToEnumConverter;

/**
 * API Jackson Enum and String convert eachother configurer
 *
 * @author brucewuu
 * @date 2019-08-12 12:13
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UltronConverterConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EnumToStringConverter());
        registry.addConverter(new StringToEnumConverter());
    }
}
