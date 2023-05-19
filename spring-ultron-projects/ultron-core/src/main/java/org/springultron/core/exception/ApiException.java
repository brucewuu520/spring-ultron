package org.springultron.core.exception;

import org.springultron.core.result.ApiResult;
import org.springultron.core.result.IResultCode;

import java.io.Serial;

/**
 * 自定义异常
 *
 * @author brucewuu
 * @date 2019-06-03 09:46
 */
public class ApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 250919198459751841L;

    private final ApiResult<?> result;

    public ApiException(ApiResult<?> result) {
        super(result.getMessage());
        this.result = result;
    }

    public ApiException(IResultCode resultCode) {
        this(resultCode, resultCode.getMessage());
    }

    public ApiException(IResultCode resultCode, String message) {
        super(message);
        this.result = ApiResult.fail(resultCode, message);
    }

    public ApiException(int resultCode, String message) {
        super(message);
        this.result = ApiResult.fail(resultCode, message);
    }

    public ApiException(String message) {
        super(message);
        this.result = ApiResult.fail(message);
    }

    public ApiException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        doFillInStackTrace();
        this.result = ApiResult.fail(message);
    }

    public <T> ApiResult<T> getResult() {
        //noinspection unchecked
        return (ApiResult<T>) result;
    }

    /**
     * 提高性能
     *
     * @return Throwable
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}
