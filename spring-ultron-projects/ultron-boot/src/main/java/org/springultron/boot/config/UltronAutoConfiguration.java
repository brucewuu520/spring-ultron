package org.springultron.boot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springultron.boot.props.UltronLogProperties;
import org.springultron.boot.props.UltronTaskProperties;
import org.springultron.boot.props.UltronUploadProperties;

/**
 * 自动配置
 *
 * @author brucewuu
 * @date 2019-06-29 20:56
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
        UltronTaskProperties.class,
        UltronLogProperties.class,
        UltronUploadProperties.class
})
public class UltronAutoConfiguration {

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

}