package org.springultron.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springultron.security.handler.*;

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
     */
    protected abstract boolean useJwt();

    @Autowired
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
                    .and()
                    .addFilterAt(new LoginFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class)
                    .rememberMe()
                    .alwaysRemember(true);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return username -> userDetailsProcessor.loadUserByUsername(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}