package org.springultron.boot.reactive.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UriComponentsBuilder;
import org.springultron.boot.error.UltronErrorEvent;
import org.springultron.boot.reactive.context.ReactiveRequestContextHolder;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.exception.ServiceException;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultStatus;
import org.springultron.core.utils.ObjectUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 通用自定义异常、未知异常处理
 *
 * @author brucewuu
 * @date 2019-06-05 12:23
 */
@Order
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ApplicationEventPublisher publisher;

    @Autowired
    public GlobalExceptionHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 自定义 REST 业务异常处理
     *
     * @param e 业务异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = ServiceException.class)
    public Mono<ApiResult<Object>> handleApiException(ServiceException e) {
        log.error("自定义业务异常", e);

        // 发送：未知异常异常事件
        return ReactiveRequestContextHolder.getRequest()
                .doOnSuccess(r -> publishEvent(r, e))
                .flatMap(r -> Mono.just(ApiResult.apiException(e)));
    }

    /**
     * 加解密异常处理
     *
     * @param e 加解密异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = CryptoException.class)
    public Mono<ApiResult<Object>> handleCryptoException(CryptoException e) {
        log.error("加解密异常", e);
        // 发送：未知异常异常事件
        return ReactiveRequestContextHolder.getRequest()
                .doOnSuccess(r -> publishEvent(r, e))
                .flatMap(r -> Mono.just(ApiResult.failed(ResultStatus.SIGN_FAILED)));
    }

    @ExceptionHandler(AssertionError.class)
    public Mono<ApiResult<Object>> handleAssertionError(AssertionError e) {
        log.error("断言异常", e);
        // 发送：未知异常异常事件
        return ReactiveRequestContextHolder.getRequest()
                .doOnSuccess(r -> publishEvent(r, e))
                .flatMap(r -> Mono.just(ApiResult.failed(ResultStatus.ASSERTION_ERROR.getCode(), e.getMessage())));
    }

    private void publishEvent(ServerHttpRequest request, Throwable error) {
        UltronErrorEvent event = new UltronErrorEvent();
        // 请求方法名
        event.setRequestMethod(request.getMethodValue());
        // 拼接地址
        String path = request.getPath().pathWithinApplication().value();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String requestUrl = UriComponentsBuilder.fromPath(path).queryParams(queryParams).build().toUriString();
        event.setRequestUrl(requestUrl);
        // 打印堆栈信息
        event.setStackTrace(Exceptions.getStackTraceString(error));
        event.setExceptionName(error.getClass().getName());
        event.setMessage(error.getMessage());
        event.setCreatedAt(LocalDateTime.now());
        StackTraceElement[] elements = error.getStackTrace();
        if (ObjectUtils.isNotEmpty(elements)) {
            // 打印控制器类信息
            StackTraceElement element = elements[0];
            event.setClassName(element.getClassName());
            event.setFileName(element.getFileName());
            event.setMethodName(element.getMethodName());
            event.setLineNumber(element.getLineNumber());
        }
        // 发布事件
        publisher.publishEvent(event);
    }
}