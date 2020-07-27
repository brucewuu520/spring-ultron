package org.springultron.boot.reactive.context;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * ReactiveRequestContextHolder
 *
 * @author brucewuu
 * @date 2020/7/26 21:24
 */
public class ReactiveRequestContextHolder {
    private static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    public static Mono<ServerWebExchange> getExchange() {
        return Mono.subscriberContext().map(ctx -> ctx.get(CONTEXT_KEY));
    }

    public static Mono<ServerHttpRequest> getRequest() {
        return ReactiveRequestContextHolder.getExchange().map(ServerWebExchange::getRequest);
    }

    public static Context put(Context context, ServerWebExchange exchange) {
        return context.put(CONTEXT_KEY, exchange);
    }
}
