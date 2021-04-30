package org.springultron.wechat.msg.wx.in.event;

/**
 * 订阅通知发送结果推送事件
 * *失败仅包含因异步推送导致的系统失败
 *
 * @author brucewuu
 * @date 2021/4/21 下午4:26
 */
public class InSubscribeMsgSentEvent extends InEventMsg {
    /**
     * 消息ID（64位整型）
     */
    private String msgId;

    /**
     * 订阅模板id
     */
    private String templateId;
    /**
     * 推送结果状态码（0表示成功）
     */
    private String errorCode;
    /**
     * 推送结果状态码文字含义
     */
    private String errorMsg;

    public InSubscribeMsgSentEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
