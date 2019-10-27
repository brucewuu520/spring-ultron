package org.springultron.boot.servlet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springultron.boot.config.UltronAutoConfiguration;
import org.springultron.boot.props.UltronUploadProperties;

/**
 * 文件上传配置
 *
 * @author brucewuu
 * @date 2019/10/8 09:58
 */
@Configuration
@AutoConfigureAfter(UltronAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UltronUploadConfiguration implements WebMvcConfigurer {

    private final UltronUploadProperties properties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public UltronUploadConfiguration(UltronUploadProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = properties.getSavePath();
        String pattern = properties.getUploadPathPattern();
        registry.addResourceHandler(properties.getUploadPathPattern())
                .addResourceLocations("file:" + path + pattern.replace("*", ""));
    }
}