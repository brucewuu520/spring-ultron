package org.springultron.wechat.msg.wx.in.event;

/**
 * 接收 自定义菜单点击事件
 * event=CLICK/VIEW
 * 用户点击自定义菜单后，微信会把点击事件推送给开发者，请注意，点击菜单弹出子菜单，不会产生上报
 * <p>
 * 1.点击菜单拉取消息时的事件推送（event=CLICK）
 * 2.点击菜单跳转链接时的事件推送（event=VIEW）
 * </p>
 *
 * @author brucewuu
 * @date 2021/4/14 上午11:59
 */
public class InMenuEvent extends InEventMsg {
    /**
     * 1.点击菜单拉取消息时的事件推送：event=CLICK
     */
    public static final String MENU_CLICK = "CLICK";
    /**
     * 2.点击菜单跳转链接时的事件推送：event=VIEW
     */
    public static final String MENU_VIEW = "VIEW";
    /**
     * 3.scancode_push：扫码推事件
     */
    public static final String MENU_SCANCODE_PUSH = "scancode_push";
    /**
     * 4.scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框
     */
    public static final String MENU_SCANCODE_WAIT_MSG = "scancode_waitmsg";
    /**
     * 5.pic_sysphoto：弹出系统拍照发图
     */
    public static final String MENU_PIC_SYS_PHOTO = "pic_sysphoto";
    /**
     * 6.pic_photo_or_album：弹出拍照或者相册发图，先推送菜单事件，再推送图片消息
     */
    public static final String MENU_PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
    /**
     * 7.pic_weixin：弹出微信相册发图器
     */
    public static final String MENU_WX_PIC = "pic_weixin";
    /**
     * 8.location_select：弹出地理位置选择器
     */
    public static final String MENU_LOCATION_SELECT = "location_select";
    /**
     * 9.media_id：下发消息（除文本消息）
     */
    public static final String MENU_MEDIA_ID = "media_id";
    /**
     * 10.view_limited：跳转图文消息URL
     */
    public static final String MENU_VIEW_LIMITED = "view_limited";
    /**
     * 11.view_miniprogram：跳转微信小程序
     */
    public static final String MENU_VIEW_MINIPROGRAM = "view_miniprogram";

    /**
     * 事件KEY值
     * 1.点击菜单拉取消息时的事件推送：与自定义菜单接口中KEY值对应
     * 2.点击菜单跳转链接时的事件推送：设置的跳转URL
     */
    private String eventKey;
    /**
     * 菜单ID
     */
    private String menuId;
    /**
     * 扫描信息
     */
    private ScanCodeInfo scanCodeInfo;

    public InMenuEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public ScanCodeInfo getScanCodeInfo() {
        return scanCodeInfo;
    }

    public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
        this.scanCodeInfo = scanCodeInfo;
    }


    /**
     * 菜单二维码扫描信息
     */
    public static class ScanCodeInfo {
        /**
         * 扫描类型，一般是qrcode
         */
        private String scanType;
        /**
         * 扫描结果，即二维码对应的字符串信息
         */
        private String scanResult;

        public ScanCodeInfo(String scanType, String scanResult) {
            this.scanType = scanType;
            this.scanResult = scanResult;
        }

        public String getScanType() {
            return scanType;
        }

        public void setScanType(String scanType) {
            this.scanType = scanType;
        }

        public String getScanResult() {
            return scanResult;
        }

        public void setScanResult(String scanResult) {
            this.scanResult = scanResult;
        }
    }

}
