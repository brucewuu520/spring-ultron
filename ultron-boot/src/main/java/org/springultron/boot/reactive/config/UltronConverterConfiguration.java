package org.springultron.boot.reactive.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springultron.core.convert.EnumToStringConverter;
import org.springultron.core.convert.StringToEnumConverter;

/**
 * API Jackson Enum and String convert eachother configurer
 *
 * @author brucewuu
 * @date 2019-08-12 12:29
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class UltronConverterConfiguration implements WebFluxConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EnumToStringConverter());
        registry.addConverter(new StringToEnumConverter());
    }
}
