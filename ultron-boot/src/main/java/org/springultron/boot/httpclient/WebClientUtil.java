package org.springultron.boot.httpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springultron.core.utils.StringUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebClientUtil {

    private static final Logger log = LoggerFactory.getLogger(WebClientUtil.class);

    private static volatile WebClient HTTP_CLIENT = WebClient.builder().filter(logFunction()).build();

    public static <T> Mono<T> get(String targetUrl, Class<T> returnType) {
        return HTTP_CLIENT
                .get()
                .uri(targetUrl)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> get(URI uri, Class<T> returnType) {
        return HTTP_CLIENT
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> get(String baseUrl, Map<String, ?> params, Class<T> returnType) {
        return HTTP_CLIENT
                .get()
                .uri(baseUrl, params)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> get(String targetUrl, Object[] uriVariables, Class<T> returnType) {
        return HTTP_CLIENT
                .get()
                .uri(targetUrl, uriVariables)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> get(String targetUrl, Class<T> returnType, String headerName, String... headers) {
        return HTTP_CLIENT
                .get()
                .uri(targetUrl)
                .header(headerName, headers)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> get(String baseUrl, Map<String, ?> params, Class<T> returnType, String headerName, String... headers) {
        return HTTP_CLIENT
                .get()
                .uri(baseUrl, params)
                .header(headerName, headers)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> postJSON(String baseUrl, Object request, Class<T> returnType) {
        return HTTP_CLIENT
                .post()
                .uri(baseUrl)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(request)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> postJSON(String baseUrl, Object request, Class<T> returnType, String headerName, String... headers) {
        return HTTP_CLIENT
                .post()
                .uri(baseUrl)
                .header(headerName, headers)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(request)
                .retrieve()
                .bodyToMono(returnType);
    }

    public static <T> Mono<T> postForm(String baseUrl, MultiValueMap<String, String> formData, Class<T> returnType) {
        return HTTP_CLIENT
                .post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .syncBody(formData)
                .retrieve()
                .bodyToMono(returnType);
    }

    private static ExchangeFilterFunction logFunction() {
        return (request, next) -> {
            // 记录日志
            String requestURI = request.url().toString();
            // 构建成一条长 日志，避免并发下日志错乱
            StringBuilder beforeReqLog = new StringBuilder(300);
            // 日志参数
            List<Object> beforeReqArgs = new ArrayList<>();
            beforeReqLog.append("\n\n================ WebClient Request Start  ================\n");
            // 打印路由
            beforeReqLog.append("===> {}: {}\n");
            // 参数
            String requestMethod = request.method().name();
            beforeReqArgs.add(requestMethod);
            beforeReqArgs.add(requestURI);
            HttpHeaders newHeaders = request.headers();
            newHeaders.forEach((headerName, headerValue) -> {
                beforeReqLog.append("===Headers===  {}: {}\n");
                beforeReqArgs.add(headerName);
                beforeReqArgs.add(StringUtils.join(headerValue));
            });

            beforeReqLog.append("================  WebClient Request End  =================\n");

            log.info(beforeReqLog.toString(), beforeReqArgs.toArray());

            return next.exchange(request);
        };
    }
}
