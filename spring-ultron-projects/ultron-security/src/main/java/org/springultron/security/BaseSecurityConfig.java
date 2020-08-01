package org.springultron.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springultron.security.filter.JwtAuthenticationFilter;
import org.springultron.security.filter.LoginFilter;
import org.springultron.security.handler.SimpleAccessDeniedHandler;
import org.springultron.security.handler.SimpleAuthenticationEntryPoint;
import org.springultron.security.handler.SimpleLogoutHandler;
import org.springultron.security.handler.SimpleLogoutSuccessHandler;

/**
 * Spring Security 配置基类
 * <p>
 * 默认支持前后端分离
 * 前后端分离返回json
 * 支持有状态的session认证和无状态的JWT认证
 * </p>
 *
 * @author brucewuu
 * @date 2020/1/6 19:26
 */
public abstract class BaseSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 是否使用JWT无状态登录
     * 使用JWT必须实现UserDetailsProcessor接口，并注入到Bean容器
     */
    protected abstract boolean useJwt();

    /**
     * formLogin登录地址
     */
    protected String loginProcessingUrl() {
        return "/login";
    }

    @Autowired(required = false)
    private UserDetailsProcessor userDetailsProcessor;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, // 允许对于静态资源的无授权访问
                "/*.html",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/favicon.ico",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/v2/api-docs-ext/**",
                "/webjars/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new SimpleAuthenticationEntryPoint())
                .accessDeniedHandler(new SimpleAccessDeniedHandler())
                .and()
                .logout()
                .addLogoutHandler(new SimpleLogoutHandler(userDetailsProcessor))
                .logoutSuccessHandler(new SimpleLogoutSuccessHandler());
        if (useJwt()) {
            // jwt 必须配置于 UsernamePasswordAuthenticationFilter 之前
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 生成策略用无状态策略,不创建会话
                    .and()
                    .addFilterBefore(new JwtAuthenticationFilter(userDetailsProcessor), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.formLogin()
                    .loginProcessingUrl(loginProcessingUrl())
                    .and()
                    .addFilterBefore(new LoginFilter(loginProcessingUrl(), authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class)
                    .rememberMe()
                    .alwaysRemember(true);
        }
    }

    /**
     * 全局获取用户详情配置
     */
    @Bean
    @ConditionalOnBean(UserDetailsProcessor.class)
    public UserDetailsService userDetailService() {
        return username -> userDetailsProcessor.loadUserByUsername(username);
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}