package org.springultron.wechat.msg.wx.in;

/**
 * 未知消息
 *
 * @author brucewuu
 * @date 2021/4/14 下午1:46
 */
public class InUnknownMsg extends InMsg {
    /**
     * 消息原始xml字符串
     */
    private String xmlStr;

    public InUnknownMsg(String toUserName, String fromUserName, Integer createTime, String msgType, String xmlStr) {
        super(toUserName, fromUserName, createTime, msgType);
        this.xmlStr = xmlStr;
    }

    public String getXmlStr() {
        return xmlStr;
    }

    public void setXmlStr(String xmlStr) {
        this.xmlStr = xmlStr;
    }
}
