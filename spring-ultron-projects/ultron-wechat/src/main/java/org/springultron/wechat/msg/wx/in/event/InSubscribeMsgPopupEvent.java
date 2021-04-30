package org.springultron.wechat.msg.wx.in.event;

import java.util.List;

/**
 * 用户操作订阅通知弹窗推送事件
 *
 * @author brucewuu
 * @date 2021/4/21 下午3:42
 */
public class InSubscribeMsgPopupEvent extends InEventMsg {
    /**
     * 用户点击“同意”
     */
    public static final String ACCEPT = "accept";
    /**
     * 用户点击“取消”
     */
    public static final String REJECT = "reject";

    /**
     * 订阅结果item（SubscribeMsgPopupEvent List）
     */
    private List<Item> items;

    public InSubscribeMsgPopupEvent(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, event);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        /**
         * 订阅模板id
         */
        private String templateId;
        /**
         * 订阅状态:
         * accept:同意
         * reject:拒绝
         */
        private String status;
        /**
         * 场景值:
         * 1:弹窗来自 H5 页面
         * 2:弹窗来自图文消息
         */
        private String popupScene;

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPopupScene() {
            return popupScene;
        }

        public void setPopupScene(String popupScene) {
            this.popupScene = popupScene;
        }
    }

}
