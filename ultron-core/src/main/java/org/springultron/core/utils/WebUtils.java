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
import java.util.function.Predicate;

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
            return null;
        }
        final Predicate<String> IP_PREDICATE = (ip) -> Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip);
        String ip = request.getHeader("X-Requested-For");
        if (IP_PREDICATE.test(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getRemoteAddr();
        }
        return Strings.isBlank(ip) ? null : ip.split(",")[0];
    }

    /**
     * ResponseBody返回json
     *
     * @param response HttpServletResponse
     * @param result   ResponseBody对象
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(Jackson.toJson(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}