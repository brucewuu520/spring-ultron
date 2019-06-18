package org.springultron.core.result;

/**
 * 封装API的错误码
 *
 * @Auther: brucewuu
 * @Date: 2019-05-22 16:24
 * @Description:
 */
public interface IResultCode {
    int getCode();

    String getMessage();
}
