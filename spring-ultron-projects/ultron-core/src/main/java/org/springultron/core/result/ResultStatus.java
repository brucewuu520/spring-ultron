package org.springultron.core.result;

/**
 * 请求返回状态
 *
 * @author brucewuu
 * @date 2019-05-22 16:27
 */
public enum ResultStatus implements IResultStatus {
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
     * 消息不能读取
     */
    MSG_NOT_READABLE(451, "Message Not Readable"),
    /**
     * 请求被拒绝
     */
    REQUEST_REJECT(428, "Request Reject"),
    /**
     * 请求参数缺失
     */
    PARAM_MISS(10406, "Param Miss"),
    /**
     * 参数类型错误
     */
    PARAM_TYPE_ERROR(10407, "Param Type Error"),
    /**
     * 参数绑定失败
     */
    PARAM_BIND_FAILED(10408, "Param Bind Fail"),
    /**
     * 参数校验失败
     */
    PARAM_VALID_FAILED(10409, "Param Verify Fail"),
    /**
     * 请求体太大
     */
    PAYLOAD_TOO_LARGE(10413, "Payload Too Large"),
    /**
     * 操作失败
     */
    API_EXCEPTION(10417, "Operation Fail"),
    /**
     * 限流
     */
    FLOW_LIMITING(10444, "Flow Limiting"),
    /**
     * 断言失败
     */
    ASSERTION_ERROR(10445, "Assert Fail"),
    /**
     * 服务间调用失败
     */
    CALL_ERROR(10500, "server call error");

    private final int code;
    private final String message;

    ResultStatus(int code, String message) {
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
