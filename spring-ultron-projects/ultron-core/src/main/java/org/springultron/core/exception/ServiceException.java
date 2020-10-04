package org.springultron.core.exception;

import org.springframework.lang.Nullable;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.IResultStatus;

/**
 * 自定义业务异常
 *
 * @author brucewuu
 * @date 2019-06-03 09:46
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 250919198459751841L;

    @Nullable
    private final ApiResult<?> result;

    public ServiceException(ApiResult<?> result) {
        super(result.getMessage());
        this.result = result;
    }

    public ServiceException(IResultStatus resultStatus) {
        this(resultStatus, resultStatus.getMessage());
    }

    public ServiceException(IResultStatus resultStatus, String message) {
        super(message);
        this.result = ApiResult.failed(resultStatus, message);
    }

    public ServiceException(String message) {
        super(message);
        this.result = null;
    }

    public ServiceException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public ServiceException(String message, Throwable cause) {
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
