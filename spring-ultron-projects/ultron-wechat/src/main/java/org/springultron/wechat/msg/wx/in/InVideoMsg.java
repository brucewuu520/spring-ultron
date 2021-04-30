package org.springultron.wechat.msg.wx.in;

/**
 * 接收视频消息
 * msgType=video
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:05
 */
public class InVideoMsg extends InMsg {
    /**
     * 消息ID（64位整型）
     */
    private String msgId;
    /**
     * 视频消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String mediaId;
    /**
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private String thumbMediaId;

    public InVideoMsg(String toUserName, String fromUserName, Integer createTime, String msgType) {
        super(toUserName, fromUserName, createTime, msgType);
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
