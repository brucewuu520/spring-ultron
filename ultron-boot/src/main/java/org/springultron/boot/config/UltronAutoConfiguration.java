package org.springultron.boot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springultron.boot.props.UltronLogProperties;

/**
 * 自动配置
 *
 * @Auther: brucewuu
 * @Date: 2019-06-29 20:56
 * @Description:
 */
@Configuration
@EnableConfigurationProperties({UltronLogProperties.class})
public class UltronAutoConfiguration {

}
