package org.springultron.boot.reactive.config;

import io.netty.channel.ChannelOption;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * WebClient Config
 *
 * @author brucewuu
 * @date 2022/3/26 下午1:30
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebClient.class, HttpClient.class})
public class UltronWebClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "reactorNettyHttpClientMapper")
    public ReactorNettyHttpClientMapper reactorNettyHttpClientMapper() {
        return httpClient -> httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(15));
    }

}
