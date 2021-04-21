package org.springultron.wechat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 小程序用户基本信息
 *
 * @author brucewuu
 * @date 2021/4/19 下午5:40
 */
@ApiModel(description = "小程序用户基本信息")
public class WxaUserInfo {

    @ApiModelProperty(value = "用户的标识，对当前小程序唯一", position = 1)
    private String openId;

    /**
     * 绑定了开发者帐号的小程序，开发者可以直接通过 wx.login + code2Session 获取到该用户 UnionID，无须用户授权
     */
    @ApiModelProperty(value = "用户在开放平台的唯一标识符", position = 2)
    private String unionId;

    @ApiModelProperty(value = "用户绑定的手机号（国外手机号会有区号）", position = 3)
    private String phoneNumber;

    @ApiModelProperty(value = "没有区号的手机号", position = 4)
    private String purePhoneNumber;

    @ApiModelProperty(value = "手机号区号", position = 5)
    private String countryCode;

    @ApiModelProperty(value = "用户昵称", position = 6)
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
    @ApiModelProperty(value = "用户昵称", position = 7)
    private String avatarUrl;

    @ApiModelProperty(value = "用户性别(0:未知 1:男性 2:女性)", position = 8)
    private int gender;

    @ApiModelProperty(value = "用户所在国家", position = 9)
    private String country;

    @ApiModelProperty(value = "用户所在省份", position = 10)
    private String province;

    @ApiModelProperty(value = "用户所在城市", position = 11)
    private String city;

    @ApiModelProperty(value = "用户的语言(英文:en 简体中文:zh_CN 繁体中文:zh_TW)", position = 12)
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
