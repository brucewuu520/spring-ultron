package org.springultron.wechat.params;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 创建公众号二维码参数
 *
 * @author brucewuu
 * @date 2021/4/12 下午6:39
 */
@Schema(description = "创建公众号二维码参数")
public class WxQrCodeParams {

    @Schema(description = "二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值", requiredMode = Schema.RequiredMode.REQUIRED)
    private String actionName;

    @Schema(description = "场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）")
    private Integer sceneId;

    @Schema(description = "场景值ID（字符串形式的ID），字符串类型，长度限制为1到64")
    private String sceneStr;

    @Schema(description = "该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒")
    private Integer expireSeconds;

    public String getActionName() {
        return actionName;
    }

    public WxQrCodeParams setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public WxQrCodeParams setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public WxQrCodeParams setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
        return this;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public WxQrCodeParams setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
        return this;
    }
}
