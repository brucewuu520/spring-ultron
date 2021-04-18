package org.springultron.wechat.msg.wxa;

/**
 * 小程序消息
 *
 * @author brucewuu
 * @date 2021/4/16 下午3:37
 */
public abstract class WxaMsg {
    /**
     * 开发者微信号
     */
    protected String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    protected String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    protected Integer createTime;
    /**
     * 消息类型
     * 1：text 文本消息
     * 2：image 图片消息
     * 3: event 事件消息
     * 4: miniprogrampage 小程序卡片消息
     */
    protected String msgType;

    public WxaMsg(MsgModel msgModel) {
        this.toUserName = msgModel.getToUserName();
        this.fromUserName = msgModel.getFromUserName();
        this.createTime = msgModel.getCreateTime();
        this.msgType = msgModel.getMsgType();
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
