package org.springultron.wechat.msg.wxa;

/**
 * 文本消息
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:19
 */
public class WxaTextMsg extends WxaMsg {
    /**
     * 消息id（64位整型）
     */
    private String msgId;
    /**
     * 文本消息内容
     */
    private String content;

    public WxaTextMsg(MsgModel msgModel) {
        super(msgModel);
        this.msgId = msgModel.getMsgId();
        this.content = msgModel.getContent();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
