package org.springultron.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.WebUtils;

import java.io.IOException;

/**
 * 退出登录成功处理
 *
 * @author brucewuu
 * @date 2020/1/10 10:13
 */
public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        WebUtils.renderJson(response, ApiResult.success("logout success"));
    }
}