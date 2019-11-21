package org.springultron.core.utils;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Servlet 工具
 *
 * @author brucewuu
 * @date 2019-06-26 15:47
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    private WebUtils() {
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletRequest
     */
    @Nullable
    public static HttpServletRequest getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(a -> (ServletRequestAttributes) a)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return HttpServletResponse
     */
    @Nullable
    public static HttpServletResponse getResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(a -> (ServletRequestAttributes) a)
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    /**
     * ResponseBody返回json
     *
     * @param response HttpServletResponse
     * @param result   ResponseBody对象
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(Jackson.toJson(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}