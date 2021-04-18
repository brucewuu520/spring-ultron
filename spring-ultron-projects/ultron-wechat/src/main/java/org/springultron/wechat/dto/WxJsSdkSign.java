package org.springultron.wechat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * JS-SDK签名
 *
 * @author brucewuu
 * @date 2021/4/12 下午4:43
 */
@ApiModel(description = "JS-SDK签名")
public class WxJsSdkSign {

    @ApiModelProperty(value = "公众号的唯一标识", required = true, position = 1)
    private String appId;

    @ApiModelProperty(value = "生成签名的随机串", required = true, position = 2)
    private String nonceStr;

    @ApiModelProperty(value = "时间戳", required = true, position = 3)
    private String timestamp;

    @ApiModelProperty(value = "签名", required = true, position = 4)
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
