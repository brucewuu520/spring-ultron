package org.springultron.swagger.gateway;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import springfox.documentation.swagger.web.*;

/**
 * Swagger Router
 *
 * @author brucewuu
 * @date 2020/10/2 上午8:15
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(GatewayAutoConfiguration.class)
@ConditionalOnBean(SwaggerResourcesProvider.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class SwaggerRouter {

    private final SwaggerHandler swaggerHandler;

    @Autowired
    public SwaggerRouter(SwaggerResourcesProvider swaggerResources, ObjectProvider<UiConfiguration> uiConfiguration, ObjectProvider<SecurityConfiguration> securityConfiguration) {
        this.swaggerHandler = new SwaggerHandler(swaggerResources,
                uiConfiguration.getIfAvailable(() -> UiConfigurationBuilder.builder().build()),
                securityConfiguration.getIfAvailable(() -> SecurityConfigurationBuilder.builder().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/swagger-resources", swaggerHandler::swaggerResources)
                .GET("/swagger-resources/configuration/ui", swaggerHandler::uiConfiguration)
                .GET("/swagger-resources/configuration/security", swaggerHandler::securityConfiguration)
                .build();
    }
}
