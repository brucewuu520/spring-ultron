package org.springultron.wechat.msg.wx.out;

import org.springultron.wechat.msg.wx.in.InMsg;

/**
 * 回复语音消息
 *
 * @author brucewuu
 * @date 2021/4/15 下午5:47
 */
public class OutVoiceMsg extends OutMsg {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String mediaId;

    public OutVoiceMsg() {
        this.msgType = "voice";
    }

    public OutVoiceMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = "voice";
    }

    @Override
    protected void customizeXml(StringBuilder sb) {
        if (null == mediaId) {
            throw new NullPointerException("mediaId can not be null.");
        }
        sb.append("<Voice>\n");
        sb.append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>\n");
        sb.append("</Voice>\n");
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
