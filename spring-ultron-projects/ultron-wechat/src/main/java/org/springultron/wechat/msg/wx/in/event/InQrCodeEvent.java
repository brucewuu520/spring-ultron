package org.springultron.wechat.msg.wx.in.event;

/**
 * 接收 扫描带参数二维码事件
 * event=subscribe/SCAN
 * <p>
 * 1.如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者（event=subscribe）
 * 2.如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者（event=SCAN）
 * </p>
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:35
 */
public class InQrCodeEvent extends InEventMsg {
    /**
     * 1.用户未关注时，进行关注后的事件推送：event=subscribe
     */
    public static final String QRCODE_SUBSCRIBE = "subscribe";
    /**
     * 2.用户已关注时的事件推送：event=SCAN
     */
    public static final String QRCODE_SCAN = "SCAN";

    /**
     * 事件KEY值
     * 1.用户未关注时，进行关注后的事件推送：qrscene_为前缀，后面为二维码的参数值
     * 2.用户已关注时的事件推送：是一个32位无符号整数，即创建二维码时的二维码scene_id
     */
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String ticket;

    public InQrCodeEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    /**
     * 是否关注事件推送
     */
    public boolean isSubscribe() {
        return QRCODE_SUBSCRIBE.equals(event);
    }

    /**
     * 是否已关注扫码的事件推送
     */
    public boolean isScan() {
        return QRCODE_SCAN.equals(event);
    }
}
