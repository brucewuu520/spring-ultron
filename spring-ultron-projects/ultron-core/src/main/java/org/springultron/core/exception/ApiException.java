package org.springultron.core.exception;

import org.springframework.lang.Nullable;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.IResultCode;

/**
 * 自定义异常
 *
 * @author brucewuu
 * @date 2019-06-03 09:46
 */
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 250919198459751841L;

    @Nullable
    private final ApiResult<?> result;

    public ApiException(ApiResult<?> result) {
        super(result.getMessage());
        this.result = result;
    }

    public ApiException(IResultCode resultStatus) {
        this(resultStatus, resultStatus.getMessage());
    }

    public ApiException(IResultCode resultStatus, String message) {
        super(message);
        this.result = ApiResult.fail(resultStatus, message);
    }

    public ApiException(String message) {
        super(message);
        this.result = null;
    }

    public ApiException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        doFillInStackTrace();
        this.result = null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> ApiResult<T> getResult() {
        return (ApiResult<T>) result;
    }

    /**
     * 提高性能
     * @return Throwable
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}
