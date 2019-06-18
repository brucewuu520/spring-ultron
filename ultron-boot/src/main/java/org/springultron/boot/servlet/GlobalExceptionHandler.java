package org.springultron.boot.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springultron.core.exception.ApiException;
import org.springultron.core.result.Result;

/**
 * 通用自定义异常、未知异常处理
 *
 * @Auther: brucewuu
 * @Date: 2019-06-05 12:23
 * @Description:
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
    @ExceptionHandler(value = ApiException.class)
    public Result handleApiException(ApiException e) {
        log.error("自定义业务异常: {}", e.getMessage());
        return Result.apiException(e);
    }

    @ExceptionHandler(Throwable.class)
    public Result handleError(Throwable e) {
        log.error("未知异常: {}", e.getMessage());
        // 发送：未知异常异常事件
        return Result.failed("系统未知异常");
    }
}
