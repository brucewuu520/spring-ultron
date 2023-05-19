package org.springultron.wechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 微信卡券签名
 *
 * @author brucewuu
 * @date 2021/4/12 下午5:27
 */
@Schema(description = "微信卡券签名")
public class WxCardSign {

    @Schema(description = "门店ID。shopID用于筛选出拉起带有指定location_list(shopID)的卡券列表，非必填")
    private String shopId;

    @Schema(description = "卡券ID，用于拉起指定cardId的卡券列表，当cardId为空时，默认拉起所有卡券的列表，非必填")
    private String cardId;

    @Schema(description = "卡券类型，用于拉起指定卡券类型的卡券列表。当cardType为空时，默认拉起所有卡券的列表，非必填")
    private String cardType;

    @Schema(description = "生成签名的随机串", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nonceStr;

    @Schema(description = "时间戳", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timestamp;

    @Schema(description = "签名方式，目前仅支持SHA1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signType = "SHA1";

    @Schema(description = "签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cardSign;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCardSign() {
        return cardSign;
    }

    public void setCardSign(String cardSign) {
        this.cardSign = cardSign;
    }
}
