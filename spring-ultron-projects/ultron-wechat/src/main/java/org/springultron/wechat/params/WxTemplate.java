package org.springultron.wechat.params;

import org.springultron.core.jackson.Jackson;

/**
 * 公众号消息模板
 *
 * @author brucewuu
 * @date 2021/4/21 下午3:14
 */
public class WxTemplate {
    /**
     * 接收者（用户）的 openid
     */
    private String touser;
    /**
     * 所需下发的订阅模板id
     */
    private String template_id;
    /**
     * 跳转网页时填写（非必须）
     * page 和 miniprogram 同时不填，无跳转；
     * page 和 miniprogram 同时填写，优先跳转小程序；
     */
    private String page;
    /**
     * 跳转小程序信息（非必须）
     * page 和 miniprogram 同时不填，无跳转；
     * page 和 miniprogram 同时填写，优先跳转小程序；
     */
    private MiniProgram miniprogram;
    /**
     * 模板内容
     */
    private TemplateData data;

    @Override
    public String toString() {
        return Jackson.toJson(this);
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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
