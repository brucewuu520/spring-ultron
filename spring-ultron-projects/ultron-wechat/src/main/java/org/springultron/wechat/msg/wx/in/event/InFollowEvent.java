package org.springultron.wechat.msg.wx.in.event;

/**
 * 接收 关注/取消关注事件
 * event=subscribe/unsubscribe
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:30
 */
public class InFollowEvent extends InEventMsg {
    // 订阅：subscribe
    public static final String SUBSCRIBE = "subscribe";
    // 取消订阅：unsubscribe
    public static final String UNSUBSCRIBE = "unsubscribe";

    public InFollowEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
    }

    /**
     * 是否关注
     */
    public boolean isSubscribe() {
        return SUBSCRIBE.equals(event);
    }

    /**
     * 是否取消关注
     */
    public boolean isUnsubscribe() {
        return UNSUBSCRIBE.equals(event);
    }
}
