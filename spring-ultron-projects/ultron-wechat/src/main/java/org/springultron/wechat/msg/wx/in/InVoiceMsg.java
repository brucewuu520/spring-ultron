package org.springultron.wechat.msg.wx.in;

import org.springultron.core.utils.StringUtils;

/**
 * 接收语音消息
 * msgType=voice
 *
 * @author brucewuu
 * @date 2021/4/14 上午10:57
 */
public class InVoiceMsg extends InMsg {
    /**
     * 消息ID（64位整型）
     */
    private String msgId;
    /**
     * 语音消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String mediaId;
    /**
     * 语音格式，如amr，speex等
     */
    private String format;
    /**
     * 语音识别结果，UTF8编码
     * 请注意，开通语音识别后，用户每次发送语音给公众号时，微信会在推送的语音消息XML数据包中，增加一个Recognition字段
     * （注：由于客户端缓存，开发者开启或者关闭语音识别功能，对新关注者立刻生效，对已关注用户需要24小时生效。开发者可以重新关注此帐号进行测试）
     */
    private String recognition;

    public InVoiceMsg(String toUserName, String fromUserName, Integer createTime, String msgType) {
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    /**
     * 是否语音识别
     */
    public boolean isSpeechRecognition() {
        return StringUtils.isNotBlank(recognition);
    }
}
