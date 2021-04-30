package org.springultron.wechat.params;

import org.springultron.core.jackson.Jackson;

/**
 * 小程序模版封装
 *
 * @author brucewuu
 * @date 2021/4/20 下午2:21
 */
public class WxaTemplate {
    /**
     * 接收者（用户）的 openid
     */
    private String touser;
    /**
     * 所需下发的订阅模板id
     */
    private String template_id;
    /**
     * 点击模板卡片后的跳转页面（非必须）
     * 仅限本小程序内的页面。支持带参数（示例index?foo=bar）
     * 该字段不填则模板无跳转
     */
    private String page;
    /**
     * 模板内容，格式形如:{ "key1": { "value": any }, "key2": { "value": any } }
     */
    private TemplateData data;
    /**
     * 跳转小程序类型：
     * developer(开发版)
     * trial(体验版)
     * formal(正式版)
     * 默认为formal(正式版)
     */
    private String miniprogram_state;
    /**
     * 进入小程序查看的语言类型：
     * zh_CN(简体中文)
     * en_US(英文)
     * zh_HK(繁体中文)
     * zh_TW(繁体中文)
     * 默认为zh_CN(简体中文)
     */
    private String lang;
    /**
     * 小程序模板消息formid
     */
    private String form_id;
    /**
     * 小程序模板放大关键词
     */
    private String emphasis_keyword;

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

    public TemplateData getData() {
        return data;
    }

    public void setData(TemplateData data) {
        this.data = data;
    }

    public String getMiniprogram_state() {
        return miniprogram_state;
    }

    public void setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }

}
