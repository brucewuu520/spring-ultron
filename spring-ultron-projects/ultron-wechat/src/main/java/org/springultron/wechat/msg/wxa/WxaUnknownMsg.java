package org.springultron.wechat.msg.wxa;

/**
 * 未知的小程序消息
 *
 * @author brucewuu
 * @date 2021/4/16 下午8:36
 */
public class WxaUnknownMsg extends WxaMsg {
    /**
     * 消息字符串
     */
    private String msgStr;

    public WxaUnknownMsg(MsgModel msgModel) {
        super(msgModel);
    }

    public String getMsgStr() {
        return msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }
}
