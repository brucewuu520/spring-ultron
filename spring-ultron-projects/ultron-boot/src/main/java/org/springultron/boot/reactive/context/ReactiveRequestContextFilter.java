package org.springultron.boot.reactive.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * ReactiveRequestContextFilter
 *
 * @author brucewuu
 * @date 2020/7/26 21:22
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestContextFilter implements WebFilter, Ordered {

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).contextWrite(ctx -> ReactiveRequestContextHolder.put(ctx, exchange));
    }

    @Override
    public int getOrder() {
        return -999999;
    }
}
