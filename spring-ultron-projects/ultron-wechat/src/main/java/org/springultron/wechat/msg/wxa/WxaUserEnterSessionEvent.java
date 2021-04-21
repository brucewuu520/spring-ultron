package org.springultron.wechat.msg.wxa;

/**
 * 用户进入会话事件
 * 用户在小程序“客服会话按钮”进入客服会话时将产生如下数据包
 *
 * @author brucewuu
 * @date 2021/4/16 下午6:32
 */
public class WxaUserEnterSessionEvent extends WxaMsg {
    /**
     * 事件类型，user_enter_tempsession
     */
    private String event;
    /**
     * 开发者在客服会话按钮设置的 session-from 属性
     */
    private String sessionFrom;

    public WxaUserEnterSessionEvent(MsgModel msgModel) {
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
