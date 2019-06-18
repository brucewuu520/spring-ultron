package org.springultron.boot.servlet;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springultron.core.result.Result;
import org.springultron.core.result.ResultCode;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;

/**
 * RESTFUL API 异常信息处理
 *
 * @Auther: brucewuu
 * @Date: 2019-06-17 11:50
 * @Description:
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 参数校验失败
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleError(MethodArgumentNotValidException e) {
        log.error("参数校验失败: {}", e.getMessage());
        return Optional.ofNullable(e.getBindingResult().getFieldError())
                .map(fieldError -> Result.failed(ResultCode.PARAM_VALID_FAILED.getCode(), String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage())))
                .orElseGet(() -> Result.failed(ResultCode.PARAM_VALID_FAILED));
    }

    /**
     * 参数绑定失败
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    public Result handleError(BindException e) {
        log.error("参数绑定失败: {}", e.getMessage());
        return Optional.ofNullable(e.getFieldError())
                .map(fieldError -> Result.failed(ResultCode.PARAM_BIND_FAILED.getCode(), String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage())))
                .orElseGet(() -> Result.failed(ResultCode.PARAM_BIND_FAILED));
    }

    /**
     * 缺少必要的请求参数
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleError(MissingServletRequestParameterException e) {
        log.error("缺少必要的请求参数: {}", e.getMessage());
        return Result.failed(ResultCode.BAD_REQUEST.getCode(), String.format("缺少必要的请求参数: %s", e.getMessage()));
    }

    /**
     * 请求参数格式错误
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleError(MethodArgumentTypeMismatchException e) {
        log.error("请求参数格式错误: {}", e.getMessage());
        return Result.failed(ResultCode.BAD_REQUEST.getCode(), String.format("请求参数格式错误: %s", e.getName()));
    }

    /**
     * 参数验证失败
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleError(ConstraintViolationException e) {
        log.error("参数验证失败: {}", e.getMessage());
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        ConstraintViolation<?> constraintViolation = constraintViolationSet.iterator().next();
        String path = ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, constraintViolation.getMessage());
        return Result.failed(ResultCode.PARAM_VALID_FAILED.getCode(), message);
    }

    /**
     * 404没找到请求
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleError(NoHandlerFoundException e) {
        log.error("404没找到请求: {}", e.getMessage());
        return Result.failed(ResultCode.NOT_FOUND);
    }

    /**
     * 不支持当前请求方法
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleError(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法: {}", e.getMessage());
        return Result.failed(ResultCode.METHOD_NO_ALLOWED);
    }

    /**
     * 不支持当前媒体类型
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result handleError(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型: {}", e.getMessage());
        return Result.failed(ResultCode.BAD_REQUEST.getCode(), "不支持当前媒体类型");
    }

    /**
     * 不接受的媒体类型
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public Result handleError(HttpMediaTypeNotAcceptableException e) {
        log.error("不接受的媒体类型: {}", e.getMessage());
        return Result.failed(ResultCode.BAD_REQUEST.getCode(), "不接受的媒体类型");
    }
}
