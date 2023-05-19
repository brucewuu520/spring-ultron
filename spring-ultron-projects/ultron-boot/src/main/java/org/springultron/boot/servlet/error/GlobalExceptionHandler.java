package org.springultron.boot.servlet.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springultron.boot.error.UltronErrorEvent;
import org.springultron.core.exception.ApiException;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;
import org.springultron.core.utils.ObjectUtils;
import org.springultron.core.utils.StringUtils;
import org.springultron.core.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

/**
 * 通用自定义异常、未知异常处理
 *
 * @author brucewuu
 * @date 2019-06-05 12:23
 */
@Order
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ApplicationEventPublisher publisher;

    @Autowired
    public GlobalExceptionHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 自定义 REST 业务异常捕获
     *
     * @param e 业务异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = ApiException.class)
    public ApiResult<Object> handleApiException(ApiException e) {
        log.error("自定义业务异常", e);
        publishEvent(e);
        return ApiResult.apiException(e);
    }

    /**
     * 加解密异常捕获
     *
     * @param e 加解密异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = CryptoException.class)
    public ApiResult<Object> handleCryptoException(CryptoException e) {
        log.error("加解密异常", e);
        publishEvent(e);
        return ApiResult.fail(ResultCode.SIGN_FAILED);
    }

    /**
     * 断言异常捕获
     *
     * @param e 断言异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(AssertionError.class)
    public ApiResult<Object> handleAssertionError(AssertionError e) {
        log.error("断言异常", e);
        // 发送：未知异常异常事件
        publishEvent(e);
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
        log.error("数据库的唯一约束条件异常", e);
        // 发送：未知异常异常事件
        publishEvent(e);
        return ApiResult.fail(ResultCode.SQL_CONSTRAINT_ERROR);
    }

    /**
     * 异常事件推送
     *
     * @param error 异常
     */
    private void publishEvent(Throwable error) {
        UltronErrorEvent event = new UltronErrorEvent();
        HttpServletRequest request = WebUtils.getRequest();
        if (request != null) {
            // 请求方法名
            event.setRequestMethod(request.getMethod());
            // 请求地址
            String requestUrl = request.getRequestURI();
            String queryString = request.getQueryString();
            if (StringUtils.isNotEmpty(queryString)) {
                requestUrl = requestUrl + "?" + queryString;
            }
            event.setRequestUrl(requestUrl);
        }
        // 堆栈信息
        event.setStackTrace(Exceptions.getStackTraceString(error));
        event.setExceptionName(error.getClass().getName());
        event.setMessage(error.getMessage());
        event.setCreatedAt(LocalDateTime.now());
        StackTraceElement[] elements = error.getStackTrace();
        if (ObjectUtils.isNotEmpty(elements)) {
            // 报错的类信息
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