package org.springultron.boot.reactive.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springultron.core.exception.ApiException;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;
import reactor.core.publisher.Mono;

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

    /**
     * 自定义 REST 业务异常处理
     *
     * @param e 业务异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = ApiException.class)
    public Mono<ApiResult> handleApiException(ApiException e) {
        log.error("自定义业务异常: {}", e.getMessage());
        return Mono.just(ApiResult.apiException(e));
    }

    @ExceptionHandler(AssertionError.class)
    public ApiResult handleAssertionError(AssertionError e) {
        log.error("断言异常: {}", e.getMessage());
        // 发送：未知异常异常事件
        return ApiResult.failed(ResultCode.ASSERTION_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public Mono<ApiResult> handleThrowable(Throwable e) {
        log.error("系统异常", e);
        return Mono.just(ApiResult.failed(e.getMessage()));
    }
}