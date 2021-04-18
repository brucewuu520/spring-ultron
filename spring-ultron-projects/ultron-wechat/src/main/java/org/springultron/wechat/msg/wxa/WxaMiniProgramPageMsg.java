package org.springultron.wechat.msg.wxa;

/**
 * 小程序卡片消息
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:24
 */
public class WxaMiniProgramPageMsg extends WxaMsg {
    /**
     * 消息ID（64位整型）
     */
    private String msgId;
    /**
     * 小程序appId
     */
    private String appId;
    /**
     * 标题
     */
    private String title;
    /**
     * 缩略图URL
     */
    private String thumbUrl;
    /**
     * 消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private String thumbMediaId;
    /**
     * 小程序页面路径
     */
    private String pagePath;

    public WxaMiniProgramPageMsg(MsgModel msgModel) {
        super(msgModel);
        this.msgId = msgModel.getMsgId();
        this.title = msgModel.getTitle();
        this.thumbMediaId = msgModel.getThumbMediaId();
        this.thumbUrl = msgModel.getThumbUrl();
        this.appId = msgModel.getAppId();
        this.pagePath = msgModel.getPagePath();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
}
