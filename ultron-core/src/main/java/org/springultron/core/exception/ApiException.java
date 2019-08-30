package org.springultron.core.exception;

import org.springultron.core.result.IResultCode;
import org.springultron.core.result.ResultCode;

/**
 * 自定义REST异常
 *
 * @Auther: brucewuu
 * @Date: 2019-06-03 09:46
 */
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 250919198459751841L;

    /**
     * 异常码
     */
    private int code = ResultCode.API_EXCEPTION.getCode();

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int code) {
        this.code = code;
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ApiException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public ApiException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
