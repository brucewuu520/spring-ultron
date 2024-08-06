package org.springultron.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springultron.core.utils.StringUtils;
import org.springultron.security.UserDetailsProcessor;
import org.springultron.security.handler.SimpleAuthenticationEntryPoint;
import org.springultron.security.util.SecurityUtils;

import java.io.IOException;

/**
 * jwt认证过滤器 用于拦截请求 提取jwt认证
 *
 * @author brucewuu
 * @date 2020/1/9 11:49
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHENTICATION_PREFIX = "Bearer ";

    private final SimpleAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsProcessor userDetailsProcessor;

    public JwtAuthenticationFilter(UserDetailsProcessor userDetailsProcessor) {
        Assert.notNull(userDetailsProcessor, "UserDetailsProcessor.class bean can not be null");
        this.authenticationEntryPoint = new SimpleAuthenticationEntryPoint();
        this.userDetailsProcessor = userDetailsProcessor;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        // 如果已经通过认证
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(jwt) && jwt.startsWith(AUTHENTICATION_PREFIX)) {
            jwt = jwt.substring(AUTHENTICATION_PREFIX.length());
            try {
                final String username = userDetailsProcessor.obtainUsername(jwt);
                UserDetails userDetails = userDetailsProcessor.loadUserByUsername(username);
                // 检查用户信息
                SecurityUtils.checkUserDetails(userDetails);
                // 构建用户认证token，放入安全上下文中
                SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities()));
                // 执行过滤器链
                chain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                authenticationEntryPoint.commence(request, response, new CredentialsExpiredException("token is expired"));
            } catch (JwtException e) {
                authenticationEntryPoint.commence(request, response, new BadCredentialsException("token is invalid"));
            } catch (AuthenticationException e) {
                authenticationEntryPoint.commence(request, response, e);
            } finally {
                SecurityContextHolder.clearContext();
            }
        } else {
            // 执行过滤器链
            chain.doFilter(request, response);
        }
    }
}