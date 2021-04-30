package org.springultron.wechat.props;

/**
 * 公众号配置
 *
 * @author brucewuu
 * @date 2021/4/14 下午6:51
 */
public class WxConf {
    /**
     * 公众号ID
     */
    private String appId;

    /**
     * 公众号秘钥
     */
    private String appSecret;

    /**
     * 服务器配置 token
     */
    private String token;

    /**
     * 消息加AES加解密密钥
     */
    private String encodingAesKey;

    /**
     * 消息是否加密
     */
    private boolean encryptMessage = false;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public boolean isEncryptMessage() {
        return encryptMessage;
    }

    public void setEncryptMessage(boolean encryptMessage) {
        this.encryptMessage = encryptMessage;
    }
}
