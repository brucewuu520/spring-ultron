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

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.filter.basic.JakartaServletSecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.extend.filter.basic.ServletSecurityBasicAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springultron.doc.extension.Knife4jJakartaOperationCustomizer;
import org.springultron.doc.extension.Knife4jOpenApiCustomizer;
import org.springultron.doc.filter.ProductionSecurityFilter;
import org.springultron.doc.utils.EnvironmentUtils;

/***
 * Knife4j 基础自动配置类
 *
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2019/08/28 21:08
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({Knife4jProperties.class})
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
public class Knife4jAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(Knife4jAutoConfiguration.class);

    private final Knife4jProperties properties;
    private final Environment environment;

    @Autowired
    public Knife4jAutoConfiguration(Knife4jProperties properties, Environment environment) {
        this.properties = properties;
        this.environment = environment;
    }

    /**
     * 增强自定义配置
     */
    @Bean
    @ConditionalOnMissingBean
    public Knife4jOpenApiCustomizer knife4jOpenApiCustomizer() {
        if (logger.isDebugEnabled()) {
            logger.debug("Register Knife4jOpenApiCustomizer");
        }
        return new Knife4jOpenApiCustomizer(this.properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public Knife4jJakartaOperationCustomizer knife4jJakartaOperationCustomizer() {
        return new Knife4jJakartaOperationCustomizer();
    }

    /**
     * 配置Cors
     *
     * @since 2.0.4
     */
    @Bean("knife4jCorsFilter")
    @ConditionalOnMissingBean(CorsFilter.class)
    @ConditionalOnProperty(name = "knife4j.cors", havingValue = "true")
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(10000L);
        // 匹配所有API
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * Security with Basic Http
     *
     * @param knife4jProperties Basic Properties
     * @return BasicAuthFilter
     */
    @Bean
    @ConditionalOnMissingBean(ServletSecurityBasicAuthFilter.class)
    @ConditionalOnProperty(name = "knife4j.basic.enable", havingValue = "true")
    public JakartaServletSecurityBasicAuthFilter securityBasicAuthFilter(Knife4jProperties knife4jProperties) {
        JakartaServletSecurityBasicAuthFilter authFilter = new JakartaServletSecurityBasicAuthFilter();
        if (knife4jProperties == null) {
            authFilter.setEnableBasicAuth(EnvironmentUtils.resolveBool(environment, "knife4j.basic.enable", Boolean.FALSE));
            authFilter.setUserName(EnvironmentUtils.resolveString(environment, "knife4j.basic.username", GlobalConstants.BASIC_DEFAULT_USERNAME));
            authFilter.setPassword(EnvironmentUtils.resolveString(environment, "knife4j.basic.password", GlobalConstants.BASIC_DEFAULT_PASSWORD));
        } else {
            if (knife4jProperties.getBasic() == null) {
                authFilter.setEnableBasicAuth(Boolean.FALSE);
                authFilter.setUserName(GlobalConstants.BASIC_DEFAULT_USERNAME);
                authFilter.setPassword(GlobalConstants.BASIC_DEFAULT_PASSWORD);
            } else {
                authFilter.setEnableBasicAuth(knife4jProperties.getBasic().isEnable());
                authFilter.setUserName(knife4jProperties.getBasic().getUsername());
                authFilter.setPassword(knife4jProperties.getBasic().getPassword());
                authFilter.addRule(knife4jProperties.getBasic().getIncludes());
            }
        }
        return authFilter;
    }

    @Bean
    @ConditionalOnMissingBean(ProductionSecurityFilter.class)
    @ConditionalOnProperty(name = "knife4j.production", havingValue = "true")
    public ProductionSecurityFilter productionSecurityFilter(Environment environment) {
        boolean prod = false;
        ProductionSecurityFilter filter;
        if (properties == null) {
            if (environment != null) {
                String prodStr = environment.getProperty("knife4j.production");
                if (logger.isDebugEnabled()) {
                    logger.debug("swagger.production:{}", prodStr);
                }
                prod = Boolean.parseBoolean(prodStr);
            }
            filter = new ProductionSecurityFilter(prod);
        } else {
            filter = new ProductionSecurityFilter(properties.isProduction());
        }
        return filter;
    }

}
