package org.springultron.wechat.msg.wx.out;

import org.springultron.core.utils.StringUtils;
import org.springultron.wechat.msg.wx.in.InMsg;

/**
 * 回复视频消息
 *
 * @author brucewuu
 * @date 2021/4/15 下午5:49
 */
public class OutVideoMsg extends OutMsg {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String mediaId;
    /**
     * 视频标题（非必须）
     */
    private String title;
    /**
     * 视频描述（非必须）
     */
    private String description;

    public OutVideoMsg() {
        this.msgType = "video";
    }

    public OutVideoMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = "video";
    }

    @Override
    protected void customizeXml(StringBuilder sb) {
        if (null == mediaId) {
            throw new NullPointerException("mediaId can not be null.");
        }
        sb.append("<Video>\n");
        sb.append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>\n");
        if (StringUtils.isNotEmpty(title)) {
            sb.append("<Title><![CDATA[").append(title).append("]]></Title>\n");
        }
        if (StringUtils.isNotEmpty(description)) {
            sb.append("<Description><![CDATA[").append(description).append("]]></Description>\n");
        }
        sb.append("</Video>\n");
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
