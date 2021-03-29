package org.springultron.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger文档自定义配置
 *
 * @author brucewuu
 * @date 2019-06-27 19:40
 */
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /**
     * 是否开启文档（默认：开启）
     */
    private boolean enable = true;
    /**
     * 文档标题（默认：Api Documentation）
     */
    private String title;
    /**
     * 文档描述（默认：Api Documentation）
     */
    private String description;
    /**
     * 版本号（默认：V1.0）
     */
    private String version = "V1.0";
    /**
     * 服务地址
     */
    private String termsOfServiceUrl;
    /**
     * 分组地址（默认：default）
     */
    private String groupName;
    /**
     * 开发者信息
     */
    private Contact contact;
    /**
     * 全局统一鉴权配置
     **/
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

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }


    public static class Contact {
        /**
         * 开发者姓名
         */
        private String name;
        /**
         * 开发者主页
         */
        private String url;
        /**
         * 开发者邮箱
         */
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * Authorization配置
     */
    public static class Authorization {
        /**
         * 开启Authorization（默认：false）
         */
        private boolean enabled = false;
        /**
         * 鉴权策略ID，对应 SecurityReferences ID（默认：BearerToken）
         */
        private String name = "BearerToken";
        /**
         * 鉴权传递的Header参数（默认：Authorization）
         */
        private String headerName = "Authorization";
        /**
         * 需要开启鉴权URL的正则（默认：^.*$）
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
