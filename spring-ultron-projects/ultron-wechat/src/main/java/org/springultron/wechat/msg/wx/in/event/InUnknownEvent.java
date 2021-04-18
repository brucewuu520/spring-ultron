package org.springultron.wechat.msg.wx.in.event;

/**
 * 未知事件
 *
 * @author brucewuu
 * @date 2021/4/14 下午1:44
 */
public class InUnknownEvent extends InEventMsg {
    /**
     * 事件原始xml字符串
     */
    private String xmlStr;

    public InUnknownEvent(String toUserName, String fromUserName, Integer createTime, String event, String xmlStr) {
        super(toUserName, fromUserName, createTime, event);
        this.xmlStr = xmlStr;
    }

    public String getXmlStr() {
        return xmlStr;
    }

    public void setXmlStr(String xmlStr) {
        this.xmlStr = xmlStr;
    }
}
