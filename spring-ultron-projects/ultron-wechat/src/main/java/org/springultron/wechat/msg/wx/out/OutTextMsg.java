package org.springultron.wechat.msg.wx.out;

import org.springframework.util.Assert;
import org.springultron.wechat.msg.wx.in.InMsg;

/**
 * 回复文本消息
 *
 * @author brucewuu
 * @date 2021/4/14 下午7:27
 */
public class OutTextMsg extends OutMsg {
    /**
     * 文本消息内容
     */
    private String content;

    public OutTextMsg() {
        this.msgType = "text";
    }

    public OutTextMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = "text";
    }

    @Override
    protected void customizeXml(StringBuilder sb) {
        Assert.notNull(content, "content can not be null.");
        sb.append("<Content><![CDATA[").append(content).append("]]></Content>\n");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
