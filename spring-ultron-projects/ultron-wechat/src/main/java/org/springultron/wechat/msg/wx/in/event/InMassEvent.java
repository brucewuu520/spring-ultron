package org.springultron.wechat.msg.wx.in.event;

/**
 * 群发结果推送事件
 * <p>
 * 由于群发任务提交后，群发任务可能在一定时间后才完成，因此，群发接口调用时，仅会给出群发任务是否提交成功的提示，
 * 若群发任务提交成功，则在群发任务结束时，会向开发者在公众平台填写的开发者URL（callback URL）推送事件。
 * 需要注意，由于群发任务彻底完成需要较长时间，将会在群发任务即将完成的时候，就推送群发结果，
 * 此时的推送人数数据将会与实际情形存在一定误差
 * </p>
 *
 * @author brucewuu
 * @date 2021/4/16 上午11:48
 */
public class InMassEvent extends InEventMsg {
    /**
     * 群发成功
     */
    public static final String SEND_SUCCESS = "sendsuccess";
    /**
     * 群发失败，其他失败情况是err(num)
     */
    public static final String SEND_FAIL = "sendfail";

    /**
     * 群发的消息ID
     */
    private String msgId;
    /**
     * 群发的结果，为“send success”或“send fail”或“err(num)”。
     * 但send success时，也有可能因用户拒收公众号的消息、系统错误等原因造成少量用户接收失败。
     * err(num)是审核失败的具体原因，可能的情况如下：
     * err(10001):涉嫌广告
     * err(20001):涉嫌政治
     * err(20004):涉嫌社会
     * err(20002):涉嫌色情
     * err(20006):涉嫌违法犯罪
     * err(20008):涉嫌欺诈
     * err(20013):涉嫌版权
     * err(22000):涉嫌互推(互相宣传)
     * err(21000):涉嫌其他
     * err(30001):原创校验出现系统错误且用户选择了被判为转载就不群发
     * err(30002): 原创校验被判定为不能群发
     * err(30003): 原创校验被判定为转载文且用户选择了被判为转载就不群发
     * err(40001)：管理员拒绝
     * err(40002)：管理员30分钟内无响应，超时
     */
    private String status;
    /**
     * tag_id下粉丝数；或者openid_list中的粉丝数
     */
    private String totalCount;
    /**
     * 过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后准备发送的粉丝数，
     * 原则上，FilterCount 约等于 SentCount + ErrorCount
     */
    private String filterCount;
    /**
     * 发送成功的粉丝数
     */
    private String sentCount;
    /**
     * 发送失败的粉丝数
     */
    private String errorCount;

    public InMassEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
    }
    
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(String filterCount) {
        this.filterCount = filterCount;
    }

    public String getSentCount() {
        return sentCount;
    }

    public void setSentCount(String sentCount) {
        this.sentCount = sentCount;
    }

    public String getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(String errorCount) {
        this.errorCount = errorCount;
    }
}
