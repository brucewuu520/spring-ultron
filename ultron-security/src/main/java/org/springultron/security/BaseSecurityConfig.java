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
import org.springultron.security.handler.*;
import org.springultron.security.jwt.JwtProcessor;

/**
 * security 配置基类
 *
 * @author brucewuu
 * @date 2020/1/6 19:26
 */
public class BaseSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtProcessor jwtProcessor;

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
        http.csrf().disable() // 由于使用的是JWT，这里不需要csrf
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 生成策略用无状态策略,不创建会话
                .and()
                .exceptionHandling().authenticationEntryPoint(new SimpleAuthenticationEntryPoint()).accessDeniedHandler(new SimpleAccessDeniedHandler())
                .and()
                // jwt 必须配置于 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(new JwtAuthenticationFilter(jwtProcessor), UsernamePasswordAuthenticationFilter.class)
                // 登录成功后返回jwt token 失败后返回错误信息
                .formLogin().loginProcessingUrl("process").successHandler(new SimpleAuthenticationSuccessHandler(jwtProcessor)).failureHandler(new SimpleAuthenticationFailureHandler())
                .and()
                .logout().addLogoutHandler(new SimpleLogoutHandler()).logoutSuccessHandler(new SimpleLogoutSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return username -> jwtProcessor.getUserByUsername(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}