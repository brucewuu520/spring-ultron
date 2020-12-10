package org.springultron.swagger.knife4j;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * Knife4j增强功能配置
 *
 * @author brucewuu
 * @date 2020/1/6 11:43
 */
@ConfigurationProperties(prefix = "swagger.knife4j")
public class Knife4jProperties {
    /**
     * 是否开启Knife4j增强模式（默认：开启）
     */
    private boolean enable = true;
    /**
     * 是否是生产环境（默认：false）
     */
    private boolean production = false;
    /**
     * Http Basic验证配置（文档安全）
     */
    @NestedConfigurationProperty
    private Knife4jHttpBasic basic;
    /**
     * 个性化配置
     */
    @NestedConfigurationProperty
    private OpenApiExtendSetting setting;
    /**
     * 是否支持MarkDown扩展
     */
    private boolean enableMarkdown = false;
    /**
     * MarkDown分组文档集合
     */
    @NestedConfigurationProperty
    private List<MarkdownProperty> documents;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public Knife4jHttpBasic getBasic() {
        return basic;
    }

    public void setBasic(Knife4jHttpBasic basic) {
        this.basic = basic;
    }

    public OpenApiExtendSetting getSetting() {
        return setting;
    }

    public void setSetting(OpenApiExtendSetting setting) {
        this.setting = setting;
    }

    public boolean isEnableMarkdown() {
        return enableMarkdown;
    }

    public void setEnableMarkdown(boolean enableMarkdown) {
        this.enableMarkdown = enableMarkdown;
    }

    public List<MarkdownProperty> getDocuments() {
        return documents;
    }

    public void setDocuments(List<MarkdownProperty> documents) {
        this.documents = documents;
    }

    /**
     * 接口文档Http Basic认证登录配置
     * <p>
     * 文档安全
     * </p>
     */
    public static class Knife4jHttpBasic {
        /**
         * Http Basic是否开启（默认为不开启）
         */
        private boolean enable = false;
        /**
         * Http Basic 用户名（默认：admin）
         */
        private String username;
        /**
         * Http Basic 密码（默认：123321）
         */
        private String password;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
