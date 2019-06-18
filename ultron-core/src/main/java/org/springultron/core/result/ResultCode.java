package org.springultron.core.result;

/**
 * 请求返回状态码
 *
 * @Auther: brucewuu
 * @Date: 2019-05-22 16:27
 * @Description:
 */
public enum ResultCode implements IResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    BAD_REQUEST(400, "错误的请求"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    SIGN_FAILED(402, "验证签名失败"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "404没找到请求"),
    METHOD_NO_ALLOWED(405, "不支持当前请求方法"),
    PARAM_BIND_FAILED(406, "参数绑定失败"),
    PARAM_VALID_FAILED(407, "参数校验失败"),
    API_EXCEPTION(417, "操作异常");

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
