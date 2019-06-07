package org.springultron.core.api;

import org.springultron.core.exception.ApiException;

import java.io.Serializable;

/**
 * 通用请求返回对象
 *
 * @Auther: brucewuu
 * @Date: 2019-05-22 15:16
 * @Description:
 */
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = -2832435143001472900L;

    private int code;
    private String message;
    private T data;

    private ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回成功
     *
     * @param message 提示信息
     */
    public static ApiResult success(String message) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * 失败返回信息
     */
    public static ApiResult failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 失败返回信息
     *
     * @param errorCode 错误码
     */
    public static ApiResult failed(IErrorCode errorCode) {
        return new ApiResult(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败返回信息
     *
     * @param message 提示信息
     */
    public static ApiResult failed(String message) {
        return new ApiResult(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 请求验签失败
     */
    public static ApiResult signFailed() {
        return failed(ResultCode.SIGN_FAILED);
    }

    /**
     * 参数校验失败返回信息
     */
    public static ApiResult validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数校验失败返回信息
     *
     * @param message 提示信息
     */
    public static ApiResult validateFailed(String message) {
        return new ApiResult(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 未登录或token已经过期返回信息
     */
    public static ApiResult unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }

    /**
     * 未授权返回信息
     */
    public static ApiResult forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }

    /**
     * REST API 异常返回
     *
     * @param e 异常
     * @return 返回值
     */
    public static ApiResult apiException(ApiException e) {
        return new ApiResult(e.getCode(), e.getMessage());
    }
}
