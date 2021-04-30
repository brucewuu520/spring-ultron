package org.springultron.core.result;

/**
 * 自定义返回状态，自定义异常枚举需继承并实现该接口
 *
 * @author brucewuu
 * @date 2019-05-22 16:24
 */
public interface IResultCode {
    int getCode();

    String getMessage();
}
