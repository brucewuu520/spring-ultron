/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springultron.doc.configuration;

import com.github.xiaoymin.knife4j.core.enums.OpenAPILanguageEnums;

/**
 * knife4j 配置
 *
 * @author brucewuu
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2023/5/6 13:18
 */
public class Knife4jSetting {

    /**
     * Custom response HTTP status code after production environment screening(Knife4j.production=true)
     */
    private Integer customCode = 200;
    /**
     * i18n
     */
    private OpenAPILanguageEnums language = OpenAPILanguageEnums.ZH_CN;
    /**
     * Whether to display the Swagger Models function in the Ui Part.
     */
    private boolean enableSwaggerModels = true;
    /**
     * Rename Swagger model name,default is `Swagger Models`
     */
    private String swaggerModelName = "Swagger Models";

    /**
     * Whether to display the refresh variable button after each debug debugging bar, which is not displayed by default
     */
    private boolean enableReloadCacheParameter = false;

    /**
     * Whether the debug tab displays the afterScript function is enabled by default
     */
    private boolean enableAfterScript = true;

    /**
     * Whether to display the "document management" function in the Ui Part.
     */
    private boolean enableDocumentManage = true;
    /**
     * Whether to enable the version control of an interface in the interface. If it is enabled, the UI interface will have small blue dots after the backend changes
     */
    private boolean enableVersion = false;

    /**
     * Whether to enable request parameter cache
     */
    private boolean enableRequestCache = true;

    /**
     * For the interface request type of RequestMapping, if the parameter type is not specified, seven types of interface address parameters will be displayed by default if filtering is not performed. If this configuration is enabled, an interface address of post type will be displayed by default
     */
    private boolean enableFilterMultipartApis = false;

    /**
     * Filter Method type
     */
    private String enableFilterMultipartApiMethodType = "POST";

    /**
     * Enable host
     */
    private boolean enableHost = false;

    /**
     * HostAddress after enabling host
     */
    private String enableHostText = "";

    /**
     * Whether to enable dynamic request parameters
     */
    private boolean enableDynamicParameter = false;

    /**
     * Enable debug mode，default is true.
     */
    private boolean enableDebug = true;

    /**
     * Display bottom footer by default
     */
    private boolean enableFooter = true;
    /**
     * Customize footer
     */
    private boolean enableFooterCustom = false;

    /**
     * Custom footer content (support Markdown syntax)
     */
    private String footerCustomContent;

    /**
     * Show search box
     */
    private boolean enableSearch = true;

    /**
     * Whether to display the tab box of the original structure of OpenAPI, which is displayed by default
     */
    private boolean enableOpenApi = true;

    /**
     * Whether to enable home page custom configuration, false by default
     */
    private boolean enableHomeCustom = false;

    /**
     * Customize Markdown document path of home page
     */
    private String homeCustomLocation;

    /**
     * Customize Markdown document path of home page
     */
    private String homeCustomPath;

    /**
     * Whether to display the group drop-down box, the default is true (that is, display). In general, if it is a single group, you can set this property to false, that is, the group will not be displayed, so you don't need to select it.
     */
    private boolean enableGroup = true;

    /**
     * Whether to display the response status code bar
     * <a href="https://gitee.com/xiaoym/knife4j/issues/I3TE0V" />
     */
    private boolean enableResponseCode = true;

    public Integer getCustomCode() {
        return customCode;
    }

    public void setCustomCode(Integer customCode) {
        this.customCode = customCode;
    }

    public OpenAPILanguageEnums getLanguage() {
        return language;
    }

    public void setLanguage(OpenAPILanguageEnums language) {
        this.language = language;
    }

    public boolean isEnableSwaggerModels() {
        return enableSwaggerModels;
    }

    public void setEnableSwaggerModels(boolean enableSwaggerModels) {
        this.enableSwaggerModels = enableSwaggerModels;
    }

    public String getSwaggerModelName() {
        return swaggerModelName;
    }

    public void setSwaggerModelName(String swaggerModelName) {
        this.swaggerModelName = swaggerModelName;
    }

    public boolean isEnableReloadCacheParameter() {
        return enableReloadCacheParameter;
    }

    public void setEnableReloadCacheParameter(boolean enableReloadCacheParameter) {
        this.enableReloadCacheParameter = enableReloadCacheParameter;
    }

    public boolean isEnableAfterScript() {
        return enableAfterScript;
    }

    public void setEnableAfterScript(boolean enableAfterScript) {
        this.enableAfterScript = enableAfterScript;
    }

    public boolean isEnableDocumentManage() {
        return enableDocumentManage;
    }

    public void setEnableDocumentManage(boolean enableDocumentManage) {
        this.enableDocumentManage = enableDocumentManage;
    }

    public boolean isEnableVersion() {
        return enableVersion;
    }

    public void setEnableVersion(boolean enableVersion) {
        this.enableVersion = enableVersion;
    }

    public boolean isEnableRequestCache() {
        return enableRequestCache;
    }

    public void setEnableRequestCache(boolean enableRequestCache) {
        this.enableRequestCache = enableRequestCache;
    }

    public boolean isEnableFilterMultipartApis() {
        return enableFilterMultipartApis;
    }

    public void setEnableFilterMultipartApis(boolean enableFilterMultipartApis) {
        this.enableFilterMultipartApis = enableFilterMultipartApis;
    }

    public String getEnableFilterMultipartApiMethodType() {
        return enableFilterMultipartApiMethodType;
    }

    public void setEnableFilterMultipartApiMethodType(String enableFilterMultipartApiMethodType) {
        this.enableFilterMultipartApiMethodType = enableFilterMultipartApiMethodType;
    }

    public boolean isEnableHost() {
        return enableHost;
    }

    public void setEnableHost(boolean enableHost) {
        this.enableHost = enableHost;
    }

    public String getEnableHostText() {
        return enableHostText;
    }

    public void setEnableHostText(String enableHostText) {
        this.enableHostText = enableHostText;
    }

    public boolean isEnableDynamicParameter() {
        return enableDynamicParameter;
    }

    public void setEnableDynamicParameter(boolean enableDynamicParameter) {
        this.enableDynamicParameter = enableDynamicParameter;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public boolean isEnableFooter() {
        return enableFooter;
    }

    public void setEnableFooter(boolean enableFooter) {
        this.enableFooter = enableFooter;
    }

    public boolean isEnableFooterCustom() {
        return enableFooterCustom;
    }

    public void setEnableFooterCustom(boolean enableFooterCustom) {
        this.enableFooterCustom = enableFooterCustom;
    }

    public String getFooterCustomContent() {
        return footerCustomContent;
    }

    public void setFooterCustomContent(String footerCustomContent) {
        this.footerCustomContent = footerCustomContent;
    }

    public boolean isEnableSearch() {
        return enableSearch;
    }

    public void setEnableSearch(boolean enableSearch) {
        this.enableSearch = enableSearch;
    }

    public boolean isEnableOpenApi() {
        return enableOpenApi;
    }

    public void setEnableOpenApi(boolean enableOpenApi) {
        this.enableOpenApi = enableOpenApi;
    }

    public boolean isEnableHomeCustom() {
        return enableHomeCustom;
    }

    public void setEnableHomeCustom(boolean enableHomeCustom) {
        this.enableHomeCustom = enableHomeCustom;
    }

    public String getHomeCustomLocation() {
        return homeCustomLocation;
    }

    public void setHomeCustomLocation(String homeCustomLocation) {
        this.homeCustomLocation = homeCustomLocation;
    }

    public String getHomeCustomPath() {
        return homeCustomPath;
    }

    public void setHomeCustomPath(String homeCustomPath) {
        this.homeCustomPath = homeCustomPath;
    }

    public boolean isEnableGroup() {
        return enableGroup;
    }

    public void setEnableGroup(boolean enableGroup) {
        this.enableGroup = enableGroup;
    }

    public boolean isEnableResponseCode() {
        return enableResponseCode;
    }

    public void setEnableResponseCode(boolean enableResponseCode) {
        this.enableResponseCode = enableResponseCode;
    }

}
