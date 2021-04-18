package org.springultron.wechat.msg.wx.in.event;

import org.springultron.wechat.msg.wx.in.InMsg;

/**
 * 接收事件推送
 * msgType=event
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:28
 */
public abstract class InEventMsg extends InMsg {
    private static final String MSG_TYPE = "event";

    /**
     * 事件类型
     */
    protected String event;

    public InEventMsg(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, MSG_TYPE);
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
