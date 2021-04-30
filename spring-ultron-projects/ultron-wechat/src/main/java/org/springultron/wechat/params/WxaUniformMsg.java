package org.springultron.wechat.params;

/**
 * 小程序和公众号统一的服务消息
 *
 * @author brucewuu
 * @date 2021/4/20 下午4:41
 */
public class WxaUniformMsg {
    /**
     * 用户openid，可以是小程序的openid，也可以是mp_template_msg.appid对应的公众号的openid
     */
    private String touser;
    /**
     * 小程序模板消息相关的信息（非必须）
     * 可以参考小程序模板消息接口; 有此节点则优先发送小程序模板消息
     */
    private WxaTemplate weapp_template_msg;
    /**
     * 公众号模板消息相关的信息
     * 可以参考公众号模板消息接口；有此节点并且没有weapp_template_msg节点时，发送公众号模板消息
     */
    private MsgTemplate mp_template_msg;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public WxaTemplate getWeapp_template_msg() {
        return weapp_template_msg;
    }

    public void setWeapp_template_msg(WxaTemplate weapp_template_msg) {
        this.weapp_template_msg = weapp_template_msg;
    }

    public MsgTemplate getMp_template_msg() {
        return mp_template_msg;
    }

    public void setMp_template_msg(MsgTemplate mp_template_msg) {
        this.mp_template_msg = mp_template_msg;
    }

    public static class MsgTemplate {
        /**
         * 公众号appid，要求与小程序有绑定且同主体
         */
        private String appid;
        /**
         * 公众号模板id
         */
        private String template_id;
        /**
         * 公众号模板消息所要跳转的url
         */
        private String url;
        /**
         * 公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系
         */
        private MiniProgram miniprogram;
        /**
         * 公众号模板消息的数据
         */
        private TemplateData data;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public MiniProgram getMiniprogram() {
            return miniprogram;
        }

        public void setMiniprogram(MiniProgram miniprogram) {
            this.miniprogram = miniprogram;
        }

        public TemplateData getData() {
            return data;
        }

        public void setData(TemplateData data) {
            this.data = data;
        }
    }

}
