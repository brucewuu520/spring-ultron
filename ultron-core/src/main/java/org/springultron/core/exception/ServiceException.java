package org.springultron.core.exception;

import org.springultron.core.result.IResultCode;
import org.springultron.core.result.ResultCode;

/**
 * 自定义REST异常
 *
 * @author brucewuu
 * @date 2019-06-03 09:46
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 250919198459751841L;

    /**
     * 异常码
     */
    private int code = ResultCode.API_EXCEPTION.getCode();

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public ServiceException(IResultCode resultCode) {
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
