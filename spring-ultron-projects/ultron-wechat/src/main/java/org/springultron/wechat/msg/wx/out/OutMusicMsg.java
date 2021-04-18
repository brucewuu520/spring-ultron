package org.springultron.wechat.msg.wx.out;

import org.springultron.core.utils.StringUtils;
import org.springultron.wechat.msg.wx.in.InMsg;

/**
 * 回复音乐消息
 *
 * @author brucewuu
 * @date 2021/4/15 下午5:56
 */
public class OutMusicMsg extends OutMsg {
    /**
     * 音乐标题（非必须）
     */
    private String title;
    /**
     * 音乐描述（非必须）
     */
    private String description;
    /**
     * 音乐链接（非必须）
     */
    private String musicUrl;
    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐（非必须）
     */
    private String hqMusicUrl;

    private String funcFlag = "0";

    public OutMusicMsg() {
        this.msgType = "music";
    }

    public OutMusicMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = "music";
    }

    @Override
    protected void customizeXml(StringBuilder sb) {
        sb.append("<Music>\n");
        if (StringUtils.isNotEmpty(title)) {
            sb.append("<Title><![CDATA[").append(title).append("]]></Title>\n");
        }
        if (StringUtils.isNotEmpty(description)) {
            sb.append("<Description><![CDATA[").append(description).append("]]></Description>\n");
        }
        if (StringUtils.isNotEmpty(musicUrl)) {
            sb.append("<MusicUrl><![CDATA[").append(musicUrl).append("]]></MusicUrl>\n");
        }
        if (StringUtils.isNotEmpty(hqMusicUrl)) {
            sb.append("<HQMusicUrl><![CDATA[").append(hqMusicUrl).append("]]></HQMusicUrl>\n");
        }
        sb.append("<FuncFlag>").append(funcFlag).append("</FuncFlag>\n");
        sb.append("</Music>\n");
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

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    public void setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
    }

    public String getFuncFlag() {
        return funcFlag;
    }

    public void setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
    }
}
