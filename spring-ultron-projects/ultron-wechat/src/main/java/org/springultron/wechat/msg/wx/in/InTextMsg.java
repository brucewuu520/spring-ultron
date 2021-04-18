package org.springultron.wechat.msg.wx.in;

/**
 * 接收文本消息
 * msgType=text
 *
 * @author brucewuu
 * @date 2021/4/14 上午10:53
 */
public class InTextMsg extends InMsg {
    /**
     * 消息ID（64位整型）
     */
    private String msgId;
    /**
     * 文本消息内容
     */
    private String content;

    public InTextMsg(String toUserName, String fromUserName, Integer createTime, String msgType) {
        super(toUserName, fromUserName, createTime, msgType);
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
