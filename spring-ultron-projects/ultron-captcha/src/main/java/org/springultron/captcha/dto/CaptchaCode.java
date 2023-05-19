package org.springultron.captcha.dto;

/**
 * 图形验证码
 *
 * @param codeKey    验证码缓存key
 * @param base64Code 图形验证码base64
 * @author brucewuu
 * @date 2021/4/13 下午4:30
 */
public record CaptchaCode(String codeKey, String base64Code) {

    public static CaptchaCode of(String codeKey, String base64Code) {
        return new CaptchaCode(codeKey, base64Code);
    }
}
