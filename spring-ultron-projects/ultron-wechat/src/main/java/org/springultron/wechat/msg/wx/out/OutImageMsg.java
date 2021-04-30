package org.springultron.wechat.msg.wx.out;

import org.springultron.wechat.msg.wx.in.InMsg;

/**
 * 回复图片消息
 *
 * @author brucewuu
 * @date 2021/4/15 下午5:43
 */
public class OutImageMsg extends OutMsg {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String mediaId;

    public OutImageMsg() {
        this.msgType = "image";
    }

    public OutImageMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = "image";
    }

    @Override
    protected void customizeXml(StringBuilder sb) {
        if (null == mediaId) {
            throw new NullPointerException("mediaId can not be null.");
        }
        sb.append("<Image>\n");
        sb.append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>\n");
        sb.append("</Image>\n");
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
