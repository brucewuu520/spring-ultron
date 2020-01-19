package org.springultron.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
 * @author brucewuu
 * @date 2020/1/6 19:26
 */
public class BaseSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtProcessor jwtProcessor;

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers()
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 生成策略用无状态策略,不创建会话
                .and()
                .exceptionHandling().authenticationEntryPoint(new SimpleAuthenticationEntryPoint()).accessDeniedHandler(new SimpleAccessDeniedHandler())
                .and()
//                .authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(filterSecurityInterceptorObjectPostProcessor())
//                .and()
//                .addFilterBefore(preLoginFilter, UsernamePasswordAuthenticationFilter.class)
                // jwt 必须配置于 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(new JwtAuthenticationFilter(jwtProcessor), UsernamePasswordAuthenticationFilter.class)
                // 登录  成功后返回jwt token  失败后返回 错误信息
                .formLogin().loginProcessingUrl("process").successHandler(new SimpleAuthenticationSuccessHandler(jwtProcessor)).failureHandler(new SimpleAuthenticationFailureHandler())
                .and()
                .logout().addLogoutHandler(new SimpleLogoutHandler()).logoutSuccessHandler(new SimpleLogoutSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        SecurityConfigurerAdapter
        super.configure(auth);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}