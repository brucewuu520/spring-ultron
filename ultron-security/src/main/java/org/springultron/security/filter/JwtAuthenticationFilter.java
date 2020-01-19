package org.springultron.security.filter;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springultron.core.utils.StringUtils;
import org.springultron.security.handler.SimpleAuthenticationEntryPoint;
import org.springultron.security.jwt.JwtProcessor;
import org.springultron.security.util.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt认证过滤器 用于拦截请求 提取jwt认证
 *
 * @author brucewuu
 * @date 2020/1/9 11:49
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHENTICATION_PREFIX = "Bearer ";

    private SimpleAuthenticationEntryPoint authenticationEntryPoint;
    private JwtProcessor jwtProcessor;

    public JwtAuthenticationFilter(JwtProcessor jwtProcessor) {
        this.authenticationEntryPoint = new SimpleAuthenticationEntryPoint();
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 如果已经通过认证
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(jwt) && jwt.startsWith(AUTHENTICATION_PREFIX)) {
            jwt = jwt.substring(AUTHENTICATION_PREFIX.length());
            try {
                final String username = jwtProcessor.obtainUsername(jwt);
                UserDetails userDetails = jwtProcessor.getUserByUsername(username);
                SecurityUtils.checkUserDetails(userDetails);
                // 构建用户认证token
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 放入安全上下文中
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JwtException e) {
                authenticationEntryPoint.commence(request, response, new BadCredentialsException("token is invalid"));
            } catch (AuthenticationException e) {
                authenticationEntryPoint.commence(request, response, e);
            }
        }
        chain.doFilter(request, response);
    }
}