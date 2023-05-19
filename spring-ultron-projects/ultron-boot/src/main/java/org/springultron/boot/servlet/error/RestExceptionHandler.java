package org.springultron.boot.servlet.error;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springultron.boot.error.BaseExceptionHandler;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;

/**
 * RESTFUL API 异常信息处理
 *
 * @author brucewuu
 * @date 2019-06-17 11:50
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RestExceptionHandler extends BaseExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 方法参数校验异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<Object> handleError(MethodArgumentNotValidException e) {
        log.error("方法参数校验异常", e);
        return handleError(e.getBindingResult());
    }

    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(BindException.class)
    public ApiResult<Object> handleError(BindException e) {
        log.error("参数绑定异常", e);
        return handleError(e.getBindingResult());
    }

    /**
     * 缺少必要的请求参数
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<Object> handleError(MissingServletRequestParameterException e) {
        log.error("缺少必要的请求参数", e);
        return ApiResult.fail(ResultCode.PARAM_MISS.getCode(), String.format("缺少必要的请求参数: %s", e.getParameterName()));
    }

    /**
     * 请求参数格式错误
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult<Object> handleError(MethodArgumentTypeMismatchException e) {
        log.error("请求参数格式错误", e);
        return ApiResult.fail(ResultCode.PARAM_TYPE_ERROR.getCode(), String.format("请求参数格式错误: %s", e.getName()));
    }

    /**
     * 参数校验异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ValidationException.class)
    public ApiResult<Object> handleError(ValidationException e) {
        log.error("参数校验异常", e);
        Throwable cause = e.getCause();
        return ApiResult.fail(ResultCode.PARAM_VALID_FAILED.getCode(), cause == null ? e.getMessage() : cause.getMessage());
    }

    /**
     * 参数验证失败
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult<Object> handleError(ConstraintViolationException e) {
        log.error("参数验证失败", e);
        return handleError(e.getConstraintViolations());
    }

    /**
     * 404没找到请求
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult<Object> handleError(NoHandlerFoundException e) {
        log.error("404没找到请求", e);
        return ApiResult.fail(ResultCode.NOT_FOUND);
    }

    /**
     * 不支持当前请求方法
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<Object> handleError(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);
        return ApiResult.fail(ResultCode.METHOD_NO_ALLOWED);
    }

    /**
     * 不支持当前媒体类型
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult<Object> handleError(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型", e);
        return ApiResult.fail(ResultCode.BAD_REQUEST.getCode(), "不支持当前媒体类型");
    }

    /**
     * 不接受的媒体类型
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ApiResult<Object> handleError(HttpMediaTypeNotAcceptableException e) {
        log.error("不接受的媒体类型", e);
        return ApiResult.fail(ResultCode.BAD_REQUEST.getCode(), "不接受的媒体类型");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> handleError(HttpMessageNotReadableException e) {
        log.error("消息不能读取", e);
        return ApiResult.fail(ResultCode.MSG_NOT_READABLE.getCode(), e.getMessage());
    }

    /**
     * 文件上传异常
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(MultipartException.class)
    public ApiResult<Object> handleError(MultipartException e) {
        log.error("文件太大", e);
        return ApiResult.fail(ResultCode.PAYLOAD_TOO_LARGE);
    }
}
