package org.springultron.wechat.msg.wxa;

/**
 * 用户进入会话事件
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:32
 */
public class WxaUserEnterSessionMsg extends WxaMsg {
    /**
     * 事件
     */
    private String event;
    /**
     * session来源
     */
    private String sessionFrom;

    public WxaUserEnterSessionMsg(MsgModel msgModel) {
        super(msgModel);
        this.event = msgModel.getEvent();
        this.sessionFrom = msgModel.getSessionFrom();
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }
}
