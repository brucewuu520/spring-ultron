package org.springultron.core.result;

/**
 * 请求返回状态码
 *
 * @author brucewuu
 * @date 2019-05-22 16:27
 */
public enum ResultCode implements IResultCode {

    SUCCESS(200, "Success"),
    FAILED(500, "Fail"),
    BAD_REQUEST(400, "Bat Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    SIGN_FAILED(402, "Signature Verify Failed"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "404 Not Found"),
    METHOD_NO_ALLOWED(405, "Method Not Allowed"),
    PARAM_BIND_FAILED(406, "Param Bind Fail"),
    PARAM_VALID_FAILED(407, "Param Verify Fail"),
    API_EXCEPTION(417, "Operation Fail"),
    FLOW_LIMITING(444, "Flow Limiting"),
    ASSERTION_ERROR(445, "Assert Fail");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
