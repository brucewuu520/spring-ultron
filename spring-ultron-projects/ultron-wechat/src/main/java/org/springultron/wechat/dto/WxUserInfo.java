package org.springultron.wechat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 公众号用户基本信息（包括UnionID机制）
 *
 * @author brucewuu
 * @date 2021/4/12 下午12:48
 */
@Schema(description = "公众号用户基本信息（包括UnionID机制）")
public class WxUserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -8961156284894198557L;

    @Schema(description = "用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息")
    private int subscribe;

    @Schema(description = "用户的标识，对当前公众号唯一")
    @JsonAlias(value = "openid")
    private String openId;

    @Schema(description = "用户在开放平台的唯一标识符（只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段）")
    @JsonAlias(value = "unionid")
    private String unionId;

    @Schema(description = "用户的昵称")
    @JsonAlias(value = "nickname")
    private String nickName;

    @Schema(description = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效")
    @JsonAlias(value = "headimgurl")
    private String headImgUrl;

    @Schema(description = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private int sex;

    @Schema(description = "用户所在国家")
    private String country;

    @Schema(description = "用户所在省份")
    private String province;

    @Schema(description = "用户所在城市")
    private String city;

    @Schema(description = "用户的语言(英文:en 简体中文:zh_CN 繁体中文:zh_TW)")
    private String language;

    @Schema(description = "用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间")
    @JsonAlias(value = "subscribe_time")
    private long subscribeTime;

    @Schema(description = "公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注")
    private String remark;

    @Schema(description = "用户所在的分组ID（兼容旧的用户分组接口）")
    @JsonAlias(value = "groupid")
    private int groupId;

    @Schema(description = "用户被打上的标签ID列表")
    @JsonAlias(value = "tagid_list")
    private Integer[] tagIdList;

    /**
     * 返回用户关注的渠道来源
     * ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
     * ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，
     * ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，
     * ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他
     */
    @Schema(description = "返回用户关注的渠道来源")
    @JsonAlias(value = "subscribe_scene")
    private String subscribeScene;

    @Schema(description = "二维码扫码场景（开发者自定义）")
    @JsonAlias(value = "qr_scene")
    private String qrScene;

    @Schema(description = "二维码扫码场景描述（开发者自定义）")
    @JsonAlias(value = "qr_scene_str")
    private String qrSceneStr;

    @Schema(description = "用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）")
    private String privilege;

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Integer[] getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(Integer[] tagIdList) {
        this.tagIdList = tagIdList;
    }

    public String getSubscribeScene() {
        return subscribeScene;
    }

    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
    }

    public String getQrScene() {
        return qrScene;
    }

    public void setQrScene(String qrScene) {
        this.qrScene = qrScene;
    }

    public String getQrSceneStr() {
        return qrSceneStr;
    }

    public void setQrSceneStr(String qrSceneStr) {
        this.qrSceneStr = qrSceneStr;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WxUserInfo that = (WxUserInfo) o;
        return Objects.equals(openId, that.openId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openId);
    }
}
