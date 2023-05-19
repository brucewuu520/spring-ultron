package org.springultron.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.WebUtils;

import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author brucewuu
 * @date 2020/1/10 11:07
 */
public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        WebUtils.renderJson(response, ApiResult.fail(1004, e.getMessage()));
    }
}
