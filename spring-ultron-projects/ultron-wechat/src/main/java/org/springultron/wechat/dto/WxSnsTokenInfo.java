package org.springultron.wechat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 网页授权access_token信息
 *
 * @author brucewuu
 * @date 2021/4/12 下午3:41
 */
@Schema(description = "网页授权access_token信息")
public class WxSnsTokenInfo {

    @Schema(description = "网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同")
    @JsonAlias(value = "access_token")
    private String accessToken;

    @Schema(description = "access_token接口调用凭证超时时间，单位（秒）")
    @JsonAlias(value = "expires_in")
    private int expiresIn;

    @Schema(description = "用户刷新access_token")
    @JsonAlias(value = "refresh_token")
    private String refreshToken;

    @Schema(description = "用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID")
    @JsonAlias(value = "openid")
    private String openId;

    @Schema(description = "用户授权的作用域，使用逗号（,）分隔")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
