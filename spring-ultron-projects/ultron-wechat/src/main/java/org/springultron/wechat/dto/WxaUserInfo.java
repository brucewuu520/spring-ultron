package org.springultron.wechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 小程序用户基本信息
 *
 * @author brucewuu
 * @date 2021/4/19 下午5:40
 */
@Schema(description = "小程序用户基本信息")
public class WxaUserInfo {

    @Schema(description = "用户的标识，对当前小程序唯一")
    private String openId;

    /**
     * 绑定了开发者帐号的小程序，开发者可以直接通过 wx.login + code2Session 获取到该用户 UnionID，无须用户授权
     */
    @Schema(description = "用户在开放平台的唯一标识符")
    private String unionId;

    @Schema(description = "用户绑定的手机号（国外手机号会有区号）")
    private String phoneNumber;

    @Schema(description = "没有区号的手机号")
    private String purePhoneNumber;

    @Schema(description = "手机号区号")
    private String countryCode;

    @Schema(description = "用户昵称")
    private String nickName;

    /**
     * 用户头像图片的 URL
     * <p>
     * 最后一个数值代表正方形头像大小：
     * 0、46、64、96、132 数值可选
     * 0 代表 640x640 的正方形头像
     * 46 表示 46x46 的正方形头像
     * 剩余数值以此类推。默认132）
     * </p>
     */
    @Schema(description = "用户昵称")
    private String avatarUrl;

    @Schema(description = "用户性别(0:未知 1:男性 2:女性)")
    private int gender;

    @Schema(description = "用户所在国家")
    private String country;

    @Schema(description = "用户所在省份")
    private String province;

    @Schema(description = "用户所在城市")
    private String city;

    @Schema(description = "用户的语言(英文:en 简体中文:zh_CN 繁体中文:zh_TW)")
    private String language;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
