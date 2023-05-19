package org.springultron.doc.configuration;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * 文档信息
 *
 * @author brucewuu
 * @date 2023/5/12 21:51
 */
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {
    /**
     * 文档标题（默认：Api Documentation）
     */
    private String title = "Api Documentation";
    /**
     * 文档描述（默认：Api Documentation）
     */
    private String description = "Api Documentation";
    /**
     * 版本号（默认：1.0）
     */
    private String version = "1.0";
    /**
     * 服务地址
     */
    private String termsOfService = null;
    /**
     * 开发者信息
     */
    @NestedConfigurationProperty
    private Contact contact;
    /**
     * License 信息
     */
    @NestedConfigurationProperty
    private License license;
    /**
     * 概要
     */
    @OpenAPI31
    private String summary;
    /**
     * 扩展信息
     */
    @NestedConfigurationProperty
    private Map<String, Object> extensions;

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

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public Info getInfo() {
        return new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(contact)
                .termsOfService(termsOfService)
                .license(license)
                .summary(summary)
                .extensions(extensions);
    }
}
