package org.springultron.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.WebUtils;

import java.io.IOException;

/**
 * 认证成功处理器
 *
 * @author brucewuu
 * @date 2020/1/10 11:06
 */
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        WebUtils.renderJson(response, ApiResult.success(username, "login success"));
    }
}
