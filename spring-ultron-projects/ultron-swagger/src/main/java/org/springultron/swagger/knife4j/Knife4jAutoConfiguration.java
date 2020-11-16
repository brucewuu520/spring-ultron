package org.springultron.swagger.knife4j;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * knife4j增强功能自动化配置（默认不开启）
 *
 * @author brucewuu
 * @date 2020/1/6 11:42
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Knife4jProperties.class})
@ComponentScan(basePackages = {
        "com.github.xiaoymin.knife4j.spring.plugin"
})
@ConditionalOnProperty(name = "swagger.knife4j.enable", havingValue = "true", matchIfMissing = true)
class Knife4jAutoConfiguration {

    /**
     * MarkDown文档配置
     */
    @Bean(initMethod = "start")
    @ConditionalOnMissingBean(OpenApiExtensionResolver.class)
    OpenApiExtensionResolver markdownResolver(Knife4jProperties knife4jProperties) {
        OpenApiExtendSetting setting = knife4jProperties.getSetting();
        if (setting == null) {
            setting = new OpenApiExtendSetting();
        }
        return new OpenApiExtensionResolver(setting, knife4jProperties.getDocuments());
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties({Knife4jProperties.class})
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class SecurityFilterConfiguration{

        @Bean
        @ConditionalOnMissingBean(SecurityBasicAuthFilter.class)
        @ConditionalOnProperty(value = "swagger.knife4j.basic.enable")
        SecurityBasicAuthFilter securityBasicAuthFilter(Knife4jProperties knife4jProperties) {
            final Knife4jProperties.Knife4jHttpBasic httpBasic = knife4jProperties.getBasic();
            String defUsername = "admin", defPass = "123321";
            if (!StringUtils.isEmpty(httpBasic.getUsername())) {
                defUsername = httpBasic.getUsername();
            }
            if (!StringUtils.isEmpty(httpBasic.getPassword())) {
                defPass = httpBasic.getPassword();
            }
            return new SecurityBasicAuthFilter(true, defUsername, defPass);
        }

        @Bean
        @ConditionalOnMissingBean(ProductionSecurityFilter.class)
        @ConditionalOnProperty(name = "swagger.knife4j.production")
        ProductionSecurityFilter productionSecurityFilter() {
            return new ProductionSecurityFilter(true);
        }
    }
}
