package org.springultron.boot.reactive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springultron.boot.props.UltronUploadProperties;

/**
 * 文件上传配置
 *
 * @author brucewuu
 * @date 2019/10/8 09:58
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(UltronUploadProperties.class)
public class UltronUploadConfigurer implements WebFluxConfigurer {

    private final UltronUploadProperties properties;

    @Autowired
    public UltronUploadConfigurer(UltronUploadProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = properties.getSavePath();
        String pattern = properties.getUploadPathPattern();
        registry.addResourceHandler(pattern)
                .addResourceLocations("file:" + path + pattern.replace("*", ""));
    }
}