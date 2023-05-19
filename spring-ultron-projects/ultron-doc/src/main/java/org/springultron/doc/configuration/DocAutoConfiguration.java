package org.springultron.doc.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.RandomUtils;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 文档自动化配置
 *
 * @author brucewuu
 * @date 2023/5/6 17:00
 */
@AutoConfiguration(before = {SpringDocConfiguration.class})
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SpringDocProperties.class)
public class DocAutoConfiguration {

    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-order", RandomUtils.nextInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
                openApi.addExtension("x-test123", "333");
                openApi.getPaths().addExtension("x-abb", RandomUtils.nextInt(1, 100));
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI(SpringDocProperties springDocProperties) {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ).addParameters("Authorization",
                                new Parameter()
                                        .in("header")
                                        .schema(new StringSchema())
                                        .name("TokenHeader")
                        )
                )
                .info(springDocProperties.getInfo());
    }

}
