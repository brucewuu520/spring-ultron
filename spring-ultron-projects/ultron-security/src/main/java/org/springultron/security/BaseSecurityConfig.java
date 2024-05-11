package org.springultron.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springultron.captcha.service.CaptchaService;
import org.springultron.core.utils.StringUtils;
import org.springultron.security.filter.JwtAuthenticationFilter;
import org.springultron.security.filter.LoginFilter;
import org.springultron.security.handler.SimpleAccessDeniedHandler;
import org.springultron.security.handler.SimpleAuthenticationEntryPoint;
import org.springultron.security.handler.SimpleLogoutHandler;
import org.springultron.security.handler.SimpleLogoutSuccessHandler;
import org.springultron.security.provider.CaptchaAuthenticationProvider;

/**
 * Spring Security 统一基础配置
 * <p>
 * 使用时继承BaseSecurityConfig，并注入{@link UserDetailsProcessor} bean 或者 {@link UserDetailsService} bean
 * </p>
 *
 * @author brucewuu
 * @date 2023/5/12 14:12
 */
@Repository
public abstract class BaseSecurityConfig {

    private UserDetailsProcessor userDetailsProcessor;
    private ApplicationContext context;

    @Primary
    @Autowired(required = false)
    public void setUserDetailsProcessor(UserDetailsProcessor userDetailsProcessor) {
        this.userDetailsProcessor = userDetailsProcessor;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CaptchaService.class)
    public CaptchaAuthenticationProvider fallbackCaptchaAuthenticationProvider(ObjectProvider<CaptchaService> captchaService, PasswordEncoder passwordEncoder, ObjectProvider<UserDetailsService> userDetailsServices) {
        System.err.println("--- fallbackCaptchaAuthenticationProvider --- init >>>");
        CaptchaAuthenticationProvider captchaAuthenticationProvider = new CaptchaAuthenticationProvider(captchaService.getIfAvailable(), passwordEncoder);
        captchaAuthenticationProvider.setUserDetailsService(userDetailsServices.getIfAvailable(() -> username -> userDetailsProcessor.loadUserByUsername(username)));
        return captchaAuthenticationProvider;
    }

    public void build(HttpSecurity http) throws Exception {
        buildHttpSecurity(http, false, null, userDetailsProcessor);
    }

    public void build(HttpSecurity http, String loginProcessingUrl) throws Exception {
        buildHttpSecurity(http, false, loginProcessingUrl, userDetailsProcessor);
    }

    public void build(HttpSecurity http, UserDetailsProcessor userDetailsProcessor) throws Exception {
        buildHttpSecurity(http, false, null, userDetailsProcessor);
    }

    public void build(HttpSecurity http, String loginProcessingUrl, UserDetailsProcessor userDetailsProcessor) throws Exception {
        buildHttpSecurity(http, false, loginProcessingUrl, userDetailsProcessor);
    }

    public void buildWithJwt(HttpSecurity http) throws Exception {
        buildHttpSecurity(http, true, null, userDetailsProcessor);
    }

    public void buildWithJwt(HttpSecurity http, UserDetailsProcessor userDetailsProcessor) throws Exception {
        buildHttpSecurity(http, true, null, userDetailsProcessor);
    }

    private void buildHttpSecurity(HttpSecurity http, boolean useJwt, String loginProcessingUrl, UserDetailsProcessor userDetailsProcessor) throws Exception {
        AuthenticationManager authenticationManager = null;
        if (!useJwt) {
            ObjectProvider<AuthenticationManager> objectProvider = context.getBeanProvider(AuthenticationManager.class);
            authenticationManager = objectProvider.getIfAvailable(() -> {
                AuthenticationConfiguration authenticationConfiguration = context.getBean(AuthenticationConfiguration.class);
                Assert.notNull(authenticationConfiguration, "authenticationConfiguration can not be null.");
                try {
                    return authenticationConfiguration.getAuthenticationManager();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        buildHttpSecurity(http, useJwt, loginProcessingUrl, userDetailsProcessor, authenticationManager);
    }

    private void buildHttpSecurity(HttpSecurity http, boolean useJwt, String loginProcessingUrl, UserDetailsProcessor userDetailsProcessor, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(configurer -> {
                configurer.authenticationEntryPoint(new SimpleAuthenticationEntryPoint())
                          .accessDeniedHandler(new SimpleAccessDeniedHandler());
            })
            .logout(configurer -> {
                configurer.addLogoutHandler(new SimpleLogoutHandler(userDetailsProcessor))
                          .logoutSuccessHandler(new SimpleLogoutSuccessHandler());
            });
        if (useJwt) {
            // jwt 必须配置于 UsernamePasswordAuthenticationFilter 之前
            http.sessionManagement(configurer -> {
                    configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS); // session 生成策略用无状态策略,不创建会话
                })
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsProcessor), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.formLogin(configurer -> {
                    configurer.loginProcessingUrl(StringUtils.isEmpty(loginProcessingUrl) ? "/login" : loginProcessingUrl);
                })
                .addFilterBefore(new LoginFilter(loginProcessingUrl, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .rememberMe(configurer -> configurer.alwaysRemember(true).userDetailsService(context.getBeanProvider(UserDetailsService.class).getIfAvailable(() -> userDetailsProcessor::loadUserByUsername)));
        }
    }

}
