package org.springultron.core.result;

import org.springultron.core.exception.ApiException;

import java.io.Serializable;
import java.util.Optional;

/**
 * 通用请求返回对象
 *
 * @Auther: brucewuu
 * @Date: 2019-05-22 15:16
 * @Description:
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -2832435143001472900L;

    private int code;
    private String message;
    private T data;

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(int code, String message, T data) {
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
     * 返回成功
     */
    public static Result success() {
        return new Result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 返回成功
     *
     * @param message 提示信息
     */
    public static Result success(String message) {
        return new Result(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回信息
     */
    public static Result failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 失败返回信息
     *
     * @param errorCode 错误码
     */
    public static Result failed(IResultCode errorCode) {
        return new Result(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败返回信息
     *
     * @param message 提示信息
     */
    public static Result failed(String message) {
        return new Result(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 失败返回信息
     *
     * @param code    状态码
     * @param message 消息描述
     */
    public static Result failed(int code, String message) {
        return new Result(code, message);
    }

    /**
     * 请求验签失败
     */
    public static Result signFailed() {
        return failed(ResultCode.SIGN_FAILED);
    }

    /**
     * 未登录或token已经过期返回信息
     */
    public static Result unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }

    /**
     * 未授权返回信息
     */
    public static Result forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param resultCode IResultCode
     */
    public static void throwFail(IResultCode resultCode) {
        throw new ApiException(resultCode.getMessage(), resultCode.getCode());
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param message 异常信息
     */
    public static void throwFail(String message) {
        throw new ApiException(message, ResultCode.FAILED.getCode());
    }


    /**
     * REST API 异常返回
     *
     * @param e 异常
     * @return 返回值
     */
    public static Result apiException(ApiException e) {
        return new Result(e.getCode(), e.getMessage());
    }

    /**
     * 判断返回是否为成功
     *
     * @param result 返回结果
     * @return 是否成功
     */
    public static boolean isSuccess(Result<?> result) {
        return Optional.ofNullable(result)
                .map(r -> r.code)
                .map(code -> code == ResultCode.SUCCESS.getCode())
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为失败
     *
     * @param result 返回结果
     * @return 是否失败
     */
    public static boolean isFail(Result<?> result) {
        return !isSuccess(result);
    }
}
