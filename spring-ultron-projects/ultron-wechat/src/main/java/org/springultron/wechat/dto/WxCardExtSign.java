package org.springultron.wechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 微信卡券扩展字段签名
 *
 * @author brucewuu
 * @date 2021/4/12 下午5:34
 */
@Schema(description = "微信卡券扩展字段签名")
public class WxCardExtSign {

    @Schema(description = "指定领取者的openid，只有该用户能领取。bind_openid字段为true的卡券必须填写，bind_openid字段为false不必填写")
    private String openid;

    @Schema(description = "指定的卡券code码，只能被领一次。自定义code模式的卡券必须填写，非自定义code和预存code模式的卡券不必填写")
    private String code;

    @Schema(description = "随机字符串，不长于32位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nonce_str;

    @Schema(description = "时间戳", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timestamp;

    @Schema(description = "卡券在第三方系统的实际领取时间，为东八区时间戳（UTC+8,精确到秒）")
    private String fixed_begintimestamp;

    @Schema(description = "领取渠道参数，用于标识本次领取的渠道值")
    private String outer_str;

    @Schema(description = "签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signature;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFixed_begintimestamp() {
        return fixed_begintimestamp;
    }

    public void setFixed_begintimestamp(String fixed_begintimestamp) {
        this.fixed_begintimestamp = fixed_begintimestamp;
    }

    public String getOuter_str() {
        return outer_str;
    }

    public void setOuter_str(String outer_str) {
        this.outer_str = outer_str;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
