package org.springultron.boot.servlet;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springultron.core.utils.Jackson;
import org.springultron.core.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Servlet 工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-26 15:47
 * @Description:
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

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
     * 获取ip
     *
     * @return {String}
     */
    @Nullable
    public static String getIP() {
        return getIP(WebUtils.getRequest());
    }

    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return ip address
     */
    @Nullable
    public static String getIP(HttpServletRequest request) {
        if (null == request) {
            return StringUtils.EMPTY;
        }
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.isBlank(ip) ? StringUtils.EMPTY : ip.split(",")[0];
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
