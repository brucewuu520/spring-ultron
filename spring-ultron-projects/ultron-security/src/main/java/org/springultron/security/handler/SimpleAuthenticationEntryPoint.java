package org.springultron.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证异常处理
 *
 * @author brucewuu
 * @date 2020/1/9 10:26
 */
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ApiResult<Void> result;
        if (e == null) {
            result = ApiResult.unauthorized();
        } else {
            result = ApiResult.unauthorized(e.getMessage());
        }
        WebUtils.renderJson(response, result);
    }
}