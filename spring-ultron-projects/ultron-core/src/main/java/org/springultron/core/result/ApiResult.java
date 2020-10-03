package org.springultron.core.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springultron.core.exception.ServiceException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 通用请求返回体
 *
 * @author brucewuu
 * @date 2019-05-22 15:16
 */
@ApiModel(description = "返回体", value = "ApiResult")
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = -2832435143001472900L;

    @ApiModelProperty(value = "状态码", notes = "200代表请求操作成功", dataType = "int", required = true, example = "200")
    private int code;

    @ApiModelProperty(value = "消息", required = true, example = "success", position = 1)
    private String message;

    @ApiModelProperty(value = "字段校验异常信息", position = 2)
    private List<FieldErrorDTO> fieldErrors;

    @ApiModelProperty(value = "返回数据", position = 3)
    private T data;

    public ApiResult() {}

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

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 返回成功
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage());
    }

    /**
     * 返回成功
     *
     * @param message 提示信息
     * @param <T>     泛型标记
     */
    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(ResultStatus.SUCCESS.getCode(), message);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param <T>  泛型标记
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     * @param <T>     泛型标记
     */
    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<>(ResultStatus.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回信息
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> failed() {
        return failed(ResultStatus.FAILED);
    }

    /**
     * 失败返回信息
     *
     * @param errorCode 异常枚举
     * @param <T>       泛型标记
     */
    public static <T> ApiResult<T> failed(IResultStatus errorCode) {
        return new ApiResult<>(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败返回信息
     *
     * @param message 提示信息
     * @param <T>     泛型标记
     */
    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<>(ResultStatus.FAILED.getCode(), message);
    }

    /**
     * 失败返回信息
     *
     * @param code    状态码
     * @param message 消息描述
     * @param <T>     泛型标记
     */
    public static <T> ApiResult<T> failed(int code, String message) {
        return new ApiResult<>(code, message);
    }

    /**
     * 根据状态判断返回成功or失败
     *
     * @param status 状态
     * @param <T>    泛型标记
     */
    public static <T> ApiResult<T> status(boolean status) {
        return status ? ApiResult.success() : ApiResult.failed();
    }

    /**
     * 根据状态判断返回成功or失败
     *
     * @param status  状态
     * @param message 异常信息
     * @param <T>     泛型标记
     */
    public static <T> ApiResult<T> status(boolean status, String message) {
        return status ? ApiResult.success() : ApiResult.failed(message);
    }

    /**
     * 根据状态判断返回成功or失败
     *
     * @param status    状态
     * @param errorCode 异常枚举
     * @param <T>       泛型标记
     */
    public static <T> ApiResult<T> status(boolean status, IResultStatus errorCode) {
        return status ? ApiResult.success() : ApiResult.failed(errorCode);
    }

    /**
     * 请求验签失败
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> signFailed() {
        return failed(ResultStatus.SIGN_FAILED);
    }

    /**
     * 未认证
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> unauthorized() {
        return failed(ResultStatus.UNAUTHORIZED);
    }

    /**
     * 未认证
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> unauthorized(String message) {
        return failed(ResultStatus.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 未授权
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> forbidden() {
        return failed(ResultStatus.FORBIDDEN);
    }

    /**
     * 未授权
     *
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> forbidden(String message) {
        return failed(ResultStatus.FORBIDDEN.getCode(), message);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param resultCode IResultCode
     */
    public static void throwFail(IResultStatus resultCode) {
        throw new ServiceException(resultCode.getMessage(), resultCode.getCode());
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     *
     * @param message 异常信息
     */
    public static void throwFail(String message) {
        throw new ServiceException(message, ResultStatus.FAILED.getCode());
    }

    /**
     * REST API 异常返回
     *
     * @param e   异常
     * @param <T> 泛型标记
     */
    public static <T> ApiResult<T> apiException(ServiceException e) {
        return new ApiResult<>(e.getCode(), e.getMessage());
    }

    /**
     * 判断返回是否为成功
     *
     * @param result 返回结果
     * @return 是否成功
     */
    public static boolean isSuccess(ApiResult<?> result) {
        return Optional.ofNullable(result)
                .map(r -> r.code)
                .map(code -> code == ResultStatus.SUCCESS.getCode())
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为失败
     *
     * @param result 返回结果
     * @return 是否失败
     */
    public static boolean isFail(ApiResult<?> result) {
        return !isSuccess(result);
    }
}
