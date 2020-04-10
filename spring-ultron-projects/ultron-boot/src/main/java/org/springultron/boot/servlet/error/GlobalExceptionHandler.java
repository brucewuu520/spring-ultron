package org.springultron.boot.servlet.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.exception.ServiceException;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;

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

    /**
     * 自定义 REST 业务异常处理
     *
     * @param e 业务异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = ServiceException.class)
    public ApiResult<Object> handleApiException(ServiceException e) {
        log.error("自定义业务异常: {}", e.getMessage());
        return ApiResult.apiException(e);
    }

    /**
     * 加解密异常处理
     *
     * @param e 加解密异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = CryptoException.class)
    public ApiResult<Object> handleCryptoException(CryptoException e) {
        log.error("加解密异常: {}", e.getMessage());
        return ApiResult.failed(ResultCode.SIGN_FAILED);
    }

    @ExceptionHandler(AssertionError.class)
    public ApiResult<Object> handleAssertionError(AssertionError e) {
        log.error("断言异常: {}", e.getMessage());
        // 发送：未知异常异常事件
        return ApiResult.failed(ResultCode.ASSERTION_ERROR.getCode(), e.getMessage());
    }
}