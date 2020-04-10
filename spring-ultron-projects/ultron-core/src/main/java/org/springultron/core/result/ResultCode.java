package org.springultron.core.result;

/**
 * 请求返回状态码
 *
 * @author brucewuu
 * @date 2019-05-22 16:27
 */
public enum ResultCode implements IResultCode {
    /**
     * 请求成功
     */
    SUCCESS(200, "Success"),
    /**
     * 请求失败
     */
    FAILED(500, "Fail"),
    /**
     * 错误的请求
     */
    BAD_REQUEST(400, "Bat Request"),
    /**
     * 身份未认证或身份认证失败
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * 签名校验失败
     */
    SIGN_FAILED(402, "Signature Verify Failed"),
    /**
     * 无访问权限
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "404 Not Found"),
    /**
     * 不支持该请求方法
     */
    METHOD_NO_ALLOWED(405, "Method Not Allowed"),
    /**
     * 参数绑定失败
     */
    PARAM_BIND_FAILED(406, "Param Bind Fail"),
    /**
     * 参数校验失败
     */
    PARAM_VALID_FAILED(407, "Param Verify Fail"),
    /**
     * 请求体太大
     */
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    /**
     * 操作失败
     */
    API_EXCEPTION(417, "Operation Fail"),
    /**
     * 限流
     */
    FLOW_LIMITING(444, "Flow Limiting"),
    /**
     * 断言失败
     */
    ASSERTION_ERROR(445, "Assert Fail");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
