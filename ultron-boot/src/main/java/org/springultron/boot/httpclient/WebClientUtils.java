package org.springultron.boot.httpclient;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

/**
 * 基于WebClient的HTTP请求封装
 * Spring boot官方创建并预配置了一个WebClient.Builder
 * 建议在组件中注入WebClient.Builder webClientBuilder来使用WebClient实例
 * <p>
 * private final WebClient webClient;
 * <p>
 * public MyService(WebClient.Builder webClientBuilder) {
 * this.webClient = webClientBuilder.baseUrl("https://example.org").build();
 * }
 * </P>
 *
 * @author brucewuu
 * @date 2019-06-17 19:00
 */
public final class WebClientUtils {

    private WebClientUtils() {
    }

    private static volatile WebClient INSTANCE;

    public static WebClient getInstance() {
        WebClient webClient = INSTANCE;
        if (null == webClient) {
            synchronized (WebClientUtils.class) {
                webClient = INSTANCE;
                if (null == webClient) {
                    webClient = WebClient.create();
                    WebClientUtils.INSTANCE = webClient;
                }
            }
        }
        return webClient;
    }

    public static void setInstance(WebClient.Builder builder) {
        WebClientUtils.INSTANCE = builder.build();
    }

    /**
     * 发起GET请求
     *
     * @param targetUrl  请求URL
     * @param returnType 返回值类型
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(String targetUrl, Class<T> returnType) {
        return getInstance()
                .get()
                .uri(targetUrl)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起GET请求
     * <p>
     * UriComponentsBuilder
     * .fromUriString("https://example.com/hotels/query")
     * .queryParam("id", "123")
     * .queryParam("name", "J.W")
     * .build()
     * .toUri()
     * </p>
     *
     * <p>
     * UriComponentsBuilder
     * .fromUriString("https://example.com/hotels/{hotel}?q={q}")
     * .build("WestIn", "123");
     * </p>
     *
     * <p>
     * UriComponentsBuilder
     * .fromUriString("https://example.com/hotels/{hotel}")
     * .queryParam("q", "{q}")
     * .build("WestIn", "123");
     * </p>
     *
     * <p>
     * UriComponentsBuilder
     * .fromUriString("https://example.com/hotels/{hotel}")
     * .queryParam("q", "{q}")
     * .encode()
     * .build()
     * .expand("WestIn", "123")
     * .toUri();
     * </P>
     *
     * <p>
     * UriComponentsBuilder
     * .fromUriString("https://example.com/hotels/{hotel}")
     * .queryParam("q", "{q}")
     * .encode()
     * .buildAndExpand("WestIn", "123")
     * .toUri();
     * </p>
     *
     * @param uri        请求URI
     * @param returnType 返回值类型
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(URI uri, Class<T> returnType) {
        return getInstance()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起GET请求
     *
     * @param baseUrl      请求地址 "xx/persons/{id}/{name}"
     * @param uriVariables 请求pathVariable变量参数 id, name
     * @param returnType   返回值类型
     * @param <T>          返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(String baseUrl, Object[] uriVariables, Class<T> returnType) {
        return getInstance()
                .get()
                .uri(baseUrl, uriVariables)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起GET请求
     *
     * @param baseUrl      请求地址 "xx/persons/{id}/{name}"
     * @param uriVariables uriVariables url路径参数(id=1,name=tom)
     * @param returnType   返回值类型
     * @param <T>          返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(String baseUrl, Map<String, ?> uriVariables, Class<T> returnType) {
        return getInstance()
                .get()
                .uri(baseUrl, uriVariables)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起GET请求
     *
     * @param targetUrl  请求URL
     * @param returnType 返回值类型
     * @param headerName Header Name
     * @param headers    Header Value
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(String targetUrl, Class<T> returnType, String headerName, String... headers) {
        return getInstance()
                .get()
                .uri(targetUrl)
                .header(headerName, headers)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起GET请求
     *
     * @param targetUrl  请求URL
     * @param headerMap  Headers
     * @param returnType 返回值类型
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(final String targetUrl, final MultiValueMap<String, String> headerMap, final Class<T> returnType) {
        return getInstance()
                .get()
                .uri(targetUrl)
                .headers(headers -> headers.addAll(headerMap))
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起GET请求
     *
     * @param baseUrl      请求地址 "../persons/{id}/{name}"
     * @param uriVariables url路径参数(id=1,name=tom)
     * @param returnType   返回值类型
     * @param headerName   Header Name
     * @param headers      Header Value
     * @param <T>          返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> get(String baseUrl, Map<String, ?> uriVariables, Class<T> returnType, String headerName, String... headers) {
        return getInstance()
                .get()
                .uri(baseUrl, uriVariables)
                .header(headerName, headers)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起POST请求（contentType: application/json;charset=UTF-8）
     *
     * @param targetUrl  请求地址
     * @param reqBody    请求body
     * @param returnType 返回值类型
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> postJSON(String targetUrl, Object reqBody, Class<T> returnType) {
        return getInstance()
                .post()
                .uri(targetUrl)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(reqBody)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起POST请求（contentType: application/json;charset=UTF-8）
     *
     * @param baseUrl    请求地址
     * @param reqBody    请求body
     * @param returnType 返回值类型
     * @param headerName Header Name
     * @param headers    Header Value
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> postJSON(String baseUrl, Object reqBody, Class<T> returnType, String headerName, String... headers) {
        return getInstance()
                .post()
                .uri(baseUrl)
                .header(headerName, headers)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(reqBody)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起POST请求（contentType: application/json;charset=UTF-8）
     *
     * @param baseUrl    请求地址
     * @param headerMap  headers
     * @param reqBody    请求body
     * @param returnType 返回值类型
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> postJSON(final String baseUrl, final MultiValueMap<String, String> headerMap, final Object reqBody, final Class<T> returnType) {
        return getInstance()
                .post()
                .uri(baseUrl)
                .headers(headers -> headers.addAll(headerMap))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(reqBody)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起POST 表单请求（contentType: application/x-www-form-urlencoded）
     *
     * @param baseUrl      请求地址
     * @param formInserter 表单参数
     * @param returnType   返回值类型
     * @param <T>          返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> postForm(String baseUrl, BodyInserters.FormInserter<String> formInserter, Class<T> returnType) {
        return getInstance()
                .post()
                .uri(baseUrl)
                .body(formInserter)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起POST 表单请求（contentType: application/x-www-form-urlencoded）
     *
     * @param baseUrl      请求地址
     * @param headerMap    headers
     * @param formInserter 表单参数
     * @param returnType   返回值类型
     * @param <T>          返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> postForm(final String baseUrl, final MultiValueMap<String, String> headerMap, final BodyInserters.FormInserter<String> formInserter, final Class<T> returnType) {
        return getInstance()
                .post()
                .uri(baseUrl)
                .headers(headers -> headers.addAll(headerMap))
                .body(formInserter)
                .retrieve()
                .bodyToMono(returnType);
    }

    /**
     * 发起POST 表单请求（contentType: application/x-www-form-urlencoded）
     *
     * @param baseUrl    请求地址
     * @param formData   表单参数
     * @param returnType 返回值类型
     * @param <T>        返回值泛型
     * @return 返回值Mono对象
     */
    public static <T> Mono<T> postForm(String baseUrl, MultiValueMap<String, String> formData, Class<T> returnType) {
        return getInstance()
                .post()
                .uri(baseUrl)
                .syncBody(formData)
                .retrieve()
                .bodyToMono(returnType);
    }
}
