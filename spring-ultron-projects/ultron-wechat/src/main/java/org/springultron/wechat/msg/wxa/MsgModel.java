package org.springultron.wechat.msg.wxa;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springultron.core.jackson.Jackson;
import org.springultron.wechat.msg.wxa.parser.XPath;

/**
 * 小程序消息模型
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:08
 */
public class MsgModel {

    @JsonProperty("ToUserName")
    @XPath("//ToUserName")
    private String toUserName;

    @JsonProperty("FromUserName")
    @XPath("//FromUserName")
    private String fromUserName;

    @JsonProperty("CreateTime")
    @XPath("//CreateTime")
    private Integer createTime;

    @JsonProperty("MsgType")
    @XPath("//MsgType")
    private String msgType;

    // 文本消息
    @JsonProperty("Content")
    @XPath("//Content")
    private String content;

    @JsonProperty("MsgId")
    @XPath("//MsgId")
    private String msgId;

    // 图片消息
    @JsonProperty("PicUrl")
    @XPath("//PicUrl")
    private String picUrl;

    @JsonProperty("MediaId")
    @XPath("//MediaId")
    private String mediaId;

    // 事件消息
    @JsonProperty("Event")
    @XPath("//Event")
    private String event;

    @JsonProperty("SessionFrom")
    @XPath("//SessionFrom")
    private String sessionFrom;

    // 小程序客服卡片消息
    @JsonProperty("Title")
    @XPath("//Title")
    private String title;

    @JsonProperty("AppId")
    @XPath("//AppId")
    private String appId;

    @JsonProperty("PagePath")
    @XPath("//PagePath")
    private String pagePath;

    @JsonProperty("ThumbUrl")
    @XPath("//ThumbUrl")
    private String thumbUrl;

    @JsonProperty("ThumbMediaId")
    @XPath("//ThumbMediaId")
    private String thumbMediaId;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    @Override
    public String toString() {
        return Jackson.toJson(this);
    }
}
