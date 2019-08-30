package org.springultron.core.result;

/**
 * 封装API的错误码接口，自定义异常枚举需继承并实现该接口
 *
 * @Auther: brucewuu
 * @Date: 2019-05-22 16:24
 * @Description:
 */
public interface IResultCode {
    int getCode();

    String getMessage();
}
