package org.springultron.wechat.msg.wxa.parser;

import java.lang.annotation.*;

/**
 * xml解析注解
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface XPath {

    String value();

}
