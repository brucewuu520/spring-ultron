package org.springultron.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springultron.swagger.knife4j.EnableKnife4j;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Swagger自动化配置
 *
 * @author brucewuu
 * @date 2019-06-27 19:52
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableKnife4j
@Import({
        OpenApiDocumentationConfiguration.class,
        Swagger2DocumentationConfiguration.class,
        BeanValidatorPluginsConfiguration.class
})
@AutoConfigureAfter({
        WebMvcAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class
})
public class SwaggerConfiguration {

    private final SwaggerProperties swaggerProperties;
    private final ObjectProvider<OpenApiExtensionResolver> openApiExtensionResolver;

    @Autowired
    public SwaggerConfiguration(SwaggerProperties swaggerProperties, ObjectProvider<OpenApiExtensionResolver> openApiExtensionResolver) {
        this.swaggerProperties = swaggerProperties;
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .groupName(swaggerProperties.getGroupName())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
        if (swaggerProperties.getAuthorization().isEnabled()) {
            docket.securitySchemes(Collections.singletonList(apiKey()));
            docket.securityContexts(Collections.singletonList(securityContext()));
        }
        openApiExtensionResolver.ifAvailable(resolver -> docket.extensions(resolver.buildExtensions("MarkDown Docs")));
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(Optional.ofNullable(swaggerProperties.getTitle()).orElse("Api Documentation"))
                .description(Optional.ofNullable(swaggerProperties.getDescription()).orElse("Api Documentation"))
                .version(swaggerProperties.getVersion())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(Optional.ofNullable(swaggerProperties.getContact()).map(contact -> new Contact(contact.getName(), contact.getUrl(), contact.getEmail())).orElse(ApiInfo.DEFAULT_CONTACT))
                .build();
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return {ApiKey}
     */
    private ApiKey apiKey() {
        return Optional.of(swaggerProperties.getAuthorization())
                .map(authorization -> new ApiKey(authorization.getName(),
                        authorization.getHeaderName(),
                        ApiKeyVehicle.HEADER.getValue())).get();
    }

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
     * 其中 securityReferences 为配置启用的鉴权策略
     *
     * @return {SecurityContext}
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> operationContext.requestMappingPattern().matches(swaggerProperties.getAuthorization().getAuthRegex()))
                .build();
    }

    /**
     * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
     *
     * @return {List<SecurityReference>}
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(SecurityReference.builder()
                .reference(swaggerProperties.getAuthorization().getName())
                .scopes(authorizationScopes).build());
    }
}
