package org.springultron.wechat.msg.wx.in.event;

/**
 * 接收 上报地理位置事件
 * event=LOCATION
 * 用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，
 * 或在进入会话后每5秒上报一次地理位置，公众号可以在公众平台网站中修改以上设置；
 * 上报地理位置时，微信会将上报地理位置事件推送到开发者填写的URL。
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:55
 */
public class InLocationEvent extends InEventMsg {
    /**
     * 地理位置纬度
     */
    private String latitude;
    /**
     * 地理位置经度
     */
    private String longitude;
    /**
     * 地理位置精度
     */
    private String precision;

    public InLocationEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
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

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }
}
