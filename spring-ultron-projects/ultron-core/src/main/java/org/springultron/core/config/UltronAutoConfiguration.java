package org.springultron.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springultron.core.utils.SpringUtils;

/**
 * 自动配置
 *
 * @author brucewuu
 * @date 2019-06-29 20:56
 */
@Configuration(proxyBeanMethods = false)
public class UltronAutoConfiguration {

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

}