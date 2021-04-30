package org.springultron.wechat.msg.wx.in;

/**
 * 接收图片消息
 * msgType=image
 *
 * @author brucewuu
 * @date 2021/4/14 上午10:55
 */
public class InImageMsg extends InMsg {
    /**
     * 消息ID（64位整型）
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

    public InImageMsg(String toUserName, String fromUserName, Integer createTime, String msgType) {
        super(toUserName, fromUserName, createTime, msgType);
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
