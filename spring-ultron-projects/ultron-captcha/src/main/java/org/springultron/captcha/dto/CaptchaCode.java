package org.springultron.captcha.dto;

/**
 * 图形验证码
 *
 * @author brucewuu
 * @date 2021/4/13 下午4:30
 */
public class CaptchaCode {
    /**
     * 验证码缓存key
     */
    private String codeKey;
    /**
     * 图形验证码base64
     */
    private String base64Code;

    public static CaptchaCode of(String codeKey, String base64Code) {
        return new CaptchaCode(codeKey, base64Code);
    }

    public CaptchaCode(String codeKey, String base64Code) {
        this.codeKey = codeKey;
        this.base64Code = base64Code;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public void setBase64Code(String base64Code) {
        this.base64Code = base64Code;
    }
}
