package org.springultron.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springultron.core.jackson.Jackson;
import org.springultron.security.handler.SimpleAuthenticationFailureHandler;
import org.springultron.security.handler.SimpleAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录过滤器（ session认证模式下的登录过滤器）
 *
 * @author brucewuu
 * @date 2020/6/14 16:27
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(new SimpleAuthenticationSuccessHandler());
        setAuthenticationFailureHandler(new SimpleAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        final String contentType = request.getContentType();
        if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            JsonNode jsonNode = null;
            try {
                jsonNode = Jackson.readTree(request.getInputStream());
            } catch (IOException ignored) {
            }
            if (jsonNode == null || jsonNode.isEmpty()) {
                throw new UsernameNotFoundException("username and password is empty");
            }

            String username = jsonNode.get(getUsernameParameter()).asText();
            String password = jsonNode.get(getPasswordParameter()).asText();
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}
