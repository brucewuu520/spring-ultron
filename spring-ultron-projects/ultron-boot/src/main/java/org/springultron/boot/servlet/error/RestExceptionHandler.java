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
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springultron.boot.error.BaseExceptionHandler;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;

import java.sql.SQLIntegrityConstraintViolationException;

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
        log.error("方法参数校验异常: {}", e.getMessage());
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
        log.error("参数绑定异常: {}", e.getMessage());
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
        log.error("缺少必要的请求参数: {}", e.getMessage());
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
        log.error("请求参数格式错误: {}", e.getMessage());
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
        log.error("参数校验异常: {}", e.getMessage());
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
        log.error("参数验证失败: {}", e.getMessage());
        return handleError(e.getConstraintViolations());
    }

    /**
     * 404没找到请求
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<Object> handleError(NoHandlerFoundException e) {
        log.error("404没找到请求: {}", e.getMessage());
        return ApiResult.fail(ResultCode.NOT_FOUND, e.getMessage());
    }

    /**
     * 404没找到资源
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<Object> handleError(NoResourceFoundException e) {
        log.error("404没找到资源: {}", e.getMessage());
        return ApiResult.fail(ResultCode.NOT_FOUND, e.getMessage());
    }

    /**
     * 不支持当前请求方法
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<Object> handleError(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法: {}", e.getMessage());
        return ApiResult.fail(ResultCode.METHOD_NO_ALLOWED, e.getMessage());
    }

    /**
     * 不支持当前媒体类型
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult<Object> handleError(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型: {}", e.getMessage());
        return ApiResult.fail(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 不接受的媒体类型
     *
     * @param e 异常
     * @return ApiResult
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ApiResult<Object> handleError(HttpMediaTypeNotAcceptableException e) {
        log.error("不接受的媒体类型: {}", e.getMessage());
        return ApiResult.fail(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> handleError(HttpMessageNotReadableException e) {
        log.error("消息不能读取: {}", e.getMessage());
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
        log.error("文件太大: {}", e.getMessage());
        return ApiResult.fail(ResultCode.PAYLOAD_TOO_LARGE, e.getMessage());
    }

    /**
     * 加解密异常捕获
     *
     * @param e 加解密异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = CryptoException.class)
    public ApiResult<Object> handleCryptoException(CryptoException e) {
        log.error("加解密异常: {}", e.getMessage());
        return ApiResult.fail(ResultCode.SIGN_FAILED, e.getMessage());
    }

    /**
     * 断言异常捕获
     *
     * @param e 断言异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(AssertionError.class)
    public ApiResult<Object> handleAssertionError(AssertionError e) {
        log.error("断言异常: {}", e.getMessage());
        return ApiResult.fail(ResultCode.ASSERTION_ERROR.getCode(), e.getMessage());
    }

    /**
     * 数据库的唯一约束条件异常捕获
     *
     * @param e 数据库的唯一约束条件异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ApiResult<Object> handleSqlConstraintException(SQLIntegrityConstraintViolationException e) {
        log.error("数据库的唯一约束条件异常: {}", e.getMessage());
        return ApiResult.fail(ResultCode.SQL_CONSTRAINT_ERROR, e.getMessage());
    }

}
