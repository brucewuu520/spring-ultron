package org.springultron.wechat.msg.wx.in;

/**
 * 接收地理位置消息
 * msgType=location
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:07
 */
public class InLocationMsg extends InMsg {
    /**
     * 消息ID（64位整型）
     */
    private String msgId;
    /**
     * 地理位置纬度
     */
    private String latitude;
    /**
     * 地理位置经度
     */
    private String longitude;
    /**
     * 地图缩放大小
     */
    private String scale;
    /**
     * 地理位置信息
     */
    private String label;

    public InLocationMsg(String toUserName, String fromUserName, Integer createTime, String msgType) {
        super(toUserName, fromUserName, createTime, msgType);
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
