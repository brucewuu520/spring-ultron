package org.springultron.swagger.knife4j;

import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.spring.model.MarkdownFiles;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * knife4j增强自动化配置
 *
 * @author brucewuu
 * @date 2020/1/6 11:42
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Knife4jProperties.class})
@ComponentScan(basePackages = {
        "com.github.xiaoymin.knife4j.spring.plugin",
        "com.github.xiaoymin.knife4j.spring.web"
})
public class Knife4jAutoConfiguration {
    /**
     * 初始化自定义Markdown特性
     *
     * @param knife4jProperties 配置文件
     * @return markdownFiles
     */
    @Bean(initMethod = "init")
    public MarkdownFiles markdownFiles(Knife4jProperties knife4jProperties) {
        return new MarkdownFiles(knife4jProperties.getMarkdowns() == null ? "" : knife4jProperties.getMarkdowns());
    }

    @ConditionalOnProperty(value = "swagger.knife4j.basic.enable")
    @Bean
    public SecurityBasicAuthFilter securityBasicAuthFilter(Knife4jProperties knife4jProperties) {
        final Knife4jHttpBasic httpBasic = knife4jProperties.getBasic();
        return new SecurityBasicAuthFilter(httpBasic.isEnable(), httpBasic.getUsername(), httpBasic.getPassword());
    }

    @ConditionalOnProperty(value = "swagger.knife4j.production")
    @Bean
    public ProductionSecurityFilter productionSecurityFilter() {
        return new ProductionSecurityFilter(true);
    }
}
