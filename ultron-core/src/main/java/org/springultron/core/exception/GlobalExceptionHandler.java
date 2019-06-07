package org.springultron.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springultron.core.api.ApiResult;

/**
 * 通用 Api Controller 全局异常处理
 *
 * @Auther: brucewuu
 * @Date: 2019-06-05 12:23
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义 REST 业务异常处理
     *
     * @param e 业务异常
     * @return REST 返回异常结果
     */
    @ExceptionHandler(value = ApiException.class)
    public ApiResult handleApiException(ApiException e) {
        log.error("handleApiException", e);
        return ApiResult.apiException(e);
    }

    /**
     * 数据校验异常
     *
     * @param e 异常
     * @return 返回异常结果
     */
    @ExceptionHandler(value = BindException.class)
    public ApiResult handleBindException(BindException e) {
        log.error("handleBindException", e);
        FieldError fieldError = e.getFieldError();
        if (null == fieldError) {
            return ApiResult.validateFailed();
        } else {
            return ApiResult.validateFailed(fieldError.getDefaultMessage());
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (null == fieldError) {
            return ApiResult.validateFailed();
        } else {
            return ApiResult.validateFailed(fieldError.getDefaultMessage());
        }
    }
}
