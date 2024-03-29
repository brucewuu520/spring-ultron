package org.springultron.wechat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 微信小程序用户授权登录session
 *
 * @author brucewuu
 * @date 2021/4/13 上午10:02
 */
@Schema(description = "微信小程序用户授权登录session")
public class WxaUserSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 7842513978217539511L;

    @Schema(description = "用户的标识，对当前小程序唯一", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonAlias(value = "openid")
    private String openId;

    @Schema(description = "会话密钥")
    @JsonAlias(value = "session_key")
    private String sessionKey;

    @Schema(description = "用户在开放平台的唯一标识符（绑定了开发者帐号的小程序，才会出现该字段）")
    @JsonAlias(value = "unionid")
    private String unionId;

    @Schema(description = "错误码(-1: 系统繁忙，此时请开发者稍候再试 0:请求成功 40029:code 无效 频率限制，45011:每个用户每分钟100次)", hidden = true)
    @JsonAlias(value = "errcode")
    private int errCode;

    @Schema(description = "错误信息", hidden = true)
    @JsonAlias(value = "errmsg")
    private String errMsg;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
