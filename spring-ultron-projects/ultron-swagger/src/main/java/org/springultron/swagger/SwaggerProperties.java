package org.springultron.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Swagger 自定义配置
 *
 * @author brucewuu
 * @date 2019-06-27 19:40
 */
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /**
     * 是否开启 swagger，默认：true
     */
    private boolean enable = true;
    /**
     * 标题，默认：在线接口文档
     */
    private String title;
    /**
     * 详情，默认：在线接口文档
     */
    private String description;
    /**
     * 版本号，默认：V1.0
     */
    private String version = "V1.0";
    /**
     * 联系人
     */
    private String contactUser;
    /**
     * 联系网址
     */
    private String contactUrl;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 全局统一鉴权配置
     **/
    @NestedConfigurationProperty
    private Authorization authorization = new Authorization();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public static class Authorization {
        /**
         * 开启Authorization，默认：false
         */
        private boolean enabled = false;
        /**
         * 鉴权策略ID，对应 SecurityReferences ID，默认：BearerToken
         */
        private String name = "BearerToken";
        /**
         * 鉴权传递的Header参数，默认：Authorization
         */
        private String headerName = "Authorization";
        /**
         * 需要开启鉴权URL的正则，默认：^.*$
         */
        private String authRegex = "^.*$";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getAuthRegex() {
            return authRegex;
        }

        public void setAuthRegex(String authRegex) {
            this.authRegex = authRegex;
        }
    }
}
