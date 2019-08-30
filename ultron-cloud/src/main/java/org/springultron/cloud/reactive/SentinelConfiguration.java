package org.springultron.cloud.reactive;

import com.alibaba.csp.sentinel.adapter.spring.webflux.SentinelWebFluxFilter;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.WebFluxCallbackManager;
import com.alibaba.csp.sentinel.adapter.spring.webflux.exception.SentinelBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;

import java.util.Collections;
import java.util.List;

/**
 * Sentinel 配置类
 *
 * @Auther: brucewuu
 * @Date: 2019-08-11 11:22
 * @Description:
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class SentinelConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public SentinelConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
        // 限流、熔断统一处理类
        WebFluxCallbackManager.setBlockHandler((exchange, throwable) -> {
            ApiResult result = ApiResult.failed(ResultCode.FLOW_LIMITING);
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .syncBody(result);
        });
    }

    @Bean
    @Order(-1)
    public SentinelBlockExceptionHandler sentinelBlockExceptionHandler() {
        // Register the block exception handler for Spring WebFlux.
        return new SentinelBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public SentinelWebFluxFilter sentinelWebFluxFilter() {
        // Register the Sentinel WebFlux filter.
        return new SentinelWebFluxFilter();
    }
}
