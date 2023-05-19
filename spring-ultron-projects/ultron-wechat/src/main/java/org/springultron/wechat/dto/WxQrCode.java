package org.springultron.wechat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 微信公众号二维码信息
 *
 * @author brucewuu
 * @date 2021/4/12 下午6:44
 */
@Schema(description = "微信公众号二维码信息")
public class WxQrCode {

    @Schema(description = "获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ticket;

    @Schema(description = "该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonAlias(value = "expire_seconds")
    private int expireSeconds;

    @Schema(description = "二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
