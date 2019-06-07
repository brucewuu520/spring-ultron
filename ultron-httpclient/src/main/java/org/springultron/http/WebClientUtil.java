package org.springultron.http;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

public class WebClientUtil {

    private static volatile WebClient HTTP_CLIENT = WebClient.create();

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
}
