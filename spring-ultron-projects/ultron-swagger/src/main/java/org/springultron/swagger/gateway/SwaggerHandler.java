package org.springultron.swagger.gateway;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;

/**
 * Swagger Ui Config
 * @author brucewuu
 * @date 2020/10/1 下午3:48
 */
public class SwaggerHandler {

    private final SwaggerResourcesProvider swaggerResources;
    private final UiConfiguration uiConfiguration;
    private final SecurityConfiguration securityConfiguration;

    public SwaggerHandler(SwaggerResourcesProvider swaggerResources, UiConfiguration uiConfiguration, SecurityConfiguration securityConfiguration) {
        this.swaggerResources = swaggerResources;
        this.uiConfiguration = uiConfiguration;
        this.securityConfiguration = securityConfiguration;
    }

    @NonNull
    public Mono<ServerResponse> swaggerResources(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(swaggerResources.get()));
    }

    @NonNull
    public Mono<ServerResponse> uiConfiguration(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(uiConfiguration));
    }

    @NonNull
    public Mono<ServerResponse> securityConfiguration(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(securityConfiguration));
    }
}
