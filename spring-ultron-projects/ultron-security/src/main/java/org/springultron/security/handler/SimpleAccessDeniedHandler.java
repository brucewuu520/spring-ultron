package org.springultron.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无访问权限处理
 *
 * @author brucewuu
 * @date 2020/1/9 10:42
 */
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ApiResult<Void> result;
        if (e == null) {
            result = ApiResult.forbidden();
        } else {
            result = ApiResult.forbidden(e.getMessage());
        }
        WebUtils.renderJson(response, result);
    }
}