package org.springultron.wechat.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 微信配置项
 *
 * @author brucewuu
 * @date 2021/3/29 下午3:30
 */
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    /**
     * 是否开发模式，默认：false
     */
    private boolean devMode = false;
    /**
     * 公众号配置
     */
    @NestedConfigurationProperty
    private WxConf wxConf;
    /**
     * 小程序配置
     */
    @NestedConfigurationProperty
    private WxaConf wxaConf;

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public WxConf getWxConf() {
        return wxConf;
    }

    public void setWxConf(WxConf wxConf) {
        this.wxConf = wxConf;
    }

    public WxaConf getWxaConf() {
        return wxaConf;
    }

    public void setWxaConf(WxaConf wxaConf) {
        this.wxaConf = wxaConf;
    }

}
