package org.springultron.boot.reactive.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springultron.boot.error.BaseExceptionHandler;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Optional;

/**
 * WebFlux Restful API 异常信息处理
 *
 * @author brucewuu
 * @date 2019-06-17 11:50
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class RestExceptionHandler extends BaseExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 参数校验失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ApiResult<Object>> handleError(MethodArgumentNotValidException e) {
        log.error("参数校验失败", e);
        return Mono.just(handleError(e.getBindingResult()));
    }

    /**
     * 参数绑定失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(BindException.class)
    public Mono<ApiResult<Object>> handleError(BindException e) {
        log.error("参数绑定失败", e);
        return Mono.just(handleError(e.getBindingResult()));
    }

    /**
     * 参数绑定失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ApiResult<Object>> handleError(WebExchangeBindException e) {
        log.error("参数绑定失败", e);
        return Mono.just(handleError(e.getBindingResult()));
    }

    /**
     * 缺少必要的请求参数
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ApiResult<Object>> handleError(ServerWebInputException e) {
        log.error("缺少必要的请求参数", e);
        return Mono.just(Optional.ofNullable(e.getMethodParameter())
                .map(parameter -> ApiResult.fail(ResultCode.PARAM_MISS.getCode(), String.format("缺少必要的请求参数: %s", parameter.getParameterName())))
                .orElseGet(() -> ApiResult.fail(ResultCode.PARAM_MISS.getCode(), "缺少必要的请求参数")));
    }

    /**
     * 参数校验异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ValidationException.class)
    public Mono<ApiResult<Object>> handleError(ValidationException e) {
        log.error("参数校验异常", e);
        Throwable cause = e.getCause();
        return Mono.just(ApiResult.fail(ResultCode.PARAM_VALID_FAILED.getCode(), cause == null ? e.getMessage() : cause.getMessage()));
    }

    /**
     * 参数验证失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ApiResult<Object>> handleError(ConstraintViolationException e) {
        log.error("参数验证失败", e);
        return Mono.just(handleError(e.getConstraintViolations()));
    }

    /**
     * 响应状态异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ApiResult<Object>> handleError(ResponseStatusException e) {
        log.error("响应状态异常", e);
        return Mono.just(ApiResult.fail(ResultCode.BAD_REQUEST.getCode(), String.format("响应状态异常:%s", e.getReason())));
    }

    /**
     * 文件上传异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MultipartException.class)
    public Mono<ApiResult<Object>> handleError(MultipartException e) {
        log.error("文件太大", e);
        return Mono.just(ApiResult.fail(ResultCode.PAYLOAD_TOO_LARGE));
    }
}