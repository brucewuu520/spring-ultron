package org.springultron.wechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JS-SDK签名
 *
 * @author brucewuu
 * @date 2021/4/12 下午4:43
 */
@Schema(description = "JS-SDK签名")
public class WxJsSdkSign {

    @Schema(description = "公众号的唯一标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private String appId;

    @Schema(description = "生成签名的随机串", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nonceStr;

    @Schema(description = "时间戳", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timestamp;

    @Schema(description = "签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signature;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
