package org.springultron.wechat.props;

/**
 * 小程序配置
 *
 * @author brucewuu
 * @date 2021/4/14 下午6:51
 */
public class WxaConf {
    /**
     * 小程序ID
     */
    private String appId;

    /**
     * 小程序秘钥
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

    /**
     * 消息类型，默认: JSON
     */
    private MsgType msgType = MsgType.JSON;

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

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public enum MsgType {
        JSON,
        XML
    }
}
