package org.springultron.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger 自定义配置
 *
 * @Auther: brucewuu
 * @Date: 2019-06-27 19:40
 * @Description:
 */
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * 是否开启 swagger，默认：true
     */
    private boolean enable = true;
    /**
     * 标题，默认：XXX服务
     */
    private String title;
    /**
     * 详情，默认：XXX服务
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
}
