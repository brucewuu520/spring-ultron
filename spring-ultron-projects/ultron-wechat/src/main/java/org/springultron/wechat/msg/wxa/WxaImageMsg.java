package org.springultron.wechat.msg.wxa;

/**
 * 图片消息
 * 用户在客服会话中发送图片消息时将产生如下数据包
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:22
 */
public class WxaImageMsg extends WxaMsg {
    /**
     * 消息id（64位整型）
     */
    private String msgId;
    /**
     * 图片链接（由系统生成）
     */
    private String picUrl;
    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String mediaId;

    public WxaImageMsg(MsgModel msgModel) {
        super(msgModel);
        this.msgId = msgModel.getMsgId();
        this.picUrl = msgModel.getPicUrl();
        this.mediaId = msgModel.getMediaId();
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
}
