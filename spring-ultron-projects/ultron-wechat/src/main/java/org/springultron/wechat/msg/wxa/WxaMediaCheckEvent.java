package org.springultron.wechat.msg.wxa;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * 校验图片/音频是否含有违法违规内容异步检测结果
 *
 * @author brucewuu
 * @date 2021/4/20 下午8:02
 */
public class WxaMediaCheckEvent extends WxaMsg {
    /**
     * 事件类型，wxa_media_check
     */
    private String event;
    /**
     * 检测结果，0：暂未检测到风险，1：风险
     */
    private int isrisky;
    /**
     * 附加信息，默认为空
     */
    @JsonAlias(value = "extra_info_json")
    private String extraInfoJson;
    /**
     * 小程序的appid
     */
    @JsonAlias(value = "appid")
    private String appId;
    /**
     * 任务id
     */
    @JsonAlias(value = "trace_id")
    private String traceId;
    /**
     * 默认为：0，4294966288(-1008)为链接无法下载
     */
    @JsonAlias(value = "status_code")
    private int statusCode;

    public WxaMediaCheckEvent(MsgModel msgModel) {
        super(msgModel);
        this.event = msgModel.getEvent();
        this.isrisky = msgModel.getIsrisky();
        this.extraInfoJson = msgModel.getExtraInfoJson();
        this.appId = msgModel.getAppid();
        this.traceId = msgModel.getTraceId();
        this.statusCode = msgModel.getStatusCode();
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getIsrisky() {
        return isrisky;
    }

    public void setIsrisky(int isrisky) {
        this.isrisky = isrisky;
    }

    public String getExtraInfoJson() {
        return extraInfoJson;
    }

    public void setExtraInfoJson(String extraInfoJson) {
        this.extraInfoJson = extraInfoJson;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
