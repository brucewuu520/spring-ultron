package org.springultron.boot.reactive.error;

import org.hibernate.validator.internal.engine.path.PathImpl;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * WebFlux RESTFUL API 异常信息处理
 *
 * @Auther: brucewuu
 * @Date: 2019-06-17 11:50
 * @Description:
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 参数校验失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ApiResult> handleError(MethodArgumentNotValidException e) {
        log.error("参数校验失败: {}", e.getMessage());
        return Mono.just(Optional.ofNullable(e.getBindingResult().getFieldError())
                .map(fieldError -> ApiResult.failed(ResultCode.PARAM_VALID_FAILED.getCode(), String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage())))
                .orElseGet(() -> ApiResult.failed(ResultCode.PARAM_VALID_FAILED)));
    }

    /**
     * 参数绑定失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(BindException.class)
    public Mono<ApiResult> handleError(BindException e) {
        log.error("参数绑定失败: {}", e.getMessage());
        return Mono.just(Optional.ofNullable(e.getFieldError())
                .map(fieldError -> ApiResult.failed(ResultCode.PARAM_BIND_FAILED.getCode(), String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage())))
                .orElseGet(() -> ApiResult.failed(ResultCode.PARAM_BIND_FAILED)));
    }

    /**
     * 参数绑定失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ApiResult> handleError(WebExchangeBindException e) {
        log.error("参数绑定失败: {}", e.getMessage());
        return Mono.just(Optional.ofNullable(e.getFieldError())
                .map(fieldError -> ApiResult.failed(ResultCode.PARAM_BIND_FAILED.getCode(), String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage())))
                .orElseGet(() -> ApiResult.failed(ResultCode.PARAM_BIND_FAILED)));
    }

    /**
     * 缺少必要的请求参数
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ApiResult> handleError(ServerWebInputException e) {
        log.error("缺少必要的请求参数: {}", e.getMessage());
        return Mono.just(Optional.ofNullable(e.getMethodParameter())
                .map(parameter -> ApiResult.failed(ResultCode.BAD_REQUEST.getCode(), String.format("缺少必要的请求参数: %s", parameter.getParameterName())))
                .orElseGet(() -> ApiResult.failed(ResultCode.BAD_REQUEST.getCode(), "缺少必要的请求参数")));
    }

    /**
     * 参数验证失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ApiResult> handleError(ConstraintViolationException e) {
        log.error("参数验证失败: {}", e.getMessage());
        ConstraintViolation<?> constraintViolation = e.getConstraintViolations().iterator().next();
        String path = ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, constraintViolation.getMessage());
        return Mono.just(ApiResult.failed(ResultCode.PARAM_VALID_FAILED.getCode(), message));
    }

    /**
     * 响应状态异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ApiResult> handleError(ResponseStatusException e) {
        log.error("响应状态异常: {}", e.getMessage());
        return Mono.just(ApiResult.failed(ResultCode.BAD_REQUEST.getCode(), String.format("响应状态异常:%s", e.getReason())));
    }
}
